package io.github.okafke.aitcg.llm.gpt;

public record GPTMessage(String role, String content) {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";

}

