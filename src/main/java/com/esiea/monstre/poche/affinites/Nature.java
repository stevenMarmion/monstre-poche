package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;

public class Nature extends Type {
    private static final double COEF_VINGTIEME = 0.05;

    public void appliqueCapaciteSpeciale(Monstre cible) {
        double recuperation = cible.getPointsDeVie() * COEF_VINGTIEME;
        if ((cible.getPointsDeVie() + recuperation) > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVie() + recuperation);
            System.out.println(cible.getNomMonstre() + " récupère jusqu'à ses points de vie max !");
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + recuperation);
            System.out.println(cible.getNomMonstre() + " récupère " + recuperation + " points de vie.");
        }
    }
}
