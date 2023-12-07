package io.github.okafke.aitcg.card.printing;

import com.hp.jipp.encoding.IppPacket;
import com.hp.jipp.model.MediaCol;
import com.hp.jipp.model.Types;
import com.hp.jipp.trans.IppPacketData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.hp.jipp.model.Types.mediaCol;
import static com.hp.jipp.model.Types.requestingUserName;

@Slf4j
@Getter
@ToString
@RequiredArgsConstructor
public class IppPrintJob {
    private final String documentFormat;
    @ToString.Exclude
    private final Printer printer;
    @ToString.Exclude
    private final byte[] bytes;
    private final int id;
    private final String info;
    private boolean failed;
    private int ran;

    public void print() {
        log.info("Printing " + id + " " + info + " on printer " + printer.getPrinterIp());
        failed = false;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        MediaCol mediaCollection = new MediaCol();
        mediaCollection.setMediaTopMargin(0);
        mediaCollection.setMediaBottomMargin(0);
        // since we are portrait mode left and right are the long sides of the card.
        // my printer leaves white space on the left side, so I added some margin on the right
        mediaCollection.setMediaRightMargin(50);
        mediaCollection.setMediaLeftMargin(0);

        IppPacket printRequest = IppPacket.printJob(printer.getPrinterIp())
                .putOperationAttributes(
                        requestingUserName.of("trAIding-cards"),
                        Types.documentFormat.of(documentFormat))
                .putJobAttributes(mediaCol.of(mediaCollection))
                .build();
        try {
            IppPacketData response = new HttpIppClientTransport(true).sendData(printer.getPrinterIp(), new IppPacketData(printRequest, in));
            log.info("Received printing response for " + id + ": " + response.getPacket().prettyPrint(100, "   "));
            // TODO: check for error, there is "Server busy"
        } catch (IOException e) {
            log.error("Failed to get response from printer for " + id, e);
            failed = true;
        } finally {
            ran++;
        }
    }

}
