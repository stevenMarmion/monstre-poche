package com.esiea.monstre.poche.inventaire.potions;

import com.esiea.monstre.poche.entites.Monstre;

public class PotionSante extends Potion {
    private int pointsDeSoin;

    public PotionSante(String nomObjet, int pointsDeSoin) {
        super(nomObjet);
        this.pointsDeSoin = pointsDeSoin;
    }

    @Override
    public void utiliserPotion(Monstre cible) {
        System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        if (cible.getPointsDeVie() + this.pointsDeSoin > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + this.pointsDeSoin);
        }
    }
}
