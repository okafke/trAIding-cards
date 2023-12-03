package io.github.okafke.aitcg.card.printing;

import com.hp.jipp.encoding.IppPacket;
import com.hp.jipp.encoding.Tag;
import com.hp.jipp.model.MediaCol;
import com.hp.jipp.model.Operation;
import com.hp.jipp.trans.IppPacketData;
import com.twelvemonkeys.imageio.plugins.jpeg.JPEGImageWriter;
import com.twelvemonkeys.imageio.plugins.jpeg.JPEGImageWriterSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.hp.jipp.model.Types.*;


public class Main {
    private static final String CMD_NAME = "trAIding-cards";

    public static void main(String[] args) throws IOException {
        URI uri = URI.create("http://192.168.178.67:631/ipp/print");
        try (FileInputStream fin = new FileInputStream(Paths.get("ignored_images", "fiery_fridge_two_cards_rotated.png").toFile())) {
            /*MediaCol mediaCollection = new MediaCol();
            mediaCollection.setMediaTopMargin(0);
            mediaCollection.setMediaBottomMargin(0);
            mediaCollection.setMediaLeftMargin(0);
            mediaCollection.setMediaRightMargin(0);
            IppPacket packet = IppPacket.printJob(uri)
                    .putOperationAttributes(requestingUserName.of("trAIding-cards"), attributesCharset.of("utf-8"), attributesNaturalLanguage.of("en"), printerUri.of(uri), documentFormat.of("image/png"))
                    .putJobAttributes(mediaCol.of(mediaCollection))
                    .build();*/
            BufferedImage image = ImageIO.read(fin);
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newImage.createGraphics();
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.out.println(ImageIO.write(newImage, "jpg", baos));
            try (FileOutputStream fos = new FileOutputStream(Paths.get("ignored_images", "fiery_fridge.jpeg").toFile())) {
                fos.write(baos.toByteArray());
            }
            //System.out.println(new HttpIppClientTransport(true).sendData(uri, new IppPacketData(packet, fin)).getPacket().prettyPrint(10, "   "));
            print(new ByteArrayInputStream(baos.toByteArray()), new HttpIppClientTransport(true), uri);
        }
    }

    private static void print(InputStream in, HttpIppClientTransport transport, URI uri) throws IOException {
        String format = "image/jpeg";
        // Query for supported document formats
        IppPacket attributeRequest = IppPacket.getPrinterAttributes(uri)
                .putOperationAttributes(
                        requestingUserName.of(CMD_NAME),
                        requestedAttributes.of(documentFormatSupported.getName()))
                .build();

        System.out.println("\nSending " + attributeRequest.prettyPrint(100, "  "));
        IppPacketData request = new IppPacketData(attributeRequest);
        IppPacketData response = transport.sendData(uri, request);
        System.out.println("\nReceived: " + response.getPacket().prettyPrint(100, "  "));

        // Make sure the format is supported
        List<String> formats = response.getPacket().getStrings(Tag.printerAttributes, documentFormatSupported);
        if (!formats.contains(format)) {
            throw new IOException(format + " format not supported by printer in " + formats);
        }

        MediaCol mediaCollection = new MediaCol();
        mediaCollection.setMediaTopMargin(0);
        mediaCollection.setMediaBottomMargin(0);
        mediaCollection.setMediaLeftMargin(0);
        mediaCollection.setMediaRightMargin(0);
        // Deliver the print request
        IppPacket printRequest = IppPacket.printJob(uri)
                .putOperationAttributes(
                        requestingUserName.of(CMD_NAME),
                        documentFormat.of(format))
                .putJobAttributes(mediaCol.of(mediaCollection))
                .build();

        System.out.println("\nSending " + printRequest.prettyPrint(100, "  "));
        request = new IppPacketData(printRequest, in);
        response = transport.sendData(uri, request);
        System.out.println("\nReceived: " + response.getPacket().prettyPrint(100, "  "));
    }

}