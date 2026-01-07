package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Paralyse;

public class Foudre extends Type {
    private double chanceParalysie;

    public Foudre() {
        this(0.2);
    }
    
    public Foudre(double chanceParalysie) {
        this.labelType = "Foudre";
        this.fortContre = "Eau";
        this.faibleContre = "Terre";
        this.chanceParalysie = chanceParalysie;
    }
    
    public void setChanceParalysie(double chanceParalysie) {
        this.chanceParalysie = chanceParalysie;
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < this.chanceParalysie) {
            cible.setStatut(new Paralyse());
            CombatLogger.log(cible.getNomMonstre() + " est désormais paralysé.");
        }
    }
}
