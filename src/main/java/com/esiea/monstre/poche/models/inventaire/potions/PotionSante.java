package com.esiea.monstre.poche.models.inventaire.potions;

import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.combats.CombatLogger;

public class PotionSante extends Potion {
    private int pointsDeSoin;

    public PotionSante(String nomObjet, int pointsDeSoin) {
        super(nomObjet);
        this.pointsDeSoin = pointsDeSoin;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        if (cible.getPointsDeVie() + this.pointsDeSoin > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + this.pointsDeSoin);
        }
    }
}
