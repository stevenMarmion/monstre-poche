package com.esiea.monstre.poche.inventaire.potions;

import com.esiea.monstre.poche.entites.Monstre;

public class PotionSante extends Potion {

    /**
     * Value en pourcentage de restoration des points de vie d'un monstre
     */
    private final int pointsDeSoin;

    public PotionSante(String nomObjet, int pointsDeSoin) {
        super(nomObjet);
        this.pointsDeSoin = pointsDeSoin;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        if (cible.getPointsDeVie() + this.pointsDeSoin > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + this.pointsDeSoin);
        }
    }

    @Override
    public PotionSante copyOf(){
        return new PotionSante(super.nomObjet, this.pointsDeSoin);
    }
}
