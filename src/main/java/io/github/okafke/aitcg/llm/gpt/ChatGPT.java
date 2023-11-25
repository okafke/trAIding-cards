package io.github.okafke.aitcg.llm.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class ChatGPT {
    @Qualifier("openaiRestTemplate")
    private final RestTemplate openaiRestTemplate;

    @Value("${openai.llm.model}")
    private String model;

    @Value("${openai.api.llm.url}")
    private String apiUrl;

    public GPTMessage chat(List<GPTMessage> request) throws GPTException {
        return chat(new GPTRequest(model, request));
    }

    public GPTMessage chat(GPTRequest request) throws GPTException {
        try {
            GPTResponse response = openaiRestTemplate.postForObject(apiUrl, request, GPTResponse.class);
            log.info("Response: " + response + " for request: " + request);
            if (response == null || response.choices() == null || response.choices().isEmpty()) {
                throw new GPTException("Response " + response + " had no valid choices!");
            }

            return response.choices().get(0).message();
        } catch (RestClientException e) {
            throw new GPTException(e);
        }
    }

}
