package io.github.okafke.aitcg.card.render;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@SpringBootTest
public class DoubleScaleTest {
    @Autowired
    private ImageService imageService;

    @Test
    public void doubleScaleImage() throws IOException {
        String[] templates = new String[]{"air_card.webp", "earth_card.webp", "fire_card.webp", "card_template.webp", "water_card.webp", "empty.webp"};
        for (String template : templates) {
            try (InputStream is = DoubleScaleTest.class.getClassLoader().getResourceAsStream("images/" + template)) {
                assert is != null;
                BufferedImage image = ImageService.loadFromByteArray(is.readAllBytes());
                // Get the original image dimensions
                int originalWidth = image.getWidth();
                int originalHeight = image.getHeight();

                // Create a new BufferedImage with twice the width and height
                int newWidth = originalWidth * 2;
                int newHeight = originalHeight * 2;
                BufferedImage enlargedImage = new BufferedImage(newWidth, newHeight, image.getType());

                // Get the graphics object from the new image
                Graphics2D g2d = enlargedImage.createGraphics();

                // Use AffineTransform to scale the original image and draw it onto the new image
                AffineTransform at = new AffineTransform();
                at.scale(2.0, 2.0); // Scale factor of 2 in both dimensions
                g2d.drawRenderedImage(image, at);

                // Dispose of the graphics object to free up resources
                g2d.dispose();


                byte[] bytes = imageService.toPNG(enlargedImage);
                try (FileOutputStream fos = new FileOutputStream(template.replace(".webp", "2x.webp"))) {
                    fos.write(bytes);
                }
            }
        }
    }

}
