package io.github.okafke.aitcg.llm.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent = true)
public final class GPTConversation {
    @JsonProperty("model")
    private final String model;
    @JsonProperty("messages")
    private final List<GPTMessage> messages;
    @JsonProperty("max_tokens")
    private @Nullable Integer max_tokens;

    public void add(GPTMessage message) {
        messages.add(message);
    }

    public GPTConversation copy() {
        return new GPTConversation(model, new ArrayList<>(messages), max_tokens);
    }

}
