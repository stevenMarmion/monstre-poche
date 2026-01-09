package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.terrain.Innonde;

public class Eau extends Type {
    private static final int NB_TOURS_MAX_INNONDATION = 3;
    private static final int NB_TOURS_MIN_INNONDATION = 1;

    private double probabiliteInnondation;
    private double probabiliteFaireChuter;

    public Eau() {
        this(null, null);
    }
    
    public Eau(Double probabiliteInnondation, Double probabiliteFaireChuter) {
        this.labelType = "Eau";
        this.fortContre = "Feu";
        this.faibleContre = "Foudre";
        this.probabiliteInnondation = probabiliteInnondation;
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }
    
    public void setProbabiliteInnondation(Double probabiliteInnondation) {
        this.probabiliteInnondation = probabiliteInnondation;
    }
    
    public void setProbabiliteFaireChuter(Double probabiliteFaireChuter) {
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }

    public void appliqueCapaciteSpeciale(Terrain terrain, Monstre monstreEau) {
        boolean innonde = Math.random() < this.probabiliteInnondation;
        if (innonde) {
            int nbTours = (int) (Math.random() * NB_TOURS_MAX_INNONDATION) + NB_TOURS_MIN_INNONDATION;
            terrain.setStatutTerrain(new Innonde(nbTours, this.probabiliteFaireChuter, monstreEau));
            CombatLogger.info(terrain.getNomTerrain() + " est désormais inondé pendant " + nbTours + " tours.");
        }
    }
}
