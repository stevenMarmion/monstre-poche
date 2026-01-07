package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

public class Nature extends Type {
    private static final double COEF_VINGTIEME = 0.05;

    public Nature() {
        this.labelType = null;
        this.fortContre = null;
        this.faibleContre = null;
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        double recuperation = cible.getPointsDeVieMax() * COEF_VINGTIEME;
        double nouveauxPV = cible.getPointsDeVie() + recuperation;
        
        if (nouveauxPV > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
            CombatLogger.log(cible.getNomMonstre() + " récupère ses points de vie au maximum grâce au terrain inondé !");
        } else {
            cible.setPointsDeVie(nouveauxPV);
            CombatLogger.log(cible.getNomMonstre() + " récupère " + (int)recuperation + " PV grâce au terrain inondé.");
        }
    }
}
