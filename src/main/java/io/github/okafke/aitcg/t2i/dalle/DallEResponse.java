package io.github.okafke.aitcg.t2i.dalle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.okafke.aitcg.util.Base64ByteArrayJson;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true) // idk why the json contains "firstData"...
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
