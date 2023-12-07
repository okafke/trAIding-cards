package io.github.okafke.aitcg.llm.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
public final class GPTConversation {
    @JsonProperty("model")
    private final String model;
    @JsonProperty("messages")
    private final List<GPTMessage> messages;
    @JsonProperty("max_tokens")
    private @Nullable Integer max_tokens;

    public GPTConversation(String model, List<GPTMessage> messages) {
        this(model, messages, null);
    }

    public GPTConversation(@JsonProperty("model") String model, @JsonProperty("messages") List<GPTMessage> messages, @JsonProperty("max_tokens") @Nullable Integer max_tokens) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
    }

    public void add(GPTMessage message) {
        messages.add(message);
    }

    public GPTConversation copy() {
        return new GPTConversation(model, new ArrayList<>(messages), max_tokens);
    }

}
