package io.github.okafke.aitcg.card;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import lombok.SneakyThrows;
import org.davidmoten.text.utils.WordWrap;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {
    private static final WebPImageReaderSpi WEB_P_IMAGE_READER_SPI = new WebPImageReaderSpi();
    private static final Map<AiTCGElement, BufferedImage> TEMPLATES = loadCardTemplates();
    private static final BufferedImage DEFAULT_TEMPLATE = loadDefaultCardTemplate();
    private static final int IMAGE_X_OFFSET = 131;
    private static final int IMAGE_Y_OFFSET = 214;

    private static final int TITLE_X_OFFSET = 76;
    private static final int TITLE_Y_OFFSET = 106;
    private static final int TITLE_MAX_WIDTH = 550;

    private static final int TEXT_X_OFFSET = 100;
    private static final int TEXT_Y_OFFSET = 826;
    private static final int TEXT_MAX_WIDTH = 569;

    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;

    public byte[] createCard(AiTCGCard card) throws IOException {
        BufferedImage image = createBufferedCard(card);
        return toBytes(image);
    }

    public BufferedImage createBufferedCard(AiTCGCard card) throws IOException {
        BufferedImage image = loadFromByteArray(card.image());
        image = scale(image);
        image = overlay(image, TEMPLATES.getOrDefault(card.element(), DEFAULT_TEMPLATE));

        drawTitle(image, card.name());
        drawText(image, card.text());

        return image;
    }

    public BufferedImage twoCards(AiTCGCard card1, AiTCGCard card2) throws IOException {
        BufferedImage image1 = createBufferedCard(card1);
        BufferedImage image2 = createBufferedCard(card2);
        BufferedImage result = new BufferedImage(image1.getWidth() * 2, image1.getHeight(), image1.getType());

        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(image1, 0, 0, null);
        g2d.drawImage(image2, image1.getWidth(), 0, null);
        g2d.dispose();

        return result;
    }

    public byte[] toBytes(BufferedImage image) throws IOException {
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

    private BufferedImage overlay(BufferedImage overlay, BufferedImage template) {
        BufferedImage result = new BufferedImage(template.getWidth(), template.getHeight(), template.getType());
        Graphics2D g2d = result.createGraphics();

        g2d.drawImage(template, 0, 0, null);
        g2d.drawImage(overlay, IMAGE_X_OFFSET, IMAGE_Y_OFFSET, null);
        g2d.dispose();

        return result;
    }

    private void drawTitle(BufferedImage image, String title) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font font = new Font("Serif", Font.BOLD, 40);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        FontMetrics metrics = g2d.getFontMetrics();
        double scale = Math.min(1.0, (double) TITLE_MAX_WIDTH / metrics.stringWidth(title));
        int x = (int) (TITLE_X_OFFSET / (Math.max(0.01, scale)));
        // this ensures that the text stays centered in the middle of the white title box, if someone has a better solution, yes please
        int y = (int) (TITLE_Y_OFFSET / (Math.max(0.01, scale)) - (1.0 - scale) * metrics.getHeight() * 0.5);

        g2d.scale(scale, scale);
        g2d.drawString(title, x, y);
        g2d.dispose();
    }

    private void drawText(BufferedImage image, String textIn) {
        // replace all new line characters, adding a space if two non-space characters would lie directly adjacent to each other.
        String text = removeNewLines(textIn);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font font = new Font("Serif", Font.PLAIN, 17);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        FontMetrics metrics = g2d.getFontMetrics();
        List<String> lines = WordWrap.from(text)
                .maxWidth(TEXT_MAX_WIDTH)
                .newLine(System.lineSeparator())
                .includeExtraWordChars("~")
                .excludeExtraWordChars("_")
                .insertHyphens(true)
                .breakWords(true)
                .stringWidth(s -> metrics.stringWidth(s.toString()))
                .wrapToList();

        int y = TEXT_Y_OFFSET + metrics.getHeight();
        for (String line : lines) {
            g2d.drawString(line, TEXT_X_OFFSET, y);
            y += g2d.getFontMetrics().getHeight();
        }

        g2d.dispose();
    }

    String removeNewLines(String text) {
        return text.replaceAll("(\\r\\n|\\r|\\n)", " ").replaceAll(" +", " ").trim();
    }

    public static BufferedImage loadFromByteArray(byte[] bytes) throws IOException {
        var reader = WEB_P_IMAGE_READER_SPI.createReaderInstance();
        try (ImageInputStream is = new ByteArrayImageInputStream(bytes)) {
            reader.setInput(is);
            return reader.read(0);
        }
    }

    @SneakyThrows
    private static BufferedImage loadDefaultCardTemplate() {
        return loadFromByteArray(new ClassPathResource("images/card_template.webp").getContentAsByteArray());
    }

    @SneakyThrows
    private static Map<AiTCGElement, BufferedImage> loadCardTemplates() {
        var result = new EnumMap<AiTCGElement, BufferedImage>(AiTCGElement.class);
        result.put(AiTCGElement.AIR, loadFromByteArray(new ClassPathResource("images/air_card.webp").getContentAsByteArray()));
        result.put(AiTCGElement.FIRE, loadFromByteArray(new ClassPathResource("images/fire_card.webp").getContentAsByteArray()));
        result.put(AiTCGElement.EARTH, loadFromByteArray(new ClassPathResource("images/earth_card.webp").getContentAsByteArray()));
        result.put(AiTCGElement.WATER, loadFromByteArray(new ClassPathResource("images/water_card.webp").getContentAsByteArray()));
        return result;
    }

}
