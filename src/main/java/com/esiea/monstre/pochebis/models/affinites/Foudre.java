package com.esiea.monstre.pochebis.models.affinites;

import com.esiea.monstre.pochebis.models.combats.CombatLogger;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.etats.Paralyse;

public class Foudre extends Type {
    private static final double CHANCE_PARALYSIE = 0; // à changer, jsp c'est quoi les chances

    public Foudre() {
        this.labelType = "Foudre";
        this.fortContre = "Eau";
        this.faibleContre = "Terre";
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < CHANCE_PARALYSIE) {
            cible.setStatut(new Paralyse());
            CombatLogger.log(cible.getNomMonstre() + " est désormais paralysé.");
        }
    }
}
