package io.github.okafke.aitcg.card.printing;

import io.github.okafke.aitcg.TestUtil;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.ImageService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import static io.github.okafke.aitcg.card.ImageServiceTest.FRIDGE_TEXT;

// @Disabled this actually prints
@SpringBootTest
public class PrintingServiceTest {
    @Autowired
    private PrintingPreparationService printingPreparationService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PrintingService printingService;

    @Value("classpath:fiery_fridge_monster.webp")
    Resource fieryFridgeMonsterImage;

    @Value("classpath:fiery_fridge_monster_card.png")
    Resource fieryFridgeMonsterCard;

    @Test
    @SneakyThrows
    // @Disabled this actually prints
    public void testPrintingService() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        // TODO: make this actually test something, once we have attributes and stuff down!
        AiTCGCard cardWithShortTitle = TestUtil.card("Fiery Hungry Fridge", FRIDGE_TEXT, image);
        AiTCGCard cardWithLongTitle = TestUtil.card("Fiery Fridge Monster, the Terrible!", FRIDGE_TEXT, image);

        BufferedImage bufferedImage = imageService.twoCards(cardWithLongTitle, cardWithShortTitle);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_two_cards.png")) {
            outputStream.write(imageService.toPNG(bufferedImage));
        }

        bufferedImage = printingPreparationService.prepareImageForPrinting(bufferedImage);
        try (FileOutputStream outputStream = new FileOutputStream("ignored_images/fiery_fridge_two_cards_rotated.png")) {
            outputStream.write(imageService.toPNG(bufferedImage));
        }

        //printingService.print(UUID.randomUUID(), bufferedImage);
    }

}