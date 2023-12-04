package io.github.okafke.aitcg.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
public class FileService {
    public void save(AiTCGCard card, byte[] png) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        Paths.get("cards").toFile().mkdirs();
        String cardJson = new ObjectMapper().writeValueAsString(card);
        try (FileOutputStream fos = new FileOutputStream(Paths.get("cards", card.uuid() + ".json").toFile())) {
            fos.write(cardJson.getBytes(StandardCharsets.UTF_8));
        }

        try (FileOutputStream fos = new FileOutputStream(Paths.get("cards", card.uuid() + "-card.png").toFile())) {
            fos.write(png);
        }
    }

}
