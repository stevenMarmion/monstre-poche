package com.esiea.monstre.poche.models.core;

import java.io.Serializable;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.types.Type;

public class Attaque implements Serializable {
    protected static final long serialVersionUID = 1L;
    private String nomAttaque;
    private int nbUtilisations;
    private int puissanceAttaque;
    private double probabiliteEchec;
    private Type typeAttaque;

    public Attaque(String nomAttaque, int nbUtilisations, int puissanceAttaque, double probabiliteEchec, Type typeAttaque) {
        this.nomAttaque = nomAttaque;
        this.nbUtilisations = nbUtilisations;
        this.puissanceAttaque = puissanceAttaque;
        this.probabiliteEchec = probabiliteEchec;
        this.typeAttaque = typeAttaque;
    }

    public String getNomAttaque() {
        return this.nomAttaque;
    }

    public int getNbUtilisations() {
        return this.nbUtilisations;
    }

    public int getPuissanceAttaque() {
        return this.puissanceAttaque;
    }

    public double getProbabiliteEchec() {
        return this.probabiliteEchec;
    }

    public Type getTypeAttaque() {
        return this.typeAttaque;
    }

    public void setNomAttaque(String nomAttaque) {
        this.nomAttaque = nomAttaque;
    }

    public void setNbUtilisations(int nbUtilisations) {
        this.nbUtilisations = nbUtilisations;
    }

    public void setPuissanceAttaque(int puissanceAttaque) {
        this.puissanceAttaque = puissanceAttaque;
    }

    public void setProbabiliteEchec(double probabiliteEchec) {
        this.probabiliteEchec = probabiliteEchec;
    }

    public void setTypeAttaque(Type typeAttaque) {
        this.typeAttaque = typeAttaque;
    }

    public double calculeDegatsAttaque(Monstre monstreAttaquant, Monstre cible) {
        int numerateur = 11 * monstreAttaquant.getAttaque() * this.getPuissanceAttaque();
        int denominateur = 25 * cible.getDefense();
        double coeff = 0.85 + (1.0 - 0.85) * Math.random();
        double avantage = 1;
        
        String typeAttaque = this.getTypeAttaque().getLabelType();
        String typeCible = cible.getTypeMonstre().getLabelType();
        
        if (this.getTypeAttaque().estFaibleContre() != null && this.getTypeAttaque().estFaibleContre().equals(typeCible)) {
            avantage = 0.5;
            CombatLogger.log("  [TYPE] " + typeAttaque + " est faible contre " + typeCible + " ! Dégâts réduits de moitié.");
        } else if (this.getTypeAttaque().estFortContre() != null && this.getTypeAttaque().estFortContre().equals(typeCible)) {
            avantage = 2;
            CombatLogger.log("  [TYPE] " + typeAttaque + " est fort contre " + typeCible + " ! Dégâts doublés.");
        }
        return ((numerateur/denominateur) + 2) * avantage * coeff;
    }

    /**
     * Renvoie une copie de l'objet Attaque
     */
    public Attaque copyOf(){
        return new Attaque(
                this.getNomAttaque(),
                this.getNbUtilisations(),
                this.getPuissanceAttaque(),
                this.getProbabiliteEchec(),
                this.getTypeAttaque()
        );
    }

    @Override
    public String toString() {
        return "Attaque (" +
                "nomAttaque='" + nomAttaque + '\'' +
                ", nbUtilisations=" + nbUtilisations +
                ", puissanceAttaque=" + puissanceAttaque +
                ')';
    }
}
