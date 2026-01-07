package com.esiea.monstre.poche.models.online.enums;

public enum EnumEvent {
    INFO("INFO|"),
    ASK("ASK|"),
    END("END|"),
    ANSWER("ANSWER|");

    private final String label;

    EnumEvent(String label) {
        this.label = label;
    }

    public int getLabelLength() {
        return label.length();
    }

    @Override
    public String toString() {
        return label;
    }
}