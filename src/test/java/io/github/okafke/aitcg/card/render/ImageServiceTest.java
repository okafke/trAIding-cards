package io.github.okafke.aitcg.card.render;

import io.github.okafke.aitcg.TestUtil;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.AiTCGElement;
import io.github.okafke.aitcg.card.printing.PrintingService;
import io.github.okafke.aitcg.card.render.ImageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;

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
    private ImageService imageService;

    @Value("classpath:fiery_fridge_monster.webp")
    Resource fieryFridgeMonsterImage;

    @Value("classpath:fiery_fridge_monster_card.png")
    Resource fieryFridgeMonsterCard;

    @Autowired
    private PrintingService printingService;

    @Test
    @SneakyThrows
    public void testCardService() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        // TODO: make this actually test something, once we have attributes and stuff down!
        AiTCGCard cardWithShortTitle = TestUtil.card("Fiery Hungry Fridge", FRIDGE_TEXT, image);
        AiTCGCard cardWithLongTitle = TestUtil.card("Fiery Fridge Monster, the Terrible!", FRIDGE_TEXT, image);
        AiTCGCard cardWithVeryLongTitle = TestUtil.card("Fiery Fridge Monster, the Terrible Super Fridge with ultra powers the movie!", FRIDGE_TEXT, image);

        byte[] cardWithShortTitleBytes = imageService.creatPNG(cardWithShortTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card.png")) {
            outputStream.write(cardWithShortTitleBytes);
        }

        byte[] cardWithLongTitleBytes = imageService.creatPNG(cardWithLongTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card_long_title.png")) {
            outputStream.write(cardWithLongTitleBytes);
        }

        byte[] cardWithVeryLongTitleBytes = imageService.creatPNG(cardWithVeryLongTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_monster_card_very_long_title.png")) {
            outputStream.write(cardWithVeryLongTitleBytes);
        }

        try (var fis = new FileInputStream(Paths.get("ignored_images", "cello.webp").toFile())) {
            byte[] bytes = fis.readAllBytes();
            AiTCGCard cello = TestUtil.card("Melody Queen",
                    AiTCGElement.AIR,
                    """
                    The wooden cello, adorned with carved comical features, came to life as the night fell. With her sound-hole smiles and animated crown pegs, she danced in the moonlight, her bow transforming into a magical wand, conjuring melodic spells. The vivacious background echoed with floating music notes and unfolding staves, while rests blossomed into whimsical objects, creating a joyous and surreal celebration of music in an imaginary world.
                    """, bytes);
            byte[] celloBytes = imageService.creatPNG(cello);
            try (FileOutputStream outputStream = new FileOutputStream("ignored_images/cello_card2.png")) {
                outputStream.write(celloBytes);
            }
        }

        //assertArrayEquals(imageWithShortTitle.getContentAsByteArray(), cardWithShortTitleBytes);
    }

    @Test
    @SneakyThrows
    public void testTwoCards() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        // TODO: make this actually test something, once we have attributes and stuff down!
        AiTCGCard cardWithShortTitle = TestUtil.card("Fiery Hungry Fridge", FRIDGE_TEXT, image);
        AiTCGCard cardWithLongTitle = TestUtil.card("Fiery Fridge Monster, the Terrible!", FRIDGE_TEXT, image);

        BufferedImage bufferedImage = imageService.twoCards(cardWithLongTitle, cardWithShortTitle);
        byte[] bytes = imageService.toPNG(bufferedImage);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_two_cards.png")) {
            outputStream.write(bytes);
        }
    }

    @Test
    @SneakyThrows
    public void testTwoCards2() {
        try (FileInputStream fis = new FileInputStream(Paths.get("ignored_images", "cello_card2.png").toFile());
             FileInputStream fis2 = new FileInputStream(Paths.get("images", "12c8829f-106e-4f33-b5cb-09af9b3b4321-card.webp").toFile())) {
            BufferedImage image1 = ImageIO.read(fis);
            BufferedImage image2 = ImageIO.read(fis2);
            BufferedImage bufferedImage = imageService.twoCards(image1, image2);
            try (FileOutputStream outputStream = new FileOutputStream("ignored_images/two_cards_cello.jpeg")) {
                ImageIO.write(bufferedImage, "jpeg", outputStream);
            }
        }
    }

    @Test
    public void testRemoveNewLines() {
        String inputString1 = "This is a text\nwith newlines\n\nand spaces.";
        String expectedOutput1 = "This is a text with newlines and spaces.";
        assertEquals(expectedOutput1, imageService.removeNewLines(inputString1));

        String inputString2 = "Single newline\nin this text.";
        String expectedOutput2 = "Single newline in this text.";
        assertEquals(expectedOutput2, imageService.removeNewLines(inputString2));

        String inputString3 = "No newlines in this text.";
        String expectedOutput3 = "No newlines in this text.";
        assertEquals(expectedOutput3, imageService.removeNewLines(inputString3));

        String inputString5 = "\n   Text with leading and trailing spaces.   \n";
        String expectedOutput5 = "Text with leading and trailing spaces.";
        assertEquals(expectedOutput5, imageService.removeNewLines(inputString5));
    }

}
