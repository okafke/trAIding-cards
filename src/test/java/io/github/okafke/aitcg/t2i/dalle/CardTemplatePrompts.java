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
@SpringBootTest
@SuppressWarnings("NewClassNamingConvention")
public class CardTemplatePrompts {
    @Autowired
    private DallE3 dalle3;

    @Test
    @SneakyThrows
    @Disabled("Requests the OpenAI API")
    void testGettingCardTemplate() {
        byte[] image = assertDoesNotThrow(() -> dalle3.sendRequest(
                "A a card in a trading card game. " +
                        "In the upper middle there is a square space for an image. " +
                        "Above the image there is some space for a name and more space under the image for card text, " +
                        "all these areas are still to be on the card and take up most of the space of the card." +
                        "The card has a light blue background with some ornaments.").get().getFirstData());
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "card_template" + new Random().nextLong() + ".webp").toFile())) {
            os.write(image);
        }
    }

    @Test
    @SneakyThrows
    @Disabled("Requests the OpenAI API")
    void testGettingOrnamentsForCardTemplate() {
        byte[] image = assertDoesNotThrow(() -> dalle3.sendRequest(
                "A nice symmetrical ornament to be printed on a card in a rectangle format.").get().getFirstData());
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "card_ornaments" + new Random().nextLong() + ".webp").toFile())) {
            os.write(image);
        }
    }

    @Test
    @SneakyThrows
    @Disabled("Requests the OpenAI API")
    void testGettingSymbols() {
        byte[] image = assertDoesNotThrow(() -> dalle3.sendRequest(
                "Four simple symbols representing the elements: fire (red), earth (brown, cracked soil), water (blue), and air (green, a vortex of wind). " +
                        "Each symbol should be distinct and easily recognizable." +
                        " Please create four unique and straightforward images, one for each element. " +
                        "Ensure that each image has a transparent background to allow for easy integration.").get().getFirstData());
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "symbols" + new Random().nextLong() + ".webp").toFile())) {
            os.write(image);
        }
    }

    @Test
    @SneakyThrows
    @Disabled("Requests the OpenAI API")
    void testGetFridgeMonster() {
        byte[] image = assertDoesNotThrow(() -> dalle3.sendRequest(
                "Create a highly detailed and surreal scene featuring a flaming refrigerator creature as the central focus," +
                        " surrounded by a post-apocalyptic landscape with intricate details, capturing the intense heat, hunger, and menace in the creature's expression."
        ).get().getFirstData());
        try (FileOutputStream os = new FileOutputStream(Paths.get("templates", "fridge.webp").toFile())) {
            os.write(image);
        }
    }

}
