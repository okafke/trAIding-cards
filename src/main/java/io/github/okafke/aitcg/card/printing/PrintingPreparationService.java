package io.github.okafke.aitcg.card.printing;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Prepares two cards for printing on my Canon SELPHY CP1500 printer.
 * The image needs to be rotated to fill the entire photo paper, also no
 */
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class PrintingPreparationService {
    // TODO: instead use Orientation.Landscape?!
    public BufferedImage prepareImageForPrinting(BufferedImage image) {
        return rotateAndUseMakeIntRGB(image);
    }

    // rotates the image and makes it BufferedImage.TYPE_INT_RGB, which is important to get it as jpeg
    private BufferedImage rotateAndUseMakeIntRGB(BufferedImage image) {
        BufferedImage rotated = new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB);
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
