package io.github.okafke.aitcg.llm.gpt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class ChatGPTTest {
    @Autowired
    private ChatGPT gpt;

    @Test
    @Disabled("Requests the OpenAI API")
    void testGPT() {
        GPTMessage message = new GPTMessage("user", "Hello, I am performing a Spring Boot test, could you say Test?");
        GPTMessage response = assertDoesNotThrow(() -> gpt.chat(Collections.singletonList(message)));
        assertEquals("assistant", response.role());
        log.info(String.valueOf(response));
    }

    @Test
    @Disabled("Requests the OpenAI API")
    void testGPT2() {
        GPTMessage message = new GPTMessage("user", "I want to design a simple, minimalistic logo. Center of the logo should be a golden disco ball, give me a prompt for Dall-E-3");
        GPTMessage response = assertDoesNotThrow(() -> gpt.chat(Collections.singletonList(message)));
        assertEquals("assistant", response.role());
        log.info(String.valueOf(response));
    }

}
