package io.github.okafke.aitcg.gpt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Disabled // this requests stuff from the ChatGPT api
@SpringBootTest
public class ChatGPTTest {
    @Autowired
    private ChatGPT gpt;

    @Test
    @Disabled
    void testGPT() {
        GPTMessage message = new GPTMessage("user", "Hello, I am performing a Spring Boot test, could you say Test?");
        GPTMessage response = assertDoesNotThrow(() -> gpt.chat(Collections.singletonList(message)));
        assertEquals("assistant", response.role());
        log.info(String.valueOf(response));
    }

}
