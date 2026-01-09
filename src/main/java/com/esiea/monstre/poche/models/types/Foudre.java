package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.terrain.Paralyse;

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
            cible.setStatut(new Paralyse((int)(Math.random() * 6)));
            CombatLogger.info(cible.getNomMonstre() + " est désormais paralysé.");
        }
    }
}
