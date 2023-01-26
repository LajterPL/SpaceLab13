package io.github.whiterpl.sl13.atoms.mob;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MobGenerator {

    private static final Random randomGen = new Random();

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
}
