package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Feu extends Type {
    private double chanceBrulure;

    public Feu() {
        this.labelType = "Feu";
    }

    public double getChanceBrulure() {
        return chanceBrulure;
    }

    public void setChanceBrulure(double chanceBrulure) {
        this.chanceBrulure = chanceBrulure;
    }

    public boolean bruler(Monstre cible) {
        return false;
    }
}
