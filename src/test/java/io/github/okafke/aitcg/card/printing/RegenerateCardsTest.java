package io.github.okafke.aitcg.card.printing;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.render.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
public class RegenerateCardsTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private ImageService imageService;

    @Test
    public void generateCardsInCardsFolder() {
        for (File file : fileService.getFilesInCardsFolder()) {
            if (file.getName().endsWith(".json")) {
                try {
                    AiTCGCard card = new ObjectMapper().readValue(file, AiTCGCard.class);
                    byte[] pngBytes = imageService.createPNG(card);
                    fileService.save(card, pngBytes);
                } catch (IOException e) {
                    log.error("Failed to read/save " + file, e);
                }
            }
        }
    }

}
