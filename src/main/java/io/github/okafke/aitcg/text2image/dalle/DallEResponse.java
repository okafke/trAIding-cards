package io.github.okafke.aitcg.text2image.dalle;

import org.springframework.lang.Nullable;

import java.util.List;

public record DallEResponse(long created, List<DallEResponseData> data) {
    public record DallEResponseData(@Nullable String url, @Nullable String b64_json, @Nullable String revised_prompt) {

    }

}
