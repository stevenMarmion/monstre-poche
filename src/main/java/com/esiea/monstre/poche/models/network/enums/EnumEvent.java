package com.esiea.monstre.poche.models.network.enums;

public enum EnumEvent {
    INFO("INFO|"),
    ASK("ASK|"),
    END("END|"),
    ANSWER("ANSWER|"),

    PLAYER_DATA("PLAYER_DATA|"),
    MONSTER_UPDATE("MONSTER_UPDATE|"),
    ACTION("ACTION|");

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