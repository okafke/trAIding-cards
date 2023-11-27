package io.github.okafke.aitcg.api;

import java.util.List;

public record CardCreationRequest(List<String> attributes, String type) {

}
