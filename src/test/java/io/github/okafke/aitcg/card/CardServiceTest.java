package io.github.okafke.aitcg.card;

import io.github.okafke.aitcg.api.CardCreationRequest;
import io.github.okafke.aitcg.card.creation.CardService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CardServiceTest {
    @Autowired
    private CardService cardService;

    @Test
    @SneakyThrows
    @Disabled("uses the OpenAI api")
    public void testCreateCard() {
        CardCreationRequest request = new CardCreationRequest(List.of("cute", "sharp-teeth", "flying"), "unicorn");
        cardService.createCard(request, 0, UUID.randomUUID(), UUID.randomUUID());
    }

}
