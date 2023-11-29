package io.github.okafke.aitcg.card;

import io.github.okafke.aitcg.api.CardCreationRequest;
import io.github.okafke.aitcg.llm.Prompts;
import io.github.okafke.aitcg.llm.gpt.ChatGPT;
import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.llm.gpt.GPTException;
import io.github.okafke.aitcg.llm.gpt.GPTMessage;
import io.github.okafke.aitcg.t2i.dalle.DallE3;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CardService {
    private final ImageService imageService;
    private final ChatGPT llm;
    private final DallE3 t2i;


    // TODO: make cross references to other cards in text
    // TODO: produce variant of card, evolution or something? Dall-E-2 can transform existing images
    // TODO: optimization, do not use a conversation but expect entire output at once in a certain format? Would save tokens!
    @Async
    public void createCard(CardCreationRequest request) throws GPTException {
        log.info("Received card creation request for the attributes: " + request.attributes());
        UUID uuid = UUID.randomUUID();
        // Get detailed Dall-E-3 prompt from ChatGPT.
        GPTConversation conversation = llm.conversation();
        GPTMessage promptRequest = GPTMessage.user(Prompts.ONLY_OUTPUT + "Given a " + request.type()
                + " object with the attributes " + Prompts.list(request.attributes())
                + " in a fantastic setting, you are to design a prompt for Dall-E," +
                " in high detail and with a fitting background." +
                " Place great emphasis on the attributes and ensure that Dall-E does not include Text in the image..");
        conversation.add(promptRequest);
        GPTMessage dallEPrompt = llm.chat(conversation);
        log.info("Received Dall-E prompt " + dallEPrompt + " for attributes " + request.attributes());
        CompletableFuture<byte[]> image = t2i.generateImageAsync(dallEPrompt.content());

        conversation.add(dallEPrompt);

        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.NAME));
        conversation.max_tokens(10);
        GPTMessage name = llm.chat(conversation);
        log.info("Received name '" + name + "' for attributes " + request.attributes());
        conversation.add(name);
        conversation.max_tokens(null);

        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.RANDOM_AUTHOR));
        GPTMessage story = llm.chat(conversation);
        log.info("Received story " + story + " for attributes " + request.attributes());

        GPTConversation elementConversation = llm.conversation();
        elementConversation.add(story);
        elementConversation.add(GPTMessage.user(Prompts.REQUEST_ELEMENT));
        elementConversation.max_tokens(10);

        GPTMessage elementResponse = llm.chat(elementConversation);
        log.info("Received element '" + elementResponse + "' for attributes " + request.attributes());
        AiTCGElement element = AiTCGElement.interpret(elementResponse.content());

        image.thenAccept(imageBytes -> {
            log.info("Got story, name and image for card " + name.content() + " for attributes " + request.attributes() + ": " + uuid);
            AiTCGCard tcgCard = new AiTCGCard(name.content(), element, story.content(), imageBytes);
            try (FileOutputStream outputStream = new FileOutputStream("images/" + uuid + "-image.webp")) {
                outputStream.write(imageBytes);
            } catch (IOException e) {
                log.error("Failed to save image images/" + uuid + ".webp!", e);
            }

            try {
                byte[] cardImageBytes = imageService.createCard(tcgCard);
                @Cleanup
                FileOutputStream fos = new FileOutputStream("images/" + uuid + "-card.webp");
                fos.write(cardImageBytes);
            } catch (IOException e) {
                log.error("Failed to save card images/" + uuid + ".webp!", e);
            }
        });

        image.join();
    }

}
