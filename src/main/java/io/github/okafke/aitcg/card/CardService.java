package io.github.okafke.aitcg.card;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CardService {
    private static final WebPImageReaderSpi WEB_P_IMAGE_READER_SPI = new WebPImageReaderSpi();
    private static final BufferedImage TEMPLATE = loadCardTemplate();
    private static final int X_OFFSET = 131;
    private static final int Y_OFFSET = 214;
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    public byte[] createCard(AiTCGCard card) throws IOException {
        BufferedImage image = loadFromByteArray(card.image());
        image = scale(image);
        image = overlay(image);
        var os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        return os.toByteArray();
    }

    private BufferedImage scale(BufferedImage image) {
        double scaleX = (double) WIDTH / image.getWidth();
        double scaleY = (double) HEIGHT / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp op = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    private BufferedImage overlay(BufferedImage overlay) {
        BufferedImage result = new BufferedImage(TEMPLATE.getWidth(), TEMPLATE.getHeight(), TEMPLATE.getType());
        Graphics2D g2d = result.createGraphics();

        g2d.drawImage(TEMPLATE, 0, 0, null);
        g2d.drawImage(overlay, X_OFFSET, Y_OFFSET, null);
        g2d.dispose();

        return result;
    }

    public static BufferedImage loadFromByteArray(byte[] bytes) throws IOException {
        var reader = WEB_P_IMAGE_READER_SPI.createReaderInstance();
        try (ImageInputStream is = new ByteArrayImageInputStream(bytes)) {
            reader.setInput(is);
            return reader.read(0);
        }
    }

    @SneakyThrows
    private static BufferedImage loadCardTemplate() {
        return loadFromByteArray(new ClassPathResource("images/card_template.webp").getContentAsByteArray());
    }

}
