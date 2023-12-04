package io.github.okafke.aitcg.card.creation;

import io.github.okafke.aitcg.api.CardCreationRequest;
import io.github.okafke.aitcg.card.AiTCGElement;
import io.github.okafke.aitcg.llm.Prompts;
import io.github.okafke.aitcg.llm.gpt.ChatGPT;
import io.github.okafke.aitcg.llm.gpt.GPTConversation;
import io.github.okafke.aitcg.llm.gpt.GPTException;
import io.github.okafke.aitcg.llm.gpt.GPTMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class ElementService {
    private final ChatGPT llm;

    @Async
    public CompletableFuture<AiTCGElement> getElement(GPTMessage story, CardCreationRequest request) throws GPTException {
        GPTConversation elementConversation = llm.conversation();
        elementConversation.add(story);
        elementConversation.add(GPTMessage.user(Prompts.REQUEST_ELEMENT));
        elementConversation.max_tokens(10);

        GPTMessage elementResponse = llm.chat(elementConversation);
        log.info("Received element '" + elementResponse + "' for attributes " + request.attributes());
        return CompletableFuture.completedFuture(AiTCGElement.interpret(elementResponse.content()));
    }

}
