package io.github.whiterpl.sl13.atoms;

import java.util.Random;

public enum Direction {

    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1),
    NONE(0, 0);

    private int xMod;
    private int yMod;

    Direction(int xMod, int yMod) {
        this.xMod = xMod;
        this.yMod = yMod;
    }

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

    public static int getXMod(Direction direction) {
        return direction.xMod;
    }

    public static int getYMod(Direction direction) {
        return direction.yMod;
    }

    public int getXMod() {
        return Direction.getXMod(this);
    }

    public int getYMod() {
        return Direction.getYMod(this);
    }
}
