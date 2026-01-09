package com.esiea.monstre.poche.views.gui.config;

public enum ColorConfig {
    FEU("#F08030"),
    EAU("#6890F0"),
    PLANTE("#78C850"),
    FOUDRE("#F8D030"),
    TERRE("#E0C068"),
    NORMAL("#A8A878"),
    INSECTE("#A8B820"),
    NATURE("#228B22"),

    POTION("#FF6B9D"),
    MEDICAMENT("#4ECDC4"),
    OBJET("#95A5A6"),

    NO_PP_BG_COLOR("#444"),
    NO_PP_BORDER_COLOR("#333"),

    HP_COLOR_HIGH("#48d048"),
    HP_COLOR_MEDIUM("#f8c800"),
    HP_COLOR_LOW("#f85858"),

    ACTION_ATTACK_COLOR("#e85858"),
    ACTION_ATTACK_DARK("#c84848"),
    ACTION_ITEM_COLOR("#58b858"),
    ACTION_ITEM_DARK("#48a848"),
    ACTION_SWITCH_COLOR("#5898e8"),
    ACTION_SWITCH_DARK("#4888d8"),
    ACTION_FLEE_COLOR("#888898"),
    ACTION_FLEE_DARK("#707080"),

    BARE_HANDS_COLOR("#8B4513"),
    BARE_HANDS_COLOR_HOVER("#A0522D"),
    BARE_HANDS_BORDER("#654321"),

    ITEM_MEDICINE_COLOR("#e88848"),
    ITEM_POTION_DEGATS_COLOR("#e85858"),
    ITEM_POTION_VITESSE_COLOR("#5898e8"),
    ITEM_DEFAULT_COLOR("#58b858");

    private final String colorCode;

    ColorConfig(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public static ColorConfig fromString(String colorName) {
        for (ColorConfig colorConfig : ColorConfig.values()) {
            if (colorConfig.name().equalsIgnoreCase(colorName)) {
                return colorConfig;
            }
        }
        return NORMAL;
    }
}
