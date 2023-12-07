package io.github.okafke.aitcg.card;

import java.util.Random;

public record CardStats(int attack, int defense, int speed, int magic) {
    public CardStats increase(int by) {
        int attack = Math.min(attack() + by, 100);
        int defense = Math.min(defense() + by, 100);
        int speed = Math.min(speed() + by, 100);
        int magic = Math.min(magic() + by, 100);
        return new CardStats(attack, defense, speed, magic);
    }

    // TODO: give attributes multipliers: fire = +attack, flying = +speed, earth = +defense -speed
    public static CardStats roll() {
        Random random = new Random();
        int attack = random.nextInt(100);
        int defense = random.nextInt(100);
        int speed = random.nextInt(100);
        int magic = random.nextInt(100);
        return new CardStats(attack, defense, speed, magic);
    }

}
