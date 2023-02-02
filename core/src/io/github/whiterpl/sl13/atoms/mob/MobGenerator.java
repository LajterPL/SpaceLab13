package io.github.whiterpl.sl13.atoms.mob;

import com.badlogic.gdx.Gdx;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.item.Item;
import io.github.whiterpl.sl13.atoms.item.Slot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MobGenerator {

    private static final List<Mob> angryMobs = new ArrayList<>();
    private static final Random randomGen = new Random();

    public static void initialize() {
        loadAngryMobs();
    }

    public static void loadAngryMobs() {
        angryMobs.clear();

        BufferedReader rd = new BufferedReader(new InputStreamReader(Gdx.files.internal("strings/angrymobs.txt").read()));
        rd.lines().forEach(s -> {
            Mob temp = new Mob(s.split(";")[0], s.split(";")[1], s.split(";")[2].charAt(0), s.split(";")[3]);

            temp.setActionDelay(Integer.parseInt(s.split(";")[4]));

            temp.setBaseDmg(Integer.parseInt(s.split(";")[5]));

            temp.maxHp = Short.parseShort(s.split(";")[6]);
            temp.currentHp = temp.maxHp;

            temp.addAction(0, Action.GenericAction.WALK_RANDOM);
            temp.addAction(100, Action.GenericAction.ATTACK, Status.HUMAN);

            angryMobs.add(temp);
        });
    }

    public static String getRandomHumanName() {
        BufferedReader rd = new BufferedReader(new InputStreamReader(Gdx.files.internal("strings/randomnames.txt").read()));
        ArrayList<String> names = new ArrayList<>();
        rd.lines().forEach(names::add);
        return names.get(randomGen.nextInt(names.size()));
    }

    public static short calculateMaxHp(short toughness) {
        return (short) (50 + toughness * 10);
    }

    public static short calculateMaxSp(short willpower) {
        return (short) (50 + willpower * 10);
    }

    public static int calculateMaxWeightLimit(short labour) {
        return 25 + labour * 5;
    }

    public static boolean attackMiss(short melee) {
        return randomGen.nextInt(melee/2 + 5) + melee/2 <= 3;
    }

    public static Mob getRandomAngryMob(int lvl) {
        Mob mob = new Mob(angryMobs.get(randomGen.nextInt(angryMobs.size())));

        for (int i = 0; i < 9; i++) {
            mob.skills[i] = (short) Math.max(randomGen.nextInt(lvl/3 + 1), 9);
        }

        mob.skills[Skill.MELEE.getIndex()] = (short) Math.max(randomGen.nextInt(8) + 1, 9);

        return mob;
    }
}
