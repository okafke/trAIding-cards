package io.github.okafke.aitcg.card;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class ImageServiceTest {
    public static final String FRIDGE_TEXT = """
    In the desolate wasteland, where only shadows whispered of a forgotten world, there emerged a surreal manifestation of man's folly.
    The flaming refrigerator creature, a grotesque fusion of nature and unnatural, prowled with an insatiable hunger, its fiery eyes reflecting the remnants of a civilization lost.
    Its presence sent tremors through the land, a chilling reminder of the burning desires and destructive appetites that had led to its monstrous creation.
    """;

    @Autowired
    private ImageService cardService;

    @Value("classpath:fiery_fridge_monster.webp")
    Resource fieryFridgeMonsterImage;

    @Value("classpath:fiery_fridge_monster_card.png")
    Resource fieryFridgeMonsterCard;

    @Test
    @SneakyThrows
    void testCardService() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        // TODO: make this actually test something, once we have attributes and stuff down!
        AiTCGCard cardWithShortTitle = new AiTCGCard("Fiery Hungry Fridge", AiTCGElement.FIRE, FRIDGE_TEXT, image);
        AiTCGCard cardWithLongTitle = new AiTCGCard("Fiery Fridge Monster, the Terrible!", AiTCGElement.FIRE, FRIDGE_TEXT, image);
        AiTCGCard cardWithVeryLongTitle = new AiTCGCard("Fiery Fridge Monster, the Terrible Super Fridge with ultra powers the movie!", AiTCGElement.FIRE, FRIDGE_TEXT, image);

        byte[] cardWithShortTitleBytes = cardService.createCard(cardWithShortTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card.png")) {
            outputStream.write(cardWithShortTitleBytes);
        }

        byte[] cardWithLongTitleBytes = cardService.createCard(cardWithLongTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card_long_title.png")) {
            outputStream.write(cardWithLongTitleBytes);
        }

        byte[] cardWithVeryLongTitleBytes = cardService.createCard(cardWithVeryLongTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card_very_long_title.png")) {
            outputStream.write(cardWithVeryLongTitleBytes);
        }

        //assertArrayEquals(imageWithShortTitle.getContentAsByteArray(), cardWithShortTitleBytes);
    }

    @Test
    public void testRemoveNewLines() {
        String inputString1 = "This is a text\nwith newlines\n\nand spaces.";
        String expectedOutput1 = "This is a text with newlines and spaces.";
        assertEquals(expectedOutput1, cardService.removeNewLines(inputString1));

        String inputString2 = "Single newline\nin this text.";
        String expectedOutput2 = "Single newline in this text.";
        assertEquals(expectedOutput2, cardService.removeNewLines(inputString2));

        String inputString3 = "No newlines in this text.";
        String expectedOutput3 = "No newlines in this text.";
        assertEquals(expectedOutput3, cardService.removeNewLines(inputString3));

        String inputString5 = "\n   Text with leading and trailing spaces.   \n";
        String expectedOutput5 = "Text with leading and trailing spaces.";
        assertEquals(expectedOutput5, cardService.removeNewLines(inputString5));
    }

}
