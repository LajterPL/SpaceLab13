package io.github.whiterpl.sl13.player;

import io.github.whiterpl.sl13.atoms.Item;
import io.github.whiterpl.sl13.atoms.Mob;
import io.github.whiterpl.sl13.atoms.Region;
import io.github.whiterpl.sl13.atoms.RegionGenerator;

import java.util.ArrayList;
import java.util.List;

public class PlayerController {

    public final Mob player;

    protected short characterLevel;
    protected int currentExp;

    protected int currentTurn;

    protected Region activeRegion;

    public PlayerController() {
        this.player = new Mob("Player", "This is you.", '@', "FFFFFFFF");
        this.activeRegion = RegionGenerator.getTestingRegion();
        player.placeMob(activeRegion, 5, 5);
    }

    public PlayerController(String name, short[] skills) {
        this(
                name,
                Mob.calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                Mob.calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                Mob.calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                Mob.calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                0,
                (short) 1,
                0,
                skills,
                new ArrayList<>()
        );
    }

    public PlayerController(String name, short maxHp, short currentHp, short maxSp, short currentSp, int credits, short characterLevel, int currentExp, short[] skills, List<Item> equipment) {
        this();
        this.player.setName(name);
        this.player.setMaxHp(maxHp);
        this.player.setCurrentHp(currentHp);
        this.player.setMaxSp(maxSp);
        this.player.setCurrentSp(currentSp);
        this.player.setSkills(skills);
        this.player.setEquipment(equipment);
        this.player.setCredits(credits);

        this.characterLevel = characterLevel;
        this.currentExp = currentExp;

    }

    public short getCharacterLevel() {
        return characterLevel;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public Mob getPlayer() {
        return player;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Region getActiveRegion() {
        return activeRegion;
    }
}
