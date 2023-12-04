package io.github.okafke.aitcg.card.printing;

import com.hp.jipp.encoding.IppPacket;
import com.hp.jipp.model.MediaCol;
import com.hp.jipp.trans.IppPacketData;
import io.github.okafke.aitcg.card.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import static com.hp.jipp.model.Types.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class PrintingService {
    public static final String FORMAT = "image/jpeg";

    private final ImageService imageService;

    @Value("${printer.uri:#{null}}")
    private URI printerURI;

    public void print(UUID uuid, BufferedImage image) throws IOException {
        if (printerURI == null) {
            log.info("Printing is disabled, not printing " + uuid);
            return;
        }

        log.info("Printing card " + uuid);
        ByteArrayInputStream in = new ByteArrayInputStream(imageService.toJpeg(image));
        MediaCol mediaCollection = new MediaCol();
        mediaCollection.setMediaTopMargin(0);
        mediaCollection.setMediaBottomMargin(0);
        // since we are portrait mode left and right are the long sides of the card.
        // my printer leaves white space on the left side, so I added some margin on the right
        mediaCollection.setMediaRightMargin(50);
        mediaCollection.setMediaLeftMargin(0);

        IppPacket printRequest = IppPacket.printJob(printerURI)
                .putOperationAttributes(
                        requestingUserName.of("trAIding-cards"),
                        documentFormat.of(FORMAT))
                .putJobAttributes(mediaCol.of(mediaCollection))
                .build();
        try {
            IppPacketData response = new HttpIppClientTransport(true).sendData(printerURI, new IppPacketData(printRequest, in));
            log.info("Received printing response for " + uuid + ": " + response.getPacket().prettyPrint(100, "   "));
            // TODO: check for error, there is "Server busy"
        } catch (IOException e) {
            log.error("Failed to get response from printer for " + uuid, e);
        }
    }

}
