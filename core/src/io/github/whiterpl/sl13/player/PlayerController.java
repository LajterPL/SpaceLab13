package io.github.whiterpl.sl13.player;

import io.github.whiterpl.sl13.Game;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Mob;
import io.github.whiterpl.sl13.atoms.mob.MobGenerator;
import io.github.whiterpl.sl13.atoms.mob.Skill;
import io.github.whiterpl.sl13.atoms.region.Region;
import io.github.whiterpl.sl13.atoms.region.RegionGenerator;

public class PlayerController {

    public final Mob player;

    protected short characterLevel;
    protected int currentExp;

    protected int currentTurn;

    protected Region activeRegion;
    private int regionLevel;

    public PlayerController() {
        this.player = new Mob("Player", "This is you.", '@', "FFFFFFFF");
        this.player.addStatus(Status.HUMAN);
        this.player.setActionDelay(10);

        regionLevel = 0;
    }

    public PlayerController(String name, short[] skills) {
        this(
                name,
                MobGenerator.calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                MobGenerator.calculateMaxHp(skills[Skill.TOUGHNESS.getIndex()]),
                MobGenerator.calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                MobGenerator.calculateMaxSp(skills[Skill.WILLPOWER.getIndex()]),
                0,
                (short) 1,
                0,
                skills
        );
    }

    public PlayerController(String name, short maxHp, short currentHp, short maxSp, short currentSp, int credits, short characterLevel, int currentExp, short[] skills) {
        this();
        this.player.setName(name);
        this.player.setMaxHp(maxHp);
        this.player.setCurrentHp(currentHp);
        this.player.setMaxSp(maxSp);
        this.player.setCurrentSp(currentSp);
        this.player.setSkills(skills);
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

    public void setActiveRegion(Region activeRegion) {
        this.activeRegion = activeRegion;
    }

    //METHODS

    public void goUp() {
        if (regionLevel == 0) return;

        activeRegion.removeFromQueue(player);
        activeRegion.getTile(player.getPosition()).setMob(null);

        regionLevel--;
        activeRegion = Game.getLevels().get(regionLevel);

        activeRegion.addToQueue(player);

        activeRegion.advanceQueue();
        activeRegion.getTile(player.getPosition()).setMob(player);
    }

    public void goDown() {

        activeRegion.removeFromQueue(player);
        activeRegion.getTile(player.getPosition()).setMob(null);

        regionLevel++;

        if (regionLevel == Game.getLevels().size()) {
            Game.getLevels().add(RegionGenerator.generateRegion(player.getPosition(), regionLevel, player.getNextUpdateTurn()));
        }

        activeRegion = Game.getLevels().get(regionLevel);

        activeRegion.addToQueue(player);
        activeRegion.advanceQueue();
        activeRegion.getTile(player.getPosition()).setMob(player);

    }
}
