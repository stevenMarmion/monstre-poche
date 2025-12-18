package com.esiea.monstre.poche.inventaire.potions;

import com.esiea.monstre.poche.entites.Monstre;

public class PotionDegat extends Potion {
    private int pointsDeDegat;

    public PotionDegat(String nomObjet, int pointsDeDegat) {
        super(nomObjet);
        this.pointsDeDegat = pointsDeDegat;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setAttaque(cible.getAttaque() + this.pointsDeDegat);
    }
}
