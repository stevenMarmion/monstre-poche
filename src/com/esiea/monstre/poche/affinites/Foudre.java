package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Foudre extends Type {
    private double chanceParalysie;

    public Foudre() {
        this.labelType = "Foudre";
    }

    public double getChanceParalysie() {
        return chanceParalysie;
    }

    public void setChanceParalysie(double chanceParalysie) {
        this.chanceParalysie = chanceParalysie;
    }

    public boolean paralyser(Monstre cible) {
        return false;
    }
}
