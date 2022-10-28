package io.github.whiterpl.sl13.player;

import io.github.whiterpl.sl13.atoms.Item;
import io.github.whiterpl.sl13.atoms.Mob;
import io.github.whiterpl.sl13.atoms.Region;

import java.util.ArrayList;
import java.util.List;

public class PlayerController {
    protected String name;

    protected short maxHp;
    protected short currentHp;

    protected short maxSp;
    protected short currentSp;

    protected int credits;

    protected short characterLevel;
    protected int currentExp;

    protected short[] skills;

    protected List<Item> equipment;

    public final Mob player;

    int x;
    int y;

    public PlayerController() {
        this.player = new Mob("Player", "This is you.", '@', "FFFFFFFF");
        skills = new short[9];
    }

    public PlayerController(String name, short[] skills) {
        this(
                name,
                calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                0,
                (short) 1,
                0,
                skills,
                new ArrayList<>()
        );
    }

    public PlayerController(String name, short maxHp, short currentHp, short maxSp, short currentSp, int credits, short characterLevel, int currentExp, short[] skills, List<Item> equipment) {
        this();
        this.name = name;
        this.player.setName(name);
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.maxSp = maxSp;
        this.currentSp = currentSp;
        this.credits = credits;
        this.characterLevel = characterLevel;
        this.currentExp = currentExp;
        this.skills = skills;
        this.equipment = equipment;
    }

    public String getName() {
        return name;
    }

    public short getMaxHp() {
        return maxHp;
    }

    public short getCurrentHp() {
        return currentHp;
    }

    public short getMaxSp() {
        return maxSp;
    }

    public short getCurrentSp() {
        return currentSp;
    }

    public int getCredits() {
        return credits;
    }

    public short getCharacterLevel() {
        return characterLevel;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public short[] getSkills() {
        return skills;
    }

    public List<Item> getEquipment() {
        return equipment;
    }

    public Mob getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void placePlayer(Region region, int x, int y) {

    }

    public static short calculateMaxHp(short toughness) {
        return (short) (50 + toughness * 10);
    }

    public static short calculateMaxSp(short willpower) {
        return (short) (50 + willpower * 10);
    }


}
