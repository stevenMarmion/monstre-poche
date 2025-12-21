package com.esiea.monstre.poche.inventaire.potions;

import com.esiea.monstre.poche.entites.Monstre;

public class PotionVitesse  extends Potion {
    /**
     * Value en pourcentage d'augmentation de la vitesse de base d'un monstre
     */
    private final int pointsDeVitesse;
    
    public PotionVitesse(String nomObjet, int pointsDeVitesse) {
        super(nomObjet);
        this.pointsDeVitesse = pointsDeVitesse;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setVitesse(cible.getVitesse() + this.pointsDeVitesse);
    }

    @Override
    public PotionVitesse copyOf(){
        return new PotionVitesse(super.nomObjet, this.pointsDeVitesse);
    }
}
