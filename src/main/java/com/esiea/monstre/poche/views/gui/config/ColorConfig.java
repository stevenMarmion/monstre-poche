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
    OBJET("#95A5A6");

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
