package io.github.okafke.aitcg.text2image.dalle;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@Disabled // this requests stuff from the OpenAI api
@SpringBootTest
public class DallE3Test {
    @Autowired
    private DallE3 dalle3;

    @Test
    @Disabled
    void testDallE3() {
        assertDoesNotThrow(() -> dalle3.generateImage("A cute baby sea otter"));
    }

}
