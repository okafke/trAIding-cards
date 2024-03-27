package io.github.okafke.aitcg.card.render;

import com.twelvemonkeys.imageio.plugins.webp.WebPImageReaderSpi;
import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.AiTCGElement;
import io.github.okafke.aitcg.card.CardStats;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.davidmoten.text.utils.WordWrap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageService {
    private static final WebPImageReaderSpi WEB_P_IMAGE_READER_SPI = new WebPImageReaderSpi();
    private static final BufferedImage CARD_SYMBOL = loadFromClassPathResource(new ClassPathResource("images/empty2x.webp"));
    private static final Map<AiTCGElement, BufferedImage> TEMPLATES = loadCardTemplates();
    private static final BufferedImage DEFAULT_TEMPLATE = loadDefaultCardTemplate();
    private final CardFormat format = CardFormat.DEFAULT2X;

    public byte[] createPNG(AiTCGCard card) throws IOException {
        BufferedImage image = createCard(card);
        return toPNG(image);
    }

    public BufferedImage createCard(AiTCGCard card) throws IOException {
        BufferedImage image = loadFromByteArray(card.dallEResponse().getFirstData());
        image = scale(image);
        image = overlay(image, TEMPLATES.getOrDefault(card.element(), DEFAULT_TEMPLATE));

        drawTitle(image, card.name());
        drawText(image, card.text());
        drawStats(image, card.stats());

        return image;
    }

    public BufferedImage cropImage(BufferedImage originalImage, int topOffset, int bottomOffset, int leftOffset, int rightOffset) {
        int newWidth = originalImage.getWidth() - leftOffset - rightOffset;
        int newHeight = originalImage.getHeight() - topOffset - bottomOffset;
        return originalImage.getSubimage(leftOffset, topOffset, newWidth, newHeight);
    }

    public BufferedImage twoCards(AiTCGCard card1, AiTCGCard card2) throws IOException {
        BufferedImage image1 = createCard(card1);
        BufferedImage image2 = createCard(card2);
        return twoCards(image1, image2);
    }

    public BufferedImage twoCards(BufferedImage image1, BufferedImage image2) {
        BufferedImage result = new BufferedImage(image1.getWidth() * 2, image1.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(image1, 0, 0, null);
        g2d.drawImage(image2, image1.getWidth(), 0, null);
        g2d.dispose();

        return result;
    }

    public byte[] toPNG(RenderedImage image) throws IOException {
        var os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        return os.toByteArray();
    }

    public BufferedImage convertType(BufferedImage bufferedImage, int type) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), type);
        Graphics2D g2d = result.createGraphics();
        g2d.drawImage(bufferedImage, 0, 0, null);
        g2d.dispose();
        return result;
    }

    /**
     * The image has to have a none alpha type!!!
     * @see BufferedImage#TYPE_INT_RGB
     */
    public byte[] toJpeg(RenderedImage image) throws IOException {
        var os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", os);
        return os.toByteArray();
    }

    public BufferedImage addCardSymbol(BufferedImage image, Color color, String cardSymbol) {
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(CARD_SYMBOL, format.cardSymbolX(), format.cardSymbolY(), null);

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("Serif", Font.BOLD, cardSymbol.length() > 1 ? format.symbolFontSize2() : format.symbolFontSize1());
        g2d.setFont(font);
        g2d.setColor(color);
        FontMetrics metrics = g2d.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(cardSymbol, g2d);
        //g2d.drawString(cardSymbol, (int) (format.cardSymbolX() + CARD_SYMBOL.getWidth() / 2 - bounds.getWidth() / 2), (int) (format.cardSymbolY() + CARD_SYMBOL.getHeight() / 2 - bounds.getHeight() / 2 + metrics.getAscent()));
        if (cardSymbol.length() > 1) {
            writeStringWithShadow(cardSymbol, (int) (format.cardSymbolX() + CARD_SYMBOL.getWidth() / 2 - bounds.getWidth() / 2), (int) (format.cardSymbolY() + CARD_SYMBOL.getHeight() / 2 - bounds.getHeight() / 2 + metrics.getAscent()), g2d);

        } else {
            writeStringWithShadow(cardSymbol, (int) (format.cardSymbolX() + 2 + CARD_SYMBOL.getWidth() / 2 - bounds.getWidth() / 2), (int) (format.cardSymbolY() - 2 + CARD_SYMBOL.getHeight() / 2 - bounds.getHeight() / 2 + metrics.getAscent()), g2d);
        }

        g2d.dispose();
        return image;
    }

    private void writeStringWithShadow(String string, int x, int y, Graphics2D graphics2D) {
        Color color = graphics2D.getColor();
        /*boolean drawLarge = true;
        if (drawLarge) {
            Font font = graphics2D.getFont();
            Font largeFont = new Font(font.getName(), font.getStyle(), font.getSize() + 4);
            graphics2D.setFont(largeFont);
            graphics2D.setColor(color.equals(Color.RED) ? Color.BLACK : Color.RED);
            graphics2D.drawString(string, x - 2, y);
            graphics2D.setFont(font);
        } else {*/
            graphics2D.setColor(Color.GRAY);
            graphics2D.drawString(string, x + 3, y + 3);
        //}

        graphics2D.setColor(color);
        graphics2D.drawString(string, x, y);
    }

    private BufferedImage scale(BufferedImage image) {
        double scaleX = (double) format.width() / image.getWidth();
        double scaleY = (double) format.height() / image.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp op = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(image, null);
    }

    private BufferedImage overlay(BufferedImage overlay, BufferedImage template) {
        BufferedImage result = new BufferedImage(template.getWidth(), template.getHeight(), template.getType());
        Graphics2D g2d = result.createGraphics();

        g2d.drawImage(template, 0, 0, null);
        g2d.drawImage(overlay, format.imageXOffset(), format.imageYOffset(), null);
        g2d.dispose();

        return result;
    }

    private void drawTitle(BufferedImage image, String title) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font font = new Font("Serif", Font.BOLD, format.titleFontSize());
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        FontMetrics metrics = g2d.getFontMetrics();
        double scale = Math.min(1.0, (double) format.titleMaxWidth() / metrics.stringWidth(title));
        int x = (int) (format.titleXOffset() / (Math.max(0.01, scale)));
        // this ensures that the text stays centered in the middle of the white title box, if someone has a better solution, yes please
        int y = (int) (format.titleYOffset() / (Math.max(0.01, scale)) - (1.0 - scale) * metrics.getHeight() * 0.5);

        g2d.scale(scale, scale);
        g2d.drawString(title, x, y);
        g2d.dispose();
    }

    // TODO: max lines 6
    private void drawText(BufferedImage image, String textIn) {
        // replace all new line characters, adding a space if two non-space characters would lie directly adjacent to each other.
        String text = removeNewLines(textIn);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font font = new Font("Serif", Font.PLAIN, format.textFontSize());
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        FontMetrics metrics = g2d.getFontMetrics();

        int maxY = format.maxLines() * metrics.getHeight() + metrics.getHeight();
        var lines = split(text, metrics);
        double currentMaxY = lines.size() * metrics.getHeight() + metrics.getHeight();
        if (currentMaxY > maxY) {
            // TODO: calculate this?
            for (int i = format.textFontSize() - 1; i > 0; i--) {
                font = new Font("Serif", Font.PLAIN, i);
                g2d.setFont(font);
                g2d.setColor(Color.BLACK);
                metrics = g2d.getFontMetrics();
                lines = split(text, metrics);
                currentMaxY = lines.size() * metrics.getHeight() + metrics.getHeight();
                if (currentMaxY <= maxY) {
                    break;
                }
            }
        }

        int y = format.textYOffset() + metrics.getHeight();
        for (String line : lines) {
            g2d.drawString(line, format.textXOffset(), y);
            y += metrics.getHeight();
        }

        g2d.dispose();
    }

    private List<String> split(String text, FontMetrics metrics) {
        return WordWrap.from(text)
                .maxWidth(format.textMaxWidth())
                .newLine(System.lineSeparator())
                .includeExtraWordChars("~")
                .excludeExtraWordChars("_")
                .insertHyphens(true)
                .breakWords(true)
                .stringWidth(s -> metrics.stringWidth(s.toString()))
                .wrapToList();
    }

    private void drawStats(BufferedImage image, CardStats stats) {
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font font = new Font("Serif", Font.BOLD, format.statsFontSize());
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        g2d.drawString(stats.attack() + "", 190 * format.statsMultiplier(), 985 * format.statsMultiplier());
        g2d.drawString(stats.defense() + "", 330 * format.statsMultiplier(), 985 * format.statsMultiplier());
        g2d.drawString(stats.speed() + "", 455 * format.statsMultiplier(), 985 * format.statsMultiplier());
        g2d.drawString(stats.magic() + "", 584 * format.statsMultiplier(), 985 * format.statsMultiplier());
        g2d.dispose();
    }

    String removeNewLines(String text) {
        return text.replaceAll("(\\r\\n|\\r|\\n)", " ").replaceAll(" +", " ").trim();
    }

    public static BufferedImage loadFromByteArray(byte[] bytes) throws IOException {
        try {
            return ImageIO.read(new ByteArrayImageInputStream(bytes));
        } catch (IOException e) {
            log.error("Failed to read bytes, trying WebP alone", e);
            var reader = WEB_P_IMAGE_READER_SPI.createReaderInstance();
            reader.setInput(new ByteArrayImageInputStream(bytes));
            return reader.read(0);
        }
    }

    @SneakyThrows
    private static BufferedImage loadFromClassPathResource(ClassPathResource classPathResource) {
        return loadFromByteArray(classPathResource.getContentAsByteArray());
    }

    @SneakyThrows
    private static BufferedImage loadDefaultCardTemplate() {
        return loadFromByteArray(new ClassPathResource("images/card_template.webp").getContentAsByteArray());
    }

    @SneakyThrows
    private static Map<AiTCGElement, BufferedImage> loadCardTemplates() {
        var result = new EnumMap<AiTCGElement, BufferedImage>(AiTCGElement.class);
        result.put(AiTCGElement.AIR, loadFromByteArray(new ClassPathResource("images/air_card2x.webp").getContentAsByteArray()));
        result.put(AiTCGElement.FIRE, loadFromByteArray(new ClassPathResource("images/fire_card2x.webp").getContentAsByteArray()));
        result.put(AiTCGElement.EARTH, loadFromByteArray(new ClassPathResource("images/earth_card2x.webp").getContentAsByteArray()));
        result.put(AiTCGElement.WATER, loadFromByteArray(new ClassPathResource("images/water_card2x.webp").getContentAsByteArray()));
        return result;
    }

}
