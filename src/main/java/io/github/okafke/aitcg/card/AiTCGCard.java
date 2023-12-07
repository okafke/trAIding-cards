package io.github.okafke.aitcg.card;

import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.t2i.dalle.DallEResponse;
import org.springframework.lang.Nullable;

import java.util.UUID;

public record AiTCGCard(
        UUID uuid,
        String name,
        CardStats stats,
        AiTCGElement element,
        @Nullable UUID baseCard,
        @Nullable UUID nextCard,
        String text,
        GPTConversation conversation,
        DallEResponse dallEResponse
) {
}
