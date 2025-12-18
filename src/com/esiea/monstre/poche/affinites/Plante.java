package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.Normal;

public class Plante extends Nature {
    private static final double CHANCE_SOIN = 0.2;

    public Plante() {
        this.labelType = "Plante";
    }

    // @Override
    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (CHANCE_SOIN < Math.random()) {
            cible.setStatut(new Normal());
        }
        super.appliqueCapaciteSpeciale(cible);
    }
}
