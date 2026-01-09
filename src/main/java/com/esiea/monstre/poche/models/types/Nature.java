package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;

public class Nature extends Type {
    private static final double COEF_VINGTIEME = 0.05;

    public Nature() {
        this.labelType = null;
        this.fortContre = null;
        this.faibleContre = null;
    }

    public void appliqueCapaciteSpeciale(Monstre cible, Terrain terrain) {
        if (terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
            double recuperation = cible.getPointsDeVieMax() * COEF_VINGTIEME;
            double nouveauxPV = cible.getPointsDeVie() + recuperation;
            
            if (nouveauxPV > cible.getPointsDeVieMax()) {
                cible.setPointsDeVie(cible.getPointsDeVieMax());
                CombatLogger.info(cible.getNomMonstre() + " récupère ses points de vie au maximum !");
            } else {
                cible.setPointsDeVie(nouveauxPV);
                CombatLogger.info(cible.getNomMonstre() + " récupère " + (int)recuperation + " PV !");
            }
        }
    }
}
