package io.github.okafke.aitcg.seed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SeedService {
    private final Random random = new Random();

    @Value("${ai.seed:#{null}}")
    private Integer seed;

    public int getSeed() {
        Integer seed = this.seed;
        return seed == null ? random.nextInt() : seed;
    }

}
