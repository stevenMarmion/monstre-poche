package com.esiea.monstre.poche.models.inventaire.potions;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

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
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setVitesse(cible.getVitesse() + this.pointsDeVitesse);
    }

    @Override
    public PotionVitesse copyOf(){
        return new PotionVitesse(super.nomObjet, this.pointsDeVitesse);
    }
}
