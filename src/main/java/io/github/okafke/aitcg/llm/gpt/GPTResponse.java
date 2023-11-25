package io.github.okafke.aitcg.llm.gpt;

import java.util.List;

public record GPTResponse(List<Choice> choices) {
    public record Choice(GPTMessage message, int index) {

    }

}
