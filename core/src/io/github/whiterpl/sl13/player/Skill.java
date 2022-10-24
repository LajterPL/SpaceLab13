package io.github.whiterpl.sl13.player;

public enum Skill {
    MELEE(0),
    RANGED(1),
    WILLPOWER(2),
    TOUGHNESS(3),
    STRENGTH(4),
    TINKERING(5),
    IDENTIFYING(6),
    HACKING(7),
    NEGOTIATION(8);

    private int value;

    Skill(int value) {
        this.value = value;
    }

    public int getIndex() {
        return this.value;
    }


}
