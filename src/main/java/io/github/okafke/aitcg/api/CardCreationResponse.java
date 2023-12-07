package io.github.okafke.aitcg.api;

import java.util.UUID;

public record CardCreationResponse(int id, UUID uuid, UUID secondUUID) {

}
