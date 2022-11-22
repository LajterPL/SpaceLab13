package io.github.whiterpl.sl13.atoms.structure;

import io.github.whiterpl.sl13.atoms.Atom;
import io.github.whiterpl.sl13.atoms.Status;

public class Structure extends Atom {
    public Structure(String name, String description, char symbol, String colorString, Status... statuses) {
        super(name, description, symbol, colorString, statuses);
    }

    public Structure(String name, String description, char symbol, String colorString) {
        super(name, description, symbol, colorString);
    }

    public Structure() {
    }
}