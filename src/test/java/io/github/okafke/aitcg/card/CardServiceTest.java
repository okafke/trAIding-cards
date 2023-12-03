package io.github.okafke.aitcg.card;

import io.github.okafke.aitcg.api.CardCreationRequest;
import io.github.okafke.aitcg.card.creation.CardService;
import io.github.okafke.aitcg.llm.gpt.GPTException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@Disabled // Uses the OpenAI api
@SpringBootTest
public class CardServiceTest {
    @Autowired
    private CardService cardService;

    @Test
    @Disabled //uses the OpenAI api
    @SneakyThrows
    public void testCreateCard() {
        CardCreationRequest request = new CardCreationRequest(List.of("lazy"), "olli");
        cardService.createCard(request);
    }

}
