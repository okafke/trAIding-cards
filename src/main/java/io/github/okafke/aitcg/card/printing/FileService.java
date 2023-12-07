package io.github.okafke.aitcg.card.printing;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.okafke.aitcg.card.AiTCGCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {
    @Value("${files.save.printing.image:#{false}}")
    private boolean savePrintingImage;

    public void save(AiTCGCard card, byte[] png) {
        log.info("Saving card " + card.name() + " " + card.uuid());
        //noinspection ResultOfMethodCallIgnored
        Paths.get("cards").toFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(Paths.get("cards", card.uuid() + ".json").toFile())) {
            String cardJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(card);
            fos.write(cardJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Failed to save card json " + card, e);
        }

        try (FileOutputStream fos = new FileOutputStream(Paths.get("cards", card.uuid() + "-card.png").toFile())) {
            fos.write(png);
        } catch (IOException e) {
            log.error("Failed to save card png " + card, e);
        }
    }

    public void savePrintingImage(int printingId, UUID uuid1, UUID uuid2, byte[] jpegBytes) {
        if (!savePrintingImage) {
            return;
        }

        //noinspection ResultOfMethodCallIgnored
        Paths.get("printing").toFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(Paths.get("printing", printingId + "_" + uuid1 + "_" + uuid2 + ".jpeg").toFile())) {
            fos.write(jpegBytes);
        } catch (IOException e) {
            log.error("Failed to save printing jpeg" + uuid1 + "_" + uuid2, e);
        }
    }

    public List<String> getCardNames() {
        File directory = Paths.get("cards").toFile();
        File[] files = directory.listFiles();
        if (directory.exists() && directory.isDirectory() && files != null) {
            return Arrays.stream(files)
                    .filter(file -> file.isFile() && file.getName().endsWith(".json"))
                    .map(file -> file.getName().substring(0, file.getName().length() - ".json".length()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

}
