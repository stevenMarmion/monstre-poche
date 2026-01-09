package com.esiea.monstre.poche.models.items.potions;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

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
        int attaqueAvant = cible.getAttaque();
        CombatLogger.info("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setAttaque(cible.getAttaque() + this.pointsDeDegat);
        CombatLogger.info("-> Attaque de " + cible.getNomMonstre() + " augmente: " + attaqueAvant + " -> " + cible.getAttaque());
    }

    @Override
    public PotionDegat copyOf(){
        return new PotionDegat(super.nomObjet, this.pointsDeDegat);
    }
}
