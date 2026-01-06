package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Innonde;

public class Eau extends Type {
    private static final int NB_TOURS_MAX_INNONDATION = 3;
    private static final int NB_TOURS_MIN_INNONDATION = 1;

    private double probabiliteInnondation;
    private double probabiliteFaireChuter;

    public Eau() {
        this(0.4, 0.3); // Valeurs par défaut
    }
    
    public Eau(double probabiliteInnondation, double probabiliteFaireChuter) {
        this.labelType = "Eau";
        this.fortContre = "Feu";
        this.faibleContre = "Foudre";
        this.probabiliteInnondation = probabiliteInnondation;
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }
    
    public double getProbabiliteInnondation() {
        return this.probabiliteInnondation;
    }
    
    public void setProbabiliteInnondation(double probabiliteInnondation) {
        this.probabiliteInnondation = probabiliteInnondation;
    }
    
    public double getProbabiliteFaireChuter() {
        return this.probabiliteFaireChuter;
    }
    
    public void setProbabiliteFaireChuter(double probabiliteFaireChuter) {
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }

    public void appliqueCapaciteSpeciale(Terrain terrain, Monstre monstreEau) {
        boolean innonde = Math.random() < this.probabiliteInnondation;
        if (innonde) {
            int nbTours = (int) (Math.random() * NB_TOURS_MAX_INNONDATION) + NB_TOURS_MIN_INNONDATION;
            terrain.setStatutTerrain(new Innonde(nbTours, this.probabiliteFaireChuter, monstreEau));
            CombatLogger.log(terrain.getNomTerrain() + " est désormais inondé pendant " + nbTours + " tours.");
        }
    }
    
    /**
     * Ancienne méthode pour compatibilité
     */
    public void appliqueCapaciteSpeciale(Terrain terrain) {
        boolean innonde = Math.random() < this.probabiliteInnondation;
        if (innonde) {
            int nbTours = (int) (Math.random() * NB_TOURS_MAX_INNONDATION) + NB_TOURS_MIN_INNONDATION;
            terrain.setStatutTerrain(new Innonde(nbTours, this.probabiliteFaireChuter, null));
            CombatLogger.log(terrain.getNomTerrain() + " est désormais inondé pendant " + nbTours + " tours.");
        }
    }
}
