package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Insecte extends Nature {
    private double chanceEmpoisonner;

    public Insecte() {
        this.labelType = "Insecte";
    }

    public double getChanceEmpoisonner() {
        return chanceEmpoisonner;
    }

    public void setChanceEmpoisonner(double chanceEmpoisonner) {
        this.chanceEmpoisonner = chanceEmpoisonner;
    }

    public boolean empoisonner(Monstre cible) {
        return false;
    }
    
}
