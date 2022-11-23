package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.atoms.*;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;

import java.util.*;
import java.util.stream.Stream;

public class Mob extends Atom {

    protected int detectionRange;

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

    private ActionManager actionManager;


    public Mob(String name, String description, char symbol, String colorString, int detectionRange, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
        this.equipment = new ArrayList<>();
        this.skills = new short[9];
        this.detectionRange = detectionRange;
        this.actionManager = new ActionManager();
    }

    public Mob(String name, String description, char symbol, String colorString, int detectionRange, int actionDelay) {
        super(name, description, symbol, colorString);
        this.equipment = new ArrayList<>();
        this.skills = new short[9];
        this.detectionRange = detectionRange;
        this.actionManager = new ActionManager();
        this.actionDelay = actionDelay;
    }

    public Mob(String name, String description, char symbol, String colorString) {
        this(name, description, symbol, colorString, 20, 10);
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

    public void setActionDelay(int actionDelay) {
        this.actionDelay = actionDelay;
    }

    public int getNextUpdateTurn() {
        return nextUpdateTurn;
    }

    public void setNextUpdateTurn(int nextUpdateTurn) {
        this.nextUpdateTurn = nextUpdateTurn;
    }

    public int getDetectionRange() {
        return detectionRange;
    }

    public void setDetectionRange(int detectionRange) {
        this.detectionRange = detectionRange;
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
        if (selectedTile.getMob() != null) {
            tempMob = selectedTile.getMob();
            selectedTile.setMob(null);
            tempMob.setX(this.x);
            tempMob.setY(this.y);
        }

        region.getTile(this.x, this.y).setMob(tempMob);
        selectedTile.setMob(this);

        this.x = this.x + xMod;
        this.y = this.y + yMod;

        return true;
    }

    public void act(Region region) {
        Stream<Action> possibleActions = actionManager.getActions(this, region);

        //TODO: Filtrowanie akcji, które są możliwe i dodawanie/odejmowanie priorytetu zależnie od tego, czy coś przeszkadza
        possibleActions
                .max(Comparator.comparingInt(Action::getPriority))
                .ifPresentOrElse(
                        action -> {
                            switch (action.getActionResponse()) {
                                case ATTACK:

                                case WALK:
                                    move(region, Direction.SOUTH);
                                    //TODO: Pathfinding
                                case LOOT:
                            }
                        },
                        () -> {

                            move(region, Direction.NORTH);
                        }        );
    }

    public void addDelay() {
        this.nextUpdateTurn += actionDelay;
    }
}
