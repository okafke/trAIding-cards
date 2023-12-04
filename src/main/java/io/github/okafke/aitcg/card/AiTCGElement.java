package io.github.okafke.aitcg.card;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public enum AiTCGElement {
    FIRE,
    WATER,
    EARTH,
    AIR;

    private static final Random RANDOM = new Random();

    public AiTCGElement getOpposite() {
        return switch (this) {
            case FIRE -> WATER;
            case WATER -> FIRE;
            case EARTH -> AIR;
            case AIR -> EARTH;
        };
    }

    public static AiTCGElement interpret(String textIn) {
        String text = textIn.toLowerCase();
        AiTCGElement result;
        String error = "";
        if (text.contains("fire")) {
            result = FIRE;
        } else if (text.contains("water")) {
            result = WATER;
        } else if (text.contains("earth")) {
            result = EARTH;
        } else if (text.contains("air")) {
            result = AIR;
        } else {
            error = "Failed to find fitting element from text. ";
            AiTCGElement[] values = values();
            result = values[RANDOM.nextInt(values.length)];
        }

        log.info(error + "Interpreting " + result + " from text " + textIn);
        return result;
    }

}
