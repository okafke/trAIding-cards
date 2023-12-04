package io.github.okafke.aitcg.card.creation;

import io.github.okafke.aitcg.card.AiTCGCard;
import io.github.okafke.aitcg.card.AiTCGElement;
import io.github.okafke.aitcg.card.CreatureStats;
import io.github.okafke.aitcg.llm.Prompts;
import io.github.okafke.aitcg.llm.gpt.ChatGPT;
import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.llm.gpt.GPTMessage;
import io.github.okafke.aitcg.t2i.dalle.DallE3;
import io.github.okafke.aitcg.t2i.dalle.DallEResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Because our printer prints in A6 format we create two cards everytime.
 * That way, we can print the side by side then cut the A6 paper to get two cards.
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CardAlternationService {
    private final ChatGPT llm;
    private final DallE3 t2i;

    @Async
    public CompletableFuture<CardAlternation> createEvolution(UUID baseCardUUID, GPTConversation conversation) throws IOException {
        return createAlternation(baseCardUUID, conversation, Prompts.ONLY_OUTPUT + Prompts.NO_DALL_E + Prompts.EVOLUTION);
    }

    @Async
    public CompletableFuture<CardAlternation> createAlternation(UUID baseCardUUID, GPTConversation conversation, String requestDallEPrompt) throws IOException {
        UUID uuid = UUID.randomUUID();
        log.info("Creating alternation " + uuid + " for " + baseCardUUID);
        conversation.add(GPTMessage.user(requestDallEPrompt));
        GPTMessage dallEPrompt = llm.chat(conversation);
        CompletableFuture<DallEResponse> image = t2i.sendRequest(dallEPrompt.content());
        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.ALTERNATE_NAME));
        GPTMessage name = llm.chat(conversation);
        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.ALTERNATE_STORY));
        GPTMessage story = llm.chat(conversation);
        conversation.add(story);

        return CompletableFuture.completedFuture(new CardAlternation(uuid, baseCardUUID, dallEPrompt, name, story, conversation, image));
    }

    public record CardAlternation(UUID uuid, UUID baseCardUUID, GPTMessage dallEPrompt, GPTMessage name,
                                  GPTMessage story, GPTConversation conversation, CompletableFuture<DallEResponse> image) {
        public AiTCGCard awaitAiTCGCard(CreatureStats stats, AiTCGElement element) throws ExecutionException, InterruptedException {
            return new AiTCGCard(uuid, name.content(), stats, element, baseCardUUID, story.content(), conversation, image.get());
        }
    }

}
