package com.esiea.monstre.poche.chore.models.inventaire.potions;

import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Monstre;

public class PotionVitesse  extends Potion {
    private int pointsDeVitesse;
    
    public PotionVitesse(String nomObjet, int pointsDeVitesse) {
        super(nomObjet);
        this.pointsDeVitesse = pointsDeVitesse;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setVitesse(cible.getVitesse() + this.pointsDeVitesse);
    }
}
