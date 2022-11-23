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

    //GETTERS & SETTERS

    public Tile[][] getTiles() {
        return tiles;
    }

    // STATIC METHOD

    public static float getDistance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // METHODS

    public Tile getTile(int x, int y) {
        if (x < WIDTH && y < HEIGHT) {
            return tiles[x][y];
        } else return null;
    }

    public void addToQueue(Mob mob) {
        mob.setNextUpdateTurn(this.lastUpdateTurn + mob.getActionDelay());
        actionQueue.add(mob);
    }

    public void advanceQueue(PlayerController playerController) {

        while(actionQueue.peek() != playerController.player) {
            Mob activeMob = actionQueue.peek();
            assert activeMob != null;
            activeMob.addDelay();
            actionQueue.poll().act(this);
            actionQueue.add(activeMob);
        }

        actionQueue.poll();
        playerController.getPlayer().addDelay();
        actionQueue.add(playerController.getPlayer());
    }




}
