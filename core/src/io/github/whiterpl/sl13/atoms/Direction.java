package io.github.whiterpl.sl13.atoms;

import java.util.Random;

public enum Direction {

    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST,
    NONE;

    private static final Random randomGen = new Random();

    public static Direction getRandomDirection() {
        switch (randomGen.nextInt(9)) {
            case 0: return NORTH;
            case 1: return NORTH_EAST;
            case 2: return EAST;
            case 3: return SOUTH_EAST;
            case 4: return SOUTH;
            case 5: return SOUTH_WEST;
            case 6: return WEST;
            case 7: return NORTH_WEST;
            default: return NONE;
        }
    }
}
