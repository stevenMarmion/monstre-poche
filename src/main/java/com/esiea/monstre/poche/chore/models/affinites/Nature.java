package com.esiea.monstre.poche.chore.models.affinites;

import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Monstre;

public class Nature extends Type {
    private static final double COEF_VINGTIEME = 0.05;

    public void appliqueCapaciteSpeciale(Monstre cible) {
        double recuperation = cible.getPointsDeVie() * COEF_VINGTIEME;
        if ((cible.getPointsDeVie() + recuperation) > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVie() + recuperation);
            CombatLogger.log(cible.getNomMonstre() + " récupère jusqu'à ses points de vie max !");
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + recuperation);
            CombatLogger.log(cible.getNomMonstre() + " récupère " + recuperation + " points de vie.");
        }
    }
}
