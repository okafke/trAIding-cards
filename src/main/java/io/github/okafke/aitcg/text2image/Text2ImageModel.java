package io.github.okafke.aitcg.text2image;

import java.io.IOException;

public interface Text2ImageModel {
    byte[] generateImage(String prompt) throws IOException;

}
