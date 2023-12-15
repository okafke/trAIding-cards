package io.github.okafke.aitcg.card.render;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.okafke.aitcg.card.AiTCGCard;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
public class PlayingCardTest {
    @Autowired
    private ImageService imageService;

    @Test
    @SneakyThrows
    public void createPlayingCards() {
        Files.walk(Paths.get("playing_cards"))
                .filter(file -> file.toString().endsWith(".json"))
                .forEach(file -> readFile(file.toFile()));
    }

    @SneakyThrows
    private void readFile(File file) {
        log.info("Reading " + file.getName());
        AiTCGCard card = new ObjectMapper().readValue(file, AiTCGCard.class);
        if (card.symbol() == null) {
            return;
        }

        assert card.symbolColor() != null;
        int color = Long.decode(card.symbolColor()).intValue();
        BufferedImage bufferedImage = imageService.createCard(card);
        bufferedImage = imageService.addCardSymbol(bufferedImage, new Color(color), card.symbol());
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(file.getParentFile(), card.element() + "-" + card.symbol() + ".jpeg"))) {
            fileOutputStream.write(imageService.toJpeg(imageService.convertType(bufferedImage, BufferedImage.TYPE_INT_RGB)));
        }
    }

}
