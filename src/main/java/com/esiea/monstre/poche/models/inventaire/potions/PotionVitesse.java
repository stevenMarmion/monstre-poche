package com.esiea.monstre.poche.models.inventaire.potions;

import com.esiea.monstre.poche.models.entites.Monstre;

public class PotionVitesse  extends Potion {
    private int pointsDeVitesse;
    
    public PotionVitesse(String nomObjet, int pointsDeVitesse) {
        super(nomObjet);
        this.pointsDeVitesse = pointsDeVitesse;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setVitesse(cible.getVitesse() + this.pointsDeVitesse);
    }
}
