package com.esiea.monstre.poche.models.items.potions;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

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
        int vitesseAvant = cible.getVitesse();
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setVitesse(cible.getVitesse() + this.pointsDeVitesse);
        CombatLogger.log("  -> Vitesse de " + cible.getNomMonstre() + " augmente: " + vitesseAvant + " -> " + cible.getVitesse());
    }

    @Override
    public PotionVitesse copyOf(){
        return new PotionVitesse(super.nomObjet, this.pointsDeVitesse);
    }
}
