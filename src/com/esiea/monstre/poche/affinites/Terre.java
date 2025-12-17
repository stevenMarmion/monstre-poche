package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.SousTerre;

public class Terre extends Type {
    private static final double CHANCE_FUITE = 0.05;

    public Terre() {
        this.labelType = "Terre";
        this.fortContre = new Foudre();
        this.faibleContre = new Nature();
    }

    public boolean fuit(Monstre cible) {
        boolean fuite = Math.random() < CHANCE_FUITE;
        if (fuite) {
            cible.setStatut(new SousTerre());
            cible.getStatut().appliquerEffets(cible);
            return true;
        } else {
            return false;
        }
    }
}
