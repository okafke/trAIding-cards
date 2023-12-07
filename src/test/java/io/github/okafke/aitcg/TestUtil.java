package io.github.okafke.aitcg;

import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.AiTCGElement;
import io.github.okafke.aitcg.card.CardStats;
import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.t2i.dalle.DallEResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtil {
    public static AiTCGCard card(String name, String text, byte[] image) {
        return card(name, AiTCGElement.FIRE, text, image);
    }

    public static AiTCGCard card(String name, AiTCGElement element, String text, byte[] image) {
        List<DallEResponse.DallEResponseData> data = new ArrayList<>(1);
        data.add(new DallEResponse.DallEResponseData(image, ""));
        DallEResponse response = new DallEResponse(0L, data);
        return new AiTCGCard(UUID.randomUUID(), name, CardStats.roll(), element, null, null, text, new GPTConversation("", new ArrayList<>()), response);
    }

}
