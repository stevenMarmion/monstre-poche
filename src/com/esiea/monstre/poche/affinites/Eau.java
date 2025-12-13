package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Eau extends Type {
    private double probabiliteInnondation;
    private double probabiliteFaireChuter;

    public Eau() {
        this.labelType = "Eau";
    }

    public double getProbabiliteInnondation() {
        return probabiliteInnondation;
    }

    public double getProbabiliteFaireChuter() {
        return probabiliteFaireChuter;
    }

    public void setProbabiliteInnondation(double probabiliteInnondation) {
        this.probabiliteInnondation = probabiliteInnondation;
    }

    public void setProbabiliteFaireChuter(double probabiliteFaireChuter) {
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }

    public boolean faitChuter(Monstre cible) {
        return false;
    }
}
