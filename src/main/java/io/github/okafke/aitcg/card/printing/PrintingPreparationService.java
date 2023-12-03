package io.github.okafke.aitcg.card.printing;

import io.github.okafke.aitcg.card.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Prepares two cards for printing on my Canon SELPHY CP1500 printer.
 * The image needs to be rotated to fill the entire photo paper.
 */
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class PrintingPreparationService {
    private final ImageService imageService;

    public BufferedImage prepareImageForPrinting(BufferedImage image) {
        return rotate(image);
    }

    /*private BufferedImage scaleToFitMediaSize(BufferedImage image) {
        double scaleFactor = PrintingService.MEDIA_SIZE.getSize(MediaSize.INCH)[1] * 300.0 / image.getHeight();

        int scaledWidth = (int) (image.getWidth() * scaleFactor);
        int scaledHeight = (int) (image.getHeight() * scaleFactor);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());

        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        return scaledImage;
    }*/

    // TODO: instead try out the orientation of the printer?
    private BufferedImage rotate(BufferedImage image) {
        BufferedImage rotated = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        Graphics2D g = rotated.createGraphics();

        AffineTransform at = new AffineTransform();
        at.translate(image.getHeight(), 0);
        at.rotate(Math.PI / 2);

        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return rotated;
    }

}
