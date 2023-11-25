package io.github.okafke.aitcg.card;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
@SpringBootTest
public class CardServiceTest {
    @Autowired
    private CardService cardService;

    @Value("classpath:fiery_fridge_monster.webp")
    Resource fieryFridgeMonsterImage;

    @Value("classpath:fiery_fridge_monster_card.png")
    Resource fieryFridgeMonsterCard;

    @Test
    @SneakyThrows
    void testCardService() {
        byte[] image = fieryFridgeMonsterImage.getContentAsByteArray();
        AiTCGCard card = new AiTCGCard("Fiery Fridge Monster", "A fiery fridge monster!", image);
        byte[] output = cardService.createCard(card);
        assertArrayEquals(fieryFridgeMonsterCard.getContentAsByteArray(), output);
    }

}
