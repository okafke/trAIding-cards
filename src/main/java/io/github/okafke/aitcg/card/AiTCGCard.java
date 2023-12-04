package io.github.okafke.aitcg.card;

import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.t2i.dalle.DallEResponse;

import java.util.UUID;

public record AiTCGCard(
        UUID uuid,
        String name,
        CreatureStats stats,
        AiTCGElement element,
        String text,
        GPTConversation conversation,
        DallEResponse dallEResponse) {
}
