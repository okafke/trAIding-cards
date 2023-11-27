package io.github.okafke.aitcg.llm.gpt;

public record GPTMessage(String role, String content) {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";

    public static GPTMessage user(String content) {
        return new GPTMessage(ROLE_USER, content);
    }

    public static GPTMessage assistant(String content) {
        return new GPTMessage(ROLE_ASSISTANT, content);
    }

}

