package com.esiea.monstre.poche.configuration.enums;

public enum ModeJeu {

    JOUEUR_CONTRE_IA("1", "Joueur vs IA"),
    JOUEUR_CONTRE_JOUEUR("2", "Joueur vs Joueur"),
    JCJ_EN_LIGNE("3", "Joueur vs Joueur en ligne");

    private final String identifiant;
    private final String libelle;

    ModeJeu(String identifiant, String libelle) {
        this.identifiant = identifiant;
        this.libelle = libelle;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public static ModeJeu fromIdentifiant(String id) {
        for (ModeJeu mode : values()) {
            if (mode.identifiant.equals(id)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Identifiant inconnu de l'enum ModeJeu : " + id);
    }

    public static boolean existsByidentifiant(String id) {
        for (ModeJeu mode : values()) {
            if (mode.identifiant.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
