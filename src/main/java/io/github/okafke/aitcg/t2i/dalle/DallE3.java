package io.github.okafke.aitcg.t2i.dalle;

import io.github.okafke.aitcg.t2i.Text2ImageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images/create#images/create-response_format">
 *     https://platform.openai.com/docs/api-reference/images/create#images/create-response_format</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class DallE3 implements Text2ImageModel {
    @Qualifier("openaiRestTemplate")
    private final RestTemplate openaiRestTemplate;

    @Value("${openai.t2i.model}")
    private String model;

    @Value("${openai.api.t2i.url}")
    private String apiUrl;

    @Value("${openai.t2i.quality}")
    private String quality;

    @Value("${openai.t2i.size}")
    private String size;

    // @Value("${openai.t2i.n}")
    @SuppressWarnings("FieldCanBeLocal")
    private final int n = 1;

    @Override
    public byte[] generateImage(String prompt) throws DallEException {
        // TODO: use natural style?
        DallERequest request = new DallERequest(model, prompt, "vivid", "b64_json", quality, size, n);
        try {
            DallEResponse response = openaiRestTemplate.postForObject(apiUrl, request, DallEResponse.class);
            if (response == null || response.data() == null || response.data().isEmpty()) {
                throw new DallEException("Response to " + request + " was " + response);
            }

            for (DallEResponse.DallEResponseData data : response.data()) {
                if (data.b64_json() == null) {
                    log.warn("Response to request " + request + " contained non b64_json data?!");
                    continue;
                }

                try {
                    if (data.revised_prompt() != null) {
                        log.info("Returning image for prompt '" + prompt + "'. Prompt has been revised to '" + data.revised_prompt() + "'");
                    }

                    return Base64.getDecoder().decode(data.b64_json());
                } catch (IllegalArgumentException e) {
                    throw new DallEException("Failed to decode " + data.b64_json(), e);
                }
            }

            throw new DallEException("Response " + response + " to request " + request + " did not contain any b64_json data");
        } catch (RestClientException e) {
            throw new DallEException(e);
        }
    }

    @Async
    public CompletableFuture<byte[]> generateImageAsync(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return generateImage(prompt);
            } catch (DallEException e) {
                log.error("Failed to generate image for prompt " + prompt, e);
                throw new RuntimeException(e);
            }
        }, Runnable::run);
    }

}
