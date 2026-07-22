package com.nx.client.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return com.nx.client.util.Lang.category(name(), label);
    }
}
