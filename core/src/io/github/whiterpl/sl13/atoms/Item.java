package io.github.whiterpl.sl13.atoms;

public class Item extends Atom {
    public Item(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
    }

    public Item(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
    }

    public Item() {
    }
}
