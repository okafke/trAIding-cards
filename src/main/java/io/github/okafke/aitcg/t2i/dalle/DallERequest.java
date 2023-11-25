package io.github.okafke.aitcg.t2i.dalle;

public record DallERequest(String model, String prompt, String response_format, String quality, String size, int n) {

}
