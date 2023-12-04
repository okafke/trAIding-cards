package io.github.okafke.aitcg.card.creation;

import io.github.okafke.aitcg.card.ImageService;
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
import java.util.concurrent.CompletableFuture;

/**
 * Because our printer prints in A6 format we create two cards everytime.
 * That way, we can print the side by side then cut the A6 paper to get two cards.
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CardAlternationService {
    private final ImageService imageService;
    private final ElementService elementService;
    private final ChatGPT llm;
    private final DallE3 t2i;

    @Async
    public void createEvolution(GPTConversation conversation, int seed) throws IOException {
        createAlternation(conversation, seed, Prompts.ONLY_OUTPUT + Prompts.NO_DALL_E + Prompts.EVOLUTION);
    }

    @Async
    public void createAlternation(GPTConversation conversation, int seed, String requestDallEPrompt) throws IOException {
        conversation.add(GPTMessage.user(requestDallEPrompt));
        GPTMessage dallEPrompt = llm.chat(conversation);
        CompletableFuture<DallEResponse> image = t2i.sendRequest(dallEPrompt.content());
        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.ALTERNATE_NAME));
        GPTMessage name = llm.chat(conversation);
        conversation.add(GPTMessage.user(Prompts.ONLY_OUTPUT + Prompts.ALTERNATE_STORY));
        GPTMessage story = llm.chat(conversation);

    }

}
