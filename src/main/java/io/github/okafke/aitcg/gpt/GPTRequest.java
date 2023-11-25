package io.github.okafke.aitcg.gpt;

import java.util.ArrayList;
import java.util.List;

public record GPTRequest(String model, List<GPTMessage> messages) {
    public GPTRequest add(GPTMessage message) {
        List<GPTMessage> messages = new ArrayList<>(messages());
        messages.add(message);
        return new GPTRequest(model, messages);
    }

}
