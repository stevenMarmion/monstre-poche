package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Paralyse;

public class Foudre extends Type {
    private double chanceParalysie;
    private boolean derniereAttaqueAParalyse = false;

    public Foudre() {
        this(0.2); // 20% par défaut
    }
    
    public Foudre(double chanceParalysie) {
        this.labelType = "Foudre";
        this.fortContre = "Eau";
        this.faibleContre = "Terre";
        this.chanceParalysie = chanceParalysie;
    }
    
    public double getChanceParalysie() {
        return this.chanceParalysie;
    }
    
    public void setChanceParalysie(double chanceParalysie) {
        this.chanceParalysie = chanceParalysie;
    }
    
    /**
     * Retourne true si la dernière attaque a paralysé l'adversaire
     */
    public boolean aParalyse() {
        return this.derniereAttaqueAParalyse;
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < this.chanceParalysie) {
            cible.setStatut(new Paralyse());
            this.derniereAttaqueAParalyse = true;
            CombatLogger.log(cible.getNomMonstre() + " est désormais paralysé.");
        } else {
            this.derniereAttaqueAParalyse = false;
        }
    }
}
