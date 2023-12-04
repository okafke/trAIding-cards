package io.github.okafke.aitcg.card;

import java.util.Random;

public record CreatureStats(int attack, int defense, int speed, int level) {
    public CreatureStats increase(int by) {
        int attack = Math.max(attack() + by, 100);
        int defense = Math.max(defense() + by, 100);
        int speed = Math.max(speed() + by, 100);
        int level = (int) (attack + defense + speed / 3.0);
        return new CreatureStats(attack, defense, speed, level);
    }

    // TODO: give attributes multipliers: fire = +attack, flying = +speed, earth = +defense -speed
    public static CreatureStats roll() {
        Random random = new Random();
        int attack = random.nextInt(100);
        int defense = random.nextInt(100);
        int speed = random.nextInt(100);
        int level = (int) ((attack + defense + speed) / 3.0);
        return new CreatureStats(attack, defense, speed, level);
    }

}
