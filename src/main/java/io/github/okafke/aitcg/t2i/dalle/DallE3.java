package io.github.okafke.aitcg.t2i.dalle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * <a href="https://platform.openai.com/docs/api-reference/images/create#images/create-response_format">
 *     https://platform.openai.com/docs/api-reference/images/create#images/create-response_format</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class DallE3 {
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

    @Async
    public CompletableFuture<DallEResponse> sendRequest(String prompt) throws DallEException {
        // TODO: use natural style?
        DallERequest request = new DallERequest(model, prompt, "vivid", "b64_json", quality, size, n);
        try {
            DallEResponse response = openaiRestTemplate.postForObject(apiUrl, request, DallEResponse.class);
            if (response == null || response.data() == null || response.data().isEmpty()) {
                throw new DallEException("Response to " + request + " was " + response);
            }

            response.getFirstData(); // verify that response contains data
            return CompletableFuture.completedFuture(response);
        } catch (RestClientException e) {
            throw new DallEException(e);
        }
    }

}
