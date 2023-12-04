package io.github.okafke.aitcg.t2i.dalle;

// TODO: Seed? Currently Dall-E has no support for this :( ?
public record DallERequest(String model, String prompt, String style, String response_format, String quality, String size, int n) {

}
