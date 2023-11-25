package io.github.okafke.aitcg.t2i.dalle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@Disabled // this requests stuff from the OpenAI api
@SpringBootTest
@SuppressWarnings("NewClassNamingConvention")
public class CardTemplatePrompt {
    @Autowired
    private DallE3 dalle3;

    @Test
    @Disabled
    @SneakyThrows
    void testGettingCardTemplate() {
        byte[] image = assertDoesNotThrow(() -> dalle3.generateImage(
                "A a card in a trading card game. " +
                        "In the upper middle there is a square space for an image. " +
                        "Above the image there is some space for a name and more space under the image for card text, " +
                        "all these areas are still to be on the card and take up most of the space of the card." +
                        "The card has a light blue background with some ornaments."));
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "card_template" + new Random().nextLong() + ".webp").toFile())) {
            os.write(image);
        }
    }

    @Test
    @Disabled
    @SneakyThrows
    void testGettingOrnamentsForCardTemplate() {
        byte[] image = assertDoesNotThrow(() -> dalle3.generateImage(
                "A nice symmetrical ornament to be printed on a card in a rectangle format."));
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "card_ornaments" + new Random().nextLong() + ".webp").toFile())) {
            os.write(image);
        }
    }

}
