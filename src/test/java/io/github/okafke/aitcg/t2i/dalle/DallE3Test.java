package io.github.okafke.aitcg.t2i.dalle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@SpringBootTest
public class DallE3Test {
    @Autowired
    private DallE3 dalle3;

    @Test
    @Disabled("Requests the OpenAI API")
    void testDallE3() {
        //assertDoesNotThrow(() -> dalle3.generateImage("A cute baby sea otter"));
    }

    @Test
    @Disabled("Requests the OpenAI API")
    void testDallE23() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("ignored_images/logo" + new Random().nextInt() + ".png")) {
            //fos.write(dalle3.generateImage("Create a simple, minimalistic logo featuring a golden disco ball at the center with a sleek, modern design. The disco ball should be highly stylized with a radiant gold finish, reflecting light in a subtle way that suggests elegance and sophistication. The background should be clean and uncluttered to maintain the minimalist aesthetic. Please provide a vector-style logo with a balanced composition suitable for brand identity."));
        }
        //assertDoesNotThrow(() -> dalle3.generateImage("simple, abstract logo, flat, minimalistic, golden disco ball with microphone "));
    }

    @Test
    @SneakyThrows
    @Disabled("Requests the OpenAI API")
    void testDallE233() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("ignored_images/stats" + new Random().nextInt() + ".webp")) {
            fos.write(dalle3.sendRequest("Simple, minimalistic, logos. sword, shield, wings and stars to represent the stats of attack, defense, speed and level.").get().getFirstData());
        }
    }


}
