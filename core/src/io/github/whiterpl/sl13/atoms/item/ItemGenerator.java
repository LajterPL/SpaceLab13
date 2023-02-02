package io.github.whiterpl.sl13.atoms.item;

import com.badlogic.gdx.Gdx;
import io.github.whiterpl.sl13.atoms.Status;
import io.github.whiterpl.sl13.atoms.mob.Action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemGenerator {

    private static final List<Item> meleeWeapons = new ArrayList<>();
    private static final Random randomGen = new Random();

    public static void initialize() {
        loadMeleeWeapons();
    }

    private static void loadMeleeWeapons() {

        meleeWeapons.clear();

        BufferedReader rd = new BufferedReader(new InputStreamReader(Gdx.files.internal("strings/melee.txt").read()));
        rd.lines().forEach(s -> {

            Item temp = new Item(s.split(";")[0], s.split(";")[1], s.split(";")[2].charAt(0), s.split(";")[3]);

            temp.addLegalSlot(Slot.BACKPACK);
            temp.addLegalSlot(Slot.LEFT_HAND);
            temp.addLegalSlot(Slot.RIGHT_HAND);

            temp.addUsageSlot(Slot.LEFT_HAND);
            temp.addUsageSlot(Slot.RIGHT_HAND);

            temp.setUsageAction( new Action(0,
                    mob -> !mob.hasStatus(Status.PARALYZED),
                    mob -> {
                int min = Integer.parseInt(s.split(";")[4]);
                int max = Integer.parseInt(s.split(";")[5]);
                mob.setWeaponMod(randomGen.nextInt(max - min) + min);
                mob.setAttackVerb(s.split(";")[6]);
            }));

            temp.setWeight(Integer.parseInt(s.split(";")[7]));

            meleeWeapons.add(temp);

        });
    }

    public static Item getRandomMelee() {
        return new Item(meleeWeapons.get(randomGen.nextInt(meleeWeapons.size())));
    }
}
