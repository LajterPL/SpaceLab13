package io.github.whiterpl.sl13.atoms;

public class Mob extends Atom {
    public Mob(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
    }

    public Mob(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
    }

    public Mob() {
    }
}
