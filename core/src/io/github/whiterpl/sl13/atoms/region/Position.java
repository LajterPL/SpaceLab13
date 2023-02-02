package io.github.whiterpl.sl13.atoms.region;

public class Position {
    public int x, y;

    public Position() {
        this(0, 0);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double getDistance(Position a, Position b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }
}
