package io.github.whiterpl.sl13.atoms.mob;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.*;
import io.github.whiterpl.sl13.atoms.item.Equipment;
import io.github.whiterpl.sl13.atoms.item.Slot;
import io.github.whiterpl.sl13.atoms.region.Position;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.Tile;
import io.github.whiterpl.sl13.gui.StageSwapper;
import io.github.whiterpl.sl13.player.PlayerController;

import java.util.*;

public class Mob extends Atom {

    protected int detectionRange;

    protected short maxHp;
    protected short currentHp;

    protected short maxSp;
    protected short currentSp;

    protected short[] skills;

    private final Equipment equipment;

    protected int credits;

    protected Position position;

    protected int actionDelay;
    protected int nextUpdateTurn;

    private final ActionManager actionManager;

    private int baseDmg;

    private int weaponMod;
    private int armorMod;

    private String attackVerb;


    public Mob(String name, String description, char symbol, String colorString, int detectionRange, int actionDelay, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
        this.equipment = new Equipment();
        this.skills = new short[9];
        this.detectionRange = detectionRange;
        this.actionDelay = actionDelay;
        this.actionManager = new ActionManager();
        this.baseDmg = 1;
        this.weaponMod = 0;
        this.armorMod = 0;
        this.attackVerb = "attacks";
    }

