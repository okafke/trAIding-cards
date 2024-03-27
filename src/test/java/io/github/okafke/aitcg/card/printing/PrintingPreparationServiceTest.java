package io.github.okafke.aitcg.card.printing;

import io.github.okafke.aitcg.TestUtil;
import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.render.ImageService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import static io.github.okafke.aitcg.card.render.ImageServiceTest.FRIDGE_TEXT;

@SpringBootTest
public class PrintingPreparationServiceTest {
    @Autowired
    private PrintingPreparationService printingPreparationService;

    @Autowired
    private ImageService imageService;

    @Value("classpath:fiery_fridge_monster.webp")
    Resource fieryFridgeMonsterImage;

    @Value("classpath:fiery_fridge_monster_card.png")
    Resource fieryFridgeMonsterCard;

    @Test
    @SneakyThrows
    public void testPrintingPreparationService() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        // TODO: make this actually test something, once we have attributes and stuff down!
        AiTCGCard cardWithShortTitle = TestUtil.card("Fiery Hungry Fridge", FRIDGE_TEXT, image);
        AiTCGCard cardWithLongTitle = TestUtil.card("Fiery Fridge Monster, the Terrible!", FRIDGE_TEXT, image);

        BufferedImage bufferedImage = imageService.twoCards(cardWithLongTitle, cardWithShortTitle);
        //bufferedImage = printingPreparationService.prepareImageForPrinting(bufferedImage);
        bufferedImage = imageService.addPrintingId(bufferedImage, 0);
        byte[] bytes = imageService.toPNG(bufferedImage);
        /*try (FileOutputStream os = new FileOutputStream("test.png")) {
            os.write(bytes);
        }*/
        // TODO: compare bytes to something
    }

}
