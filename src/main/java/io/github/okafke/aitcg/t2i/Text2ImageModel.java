package io.github.okafke.aitcg.t2i;

import java.io.IOException;

public interface Text2ImageModel {
    byte[] generateImage(String prompt) throws IOException;

}
