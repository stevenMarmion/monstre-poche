package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Normal;

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
