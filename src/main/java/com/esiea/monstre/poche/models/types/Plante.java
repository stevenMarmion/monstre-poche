package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.monster.Normal;

public class Plante extends Nature {
    private static final double CHANCE_SOIN = 0.2;

    public Plante() {
        this.labelType = "Plante";
        this.fortContre = "Eau";
        this.faibleContre = "Feu";
    }

    public void appliqueCapaciteSpeciale(Monstre cible, Terrain terrain) {
        if (Math.random() < CHANCE_SOIN) {
            cible.setStatut(new Normal());
            CombatLogger.info(cible.getNomMonstre() + " est soigné de ses altérations et redevient normal !");
        }
        super.appliqueCapaciteSpeciale(cible, terrain);
    }
}
