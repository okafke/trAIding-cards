package io.github.okafke.aitcg.text2image.dalle;

public record DallERequest(String model, String prompt, String response_format, String quality, String size, int n) {

}
