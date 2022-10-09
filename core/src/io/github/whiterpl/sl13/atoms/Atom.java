package io.github.whiterpl.sl13.atoms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Atom {
    protected String name;
    protected String description;
    protected char symbol;
    protected String colorString;
    protected List<Status> statuses;

    public Atom(String name, String description, char symbol, String colorString, Status... statuses) {
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.colorString = colorString;
        this.statuses = new ArrayList<>();

        this.statuses.addAll(Arrays.asList(statuses));
    }

    public Atom(String name, String description, char symbol, String colorString) {
        this(name, description, symbol, colorString, (Status) null);
    }

    public Atom() {
        this("Unknown", "Unknown",'?', "FFFFFFFF");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getColorString() {
        return colorString;
    }

    public boolean hasStatus(Status status) {
        return statuses.contains(status);
    }

    public void addStatus(Status status) {
        if (!this.hasStatus(status)) {
            statuses.add(status);
        }
    }

    public void removeStatus(Status status) {
        if(this.hasStatus(status)) {
            statuses.remove(status);
        }
    }
}
