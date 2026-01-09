package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Brule;

public class Feu extends Type {
    private double chanceBrulure;

    public Feu() {
        this.labelType = "Feu";
        this.fortContre = "Nature";
        this.faibleContre = "Eau";
        this.chanceBrulure = Math.random();
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < this.chanceBrulure) {
            cible.setStatut(new Brule());
            CombatLogger.info(cible.getNomMonstre() + " est désormais brûlé.");
        }
    }
}