    public Mob(String name, String description, char symbol, String colorString, int detectionRange, int actionDelay) {
        super(name, description, symbol, colorString);
        this.equipment = new Equipment();
        this.skills = new short[9];
        this.detectionRange = detectionRange;
        this.actionManager = new ActionManager();
        this.actionDelay = actionDelay;
        this.position = new Position();
        this.baseDmg = 1;
        this.weaponMod = 0;
        this.armorMod = 0;
        this.attackVerb = "attacks";
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

    public Equipment getEquipment() {
        return equipment;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Position getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public void setX(int x) {
        this.position.x = x;
    }

    public int getY() {
        return position.y;
    }

    public void setY(int y) {
        this.position.y = y;
    }

    public void setWeaponMod(int weaponMod) {
        this.weaponMod = weaponMod;
    }

    public void setArmorMod(int armorMod) {
        this.armorMod = armorMod;
    }

    public void setAttackVerb(String attackVerb) {
        this.attackVerb = attackVerb;
    }

    public void addAction (Action action) {
        actionManager.addAction(action);
    }

    public void addAction(int priority, Action.GenericAction genericAction) {
        actionManager.addAction(priority, genericAction);
    }

    public void addAction(int priority, Action.GenericAction genericAction, Status status) {
        actionManager.addAction(priority, genericAction, status);
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

    public boolean placeMob(Region region, int x, int y) {
        Tile selectedTile = region.getTile(x, y);

        if (selectedTile.hasStatus(Status.BLOCK_PASSING)) return false;

        if (selectedTile.getMob() != null) return false;

        this.setX(x);
        this.setY(y);
        selectedTile.setMob(this);
        region.addToQueue(this);

        return true;
    }

    public boolean move(Region region, Direction direction) {
        int xMod, yMod;

        xMod = Direction.getXMod(direction);
        yMod = Direction.getYMod(direction);

        if (!canInteract(xMod, yMod)) return false;

        Tile selectedTile = region.getTile(this.position.x + xMod, this.position.y + yMod);
        if (selectedTile.hasStatus(Status.BLOCK_PASSING)) return false;

        Mob tempMob = null;
        if (selectedTile.getMob() != null) {
            tempMob = selectedTile.getMob();
            selectedTile.setMob(null);
            tempMob.setX(this.getX());
            tempMob.setY(this.getY());
        }

        region.getTile(this.getX(), this.getY()).setMob(tempMob);
        selectedTile.setMob(this);

        this.setX(this.getX() + xMod);
        this.setY(this.getY() + yMod);

        return true;
    }

    public boolean attackMelee(Region region, Direction direction) {

        int xMod, yMod;

        xMod = direction.getXMod();
        yMod = direction.getYMod();

        if (!canInteract(xMod, yMod)) return false;

        Tile selectedTile = region.getTile(this.getX() + xMod, this.getY() + yMod);

        if (selectedTile.getMob() != null) {

            attackVerb = "attacks"; //Jeśli nie ma broni, używa domyślnego attackVerba
            weaponMod = 0;          //I zeruje weaponMod

            equipment.getWornItems().keySet().stream()
                    .filter(slot -> slot == Slot.LEFT_HAND || slot == Slot.RIGHT_HAND)
                    .filter(slot -> equipment.getWornItems().get(slot) != null)
                    .forEach(slot -> {
                        equipment.getWornItems().get(slot).getUsageAction().execute(this);
                    });

            if (Game.getPlayerController().getPlayer().sees(this.getX() + xMod, this.getY() + yMod, region)) {
                Game.getInfoPane().appendMessage(String.format("[#FF0000]%s %s %s[]", this.name, this.attackVerb, selectedTile.getMob().name));
            }

            selectedTile.getMob().getAttacked(region, baseDmg + weaponMod);

            return true;
        } else if(selectedTile.getStructure() != null) {

            return true;
        }

        return false;
    }

    public boolean getAttacked(Region region, int baseDmg) {
        //TODO: Zmniejszanie obrażeń na podstawie armora

        armorMod = 0;

        equipment.getWornItems().keySet().stream()
                .filter(slot -> slot != Slot.LEFT_HAND && slot != Slot.RIGHT_HAND)
                .filter(slot -> equipment.getWornItems().get(slot) != null)
                .forEach(slot -> {
                    equipment.getWornItems().get(slot).getUsageAction().execute(this); //Aktualizuje armorMod przed walką
                });

        this.currentHp += Math.min(0, armorMod - baseDmg);

        if (this.currentHp <= 0) {
            Game.getInfoPane().appendMessage(String.format("[#a52100FF]%s dies[]", this.name));

            Tile deathTile = Game.getPlayerController().getActiveRegion().getTile(this.getX(), this.getY());

            while (this.equipment.getAllItems().size() > 0) {
                this.equipment.dropItem(deathTile, this.equipment.getAllItems().get(0));
            }

            deathTile.setMob(null);

            region.removeFromQueue(this);

            if (this.equals(Game.getPlayerController().getPlayer())) {
                Game.getStageSwapper().changeStage(StageSwapper.State.MAIN_MENU);
            }

        }

        return true;
    }

    public void act(PlayerController playerController) {
        List<Action> possibleActions = actionManager.getPossibleActions(this);

        possibleActions.stream()
                .max((a, b) -> -Integer.max(a.getPriority(), b.getPriority()))
                .ifPresentOrElse(a -> a.execute(this),
                        () -> {
                    this.move(playerController.getActiveRegion(), Direction.NORTH);
                        });
    }

    public void addDelay() {
        this.nextUpdateTurn += actionDelay;
    }

    private boolean canInteract(int xMod, int yMod) {
        if (yMod == 1 && this.getY() == Region.HEIGHT) return false;
        if (yMod == -1 && this.getY() == 0) return false;

        if (xMod == 1 && this.getX() == Region.WIDTH) return false;
        if (xMod == -1 && this.getX() == 0) return false;

        return true;
    }

    public boolean canInteract(Direction direction) {
        return canInteract(direction.getXMod(), direction.getYMod());
    }

    public boolean sees(int x, int y, Region region) {

        if (Math.pow((double) x - this.getX(), 2) + Math.pow((double) y - this.getY(), 2) > Math.pow(this.detectionRange, 2)) return false;

        //TODO: Blokowanie wizji przez ściany

        return true;
    }

    public boolean sees(Status status, Region region) {
        for (int x = Math.max(0, this.getX() - this.detectionRange); x < Math.min(Region.WIDTH, this.getX() + this.detectionRange); x++) {
            for (int y = Math.max(0, this.getY() - this.detectionRange); y < Math.min(Region.HEIGHT, this.getY() + this.detectionRange); y++) {
                if (this.sees(x, y, region)) {
                    if (region.getTile(x, y).hasStatus(status)) return true;
                }
            }
        }

        return false;
    }



}
