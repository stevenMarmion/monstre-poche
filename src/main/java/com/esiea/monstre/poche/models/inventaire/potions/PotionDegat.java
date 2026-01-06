package com.esiea.monstre.poche.models.inventaire.potions;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class PotionDegat extends Potion {

    /**
     * Value en pourcentage d'augmentation de l'attaque de base d'un monstre
     */
    private final int pointsDeDegat;

    public PotionDegat(String nomObjet, int pointsDeDegat) {
        super(nomObjet);
        this.pointsDeDegat = pointsDeDegat;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setAttaque(cible.getAttaque() + this.pointsDeDegat);
    }

    @Override
    public PotionDegat copyOf(){
        return new PotionDegat(super.nomObjet, this.pointsDeDegat);
    }
}
