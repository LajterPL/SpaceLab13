package io.github.whiterpl.sl13.atoms.mob;

import com.badlogic.gdx.Gdx;
import io.github.whiterpl.sl13.atoms.*;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Mob extends Atom {

    protected short maxHp;
    protected short currentHp;

    protected short maxSp;
    protected short currentSp;

    protected short[] skills;

    protected List<Item> equipment;

    protected int credits;

    protected int x;
    protected int y;

    protected int actionDelay;
    protected int nextUpdateTurn;

    public Mob(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
        this.equipment = new ArrayList<>();
        this.skills = new short[9];
    }

    public Mob(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
        this.equipment = new ArrayList<>();
        this.skills = new short[9];
    }

    // GETTERS & SETTERS

    public short getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(short maxHp) {
        this.maxHp = maxHp;
    }

    public short getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(short currentHp) {
        this.currentHp = currentHp;
    }

    public short getMaxSp() {
        return maxSp;
    }

    public void setMaxSp(short maxSp) {
        this.maxSp = maxSp;
    }

    public short getCurrentSp() {
        return currentSp;
    }

    public void setCurrentSp(short currentSp) {
        this.currentSp = currentSp;
    }

    public short[] getSkills() {
        return skills;
    }

    public void setSkills(short[] skills) {
        this.skills = skills;
    }

    public List<Item> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Item> equipment) {
        this.equipment = equipment;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getActionDelay() {
        return actionDelay;
    }

    public int getNextUpdateTurn() {
        return nextUpdateTurn;
    }

    public void setNextUpdateTurn(int nextUpdateTurn) {
        this.nextUpdateTurn = nextUpdateTurn;
    }

    // METHODS

    public void placeMob(Region region, int x, int y) {
        Tile selectedTile = region.getTile(x, y);

        if (selectedTile.blocksPassage()) throw new IllegalArgumentException();
        else if (selectedTile.getMob() != null) throw new IllegalArgumentException();
        else {
            this.x = x;
            this.y = y;
            selectedTile.setMob(this);
            region.addToQueue(this);
        }
    }

    public boolean move(Region region, Direction direction) {
        int xMod, yMod;

        switch (direction) {
            case NORTH:
            case NORTH_EAST:
            case NORTH_WEST:
                if (this.y == 0) return false;
                yMod = -1;
                break;
            case SOUTH:
            case SOUTH_EAST:
            case SOUTH_WEST:
                if (this.y == Region.HEIGHT) return false;
                yMod = 1;
                break;
            default:
                yMod = 0;
        }

        switch (direction) {
            case WEST:
            case NORTH_WEST:
            case SOUTH_WEST:
                if (this.x == 0) return false;
                xMod = -1;
                break;
            case EAST:
            case NORTH_EAST:
            case SOUTH_EAST:
                if (this.x == Region.WIDTH) return false;
                xMod = 1;
                break;
            default:
                xMod = 0;
        }

        Tile selectedTile = region.getTile(this.x + xMod, this.y + yMod);
        if (selectedTile.blocksPassage()) return false;

        Mob tempMob = null;
        if (selectedTile.getMob() != null) tempMob = selectedTile.getMob();

        region.getTile(this.x, this.y).setMob(null);
        selectedTile.setMob(this);

        if (tempMob != null) region.getTile(this.x, this.y).setMob(tempMob);

        this.x = this.x + xMod;
        this.y = this.y + yMod;

        return true;
    }


}
