package io.github.okafke.aitcg.card.printing;

import com.hp.jipp.encoding.IppPacket;
import com.hp.jipp.model.MediaCol;
import com.hp.jipp.trans.IppPacketData;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

import static com.hp.jipp.model.Types.*;


public class Main {
    public static void main(String[] args) throws IOException {
        URI uri = URI.create("http://192.168.178.67:631/ipp/print");
        try (FileInputStream fin = new FileInputStream(Paths.get("fiery_fridge_two_cards_rotated.png").toFile())) {
            MediaCol mediaCollection = new MediaCol();
            mediaCollection.setMediaTopMargin(0);
            mediaCollection.setMediaBottomMargin(0);
            mediaCollection.setMediaLeftMargin(0);
            mediaCollection.setMediaRightMargin(0);
            new HttpIppClientTransport(true).sendData(uri, new IppPacketData(
                IppPacket.getPrinterAttributes(uri)
                         .putOperationAttributes(attributesCharset.of("utf-8"), attributesNaturalLanguage.of("en"), printerUri.of(uri))
                         .putJobAttributes(mediaCol.of(mediaCollection))
                         .build(),
                fin));
        }
    }

}