package io.github.okafke.aitcg.llm.gpt;

import java.util.List;

public record GPTConversation(String model, List<GPTMessage> messages) {
    public void add(GPTMessage message) {
        messages.add(message);
    }

}
