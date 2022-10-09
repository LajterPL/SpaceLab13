package io.github.whiterpl.sl13.atoms;

public class Region {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    Tile[][] tiles;

    public Region() {
        tiles = new Tile[WIDTH][HEIGHT];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        if (x < WIDTH && y < HEIGHT) {
            return tiles[x][y];
        } else return null;
    }
}
