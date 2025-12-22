package com.esiea.monstre.poche.models.inventaire.potions;

import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.combats.CombatLogger;

public class PotionDegat extends Potion {
    private int pointsDeDegat;

    public PotionDegat(String nomObjet, int pointsDeDegat) {
        super(nomObjet);
        this.pointsDeDegat = pointsDeDegat;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        cible.setAttaque(cible.getAttaque() + this.pointsDeDegat);
    }
}
