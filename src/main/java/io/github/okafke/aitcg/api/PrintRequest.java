package io.github.okafke.aitcg.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.okafke.aitcg.util.Base64ByteArrayJson;
import org.springframework.lang.Nullable;

import java.net.URI;

public record PrintRequest(URI printerIp,
                           String documentFormat,
                           @JsonSerialize(using = Base64ByteArrayJson.Serializer.class)
                           @JsonDeserialize(using = Base64ByteArrayJson.Deserializer.class)
                           @Nullable byte[] file) {
}
