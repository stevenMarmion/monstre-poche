package src.com.esiea.monstre.poche.actions;

import src.com.esiea.monstre.poche.affinites.Type;

public class Attaque {
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
}
