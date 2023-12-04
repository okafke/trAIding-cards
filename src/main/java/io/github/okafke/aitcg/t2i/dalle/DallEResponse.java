package io.github.okafke.aitcg.t2i.dalle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

public record DallEResponse(long created, List<DallEResponseData> data) {
    public record DallEResponseData(
            @JsonSerialize(using = Base64ByteArrayJson.Serializer.class)
            @JsonDeserialize(using = Base64ByteArrayJson.Deserializer.class)
            @Nullable byte[] b64_json,
            @Nullable String revised_prompt) {
    }

    public byte[] getFirstData() throws DallEException {
        return data.stream().map(DallEResponseData::b64_json)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new DallEException("Response " + this + " contained no data!"));
    }

}
