package com.esiea.monstre.pochebis.models.affinites;

import com.esiea.monstre.pochebis.models.combats.CombatLogger;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.etats.Normal;

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
            CombatLogger.log(cible.getNomMonstre() + " est soigné et redevient normal !");
        }
        super.appliqueCapaciteSpeciale(cible);
    }
}
