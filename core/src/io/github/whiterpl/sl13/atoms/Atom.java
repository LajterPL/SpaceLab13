package io.github.whiterpl.sl13.atoms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Atom {
    protected String name;
    protected String description;
    protected char symbol;
    protected String colorString;
    protected Set<Status> statuses;

    public Atom(String name, String description, char symbol, String colorString, Status... statuses) {
        this.name = name;
        this.description = description;
        this.symbol = symbol;
        this.colorString = colorString;
        this.statuses = new HashSet<>();

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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
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

    public Set<Status> getStatuses() {
        return statuses;
    }


}
