package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Normal;

public class Plante extends Nature {
    private static final double CHANCE_SOIN = 0.2;

    public Plante() {
        this.labelType = "Plante";
        this.fortContre = "Eau";
        this.faibleContre = "Feu";
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (CHANCE_SOIN < Math.random()) {
            cible.setStatut(new Normal());
            System.out.println(cible.getNomMonstre() + " est soigné et redevient normal !");
        }
        super.appliqueCapaciteSpeciale(cible);
    }
}
