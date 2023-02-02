package io.github.whiterpl.sl13.atoms.region;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Region {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;

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

    public Tile getTile(Position pos) {
        return getTile(pos.x, pos.y);
    }

    public void addToQueue(Mob mob) {
        mob.setNextUpdateTurn(this.lastUpdateTurn + mob.getActionDelay());
        actionQueue.add(mob);
    }

    public void advanceQueue() {

        if (!actionQueue.contains(Game.getPlayerController().getPlayer())) {
            return;
        }

        while(actionQueue.peek() != Game.getPlayerController().getPlayer()) {

            Mob activeMob = actionQueue.peek();
            assert activeMob != null;
            activeMob.addDelay();
            actionQueue.poll().act(Game.getPlayerController());
            actionQueue.add(activeMob);
        }



        actionQueue.poll();
        Game.getPlayerController().getPlayer().addDelay();
        actionQueue.add(Game.getPlayerController().getPlayer());
    }

    public void removeFromQueue(Mob mob) {
        this.actionQueue.remove(mob);
    }

    public void setLastUpdateTurn(int turn) {this.lastUpdateTurn = turn;}
}
