package com.esiea.monstre.poche.views.gui.config;

public enum FontConfig {
    SYSTEM("System"),
    ARIAL_BLACK("Arial Black"),
    ARIAL("Arial");

    private final String fontName;

    FontConfig(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public static FontConfig fromString(String fontName) {
        for (FontConfig fontConfig : FontConfig.values()) {
            if (fontConfig.getFontName().equalsIgnoreCase(fontName)) {
                return fontConfig;
            }
        }
        return SYSTEM;
    }
}