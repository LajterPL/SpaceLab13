package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.player.PlayerController;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Region {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    protected Tile[][] tiles;

    protected PriorityQueue<Mob> actionQueue;
    protected int lastUpdateTurn;

    public Region() {
        tiles = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile();
            }
        }

        actionQueue = new PriorityQueue<>(Comparator.comparingInt(Mob::getNextUpdateTurn));
        lastUpdateTurn = 0;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        if (x < WIDTH && y < HEIGHT) {
            return tiles[x][y];
        } else return null;
    }

    // METHODS

    public void addToQueue(Mob mob) {
        mob.setNextUpdateTurn(this.lastUpdateTurn + mob.getActionDelay());
        actionQueue.add(mob);
    }

    public void advanceQueue(PlayerController playerController) {
        while(actionQueue.peek() != playerController.player) {

        }
    }


}
