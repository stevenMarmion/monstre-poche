package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Innonde;

public class Eau extends Type {
    private static final int NB_TOURS_MAX_INNONDATION = 3;
    private static final int NB_TOURS_MIN_INNONDATION = 1;

    private double probabiliteInnondation;
    // private double probabiliteFaireChuter;

    public Eau() {
        this.labelType = "Eau";
        this.fortContre = "Feu";
        this.faibleContre = "Foudre";
        this.probabiliteInnondation = 0.4; // A DEFINIR, PAS DANS LE CDC
        // this.probabiliteFaireChuter = 0.3; //  A DEFINIR, PAS DANS LE CDC
    }

    public void appliqueCapaciteSpeciale(Terrain terrain) {
        boolean innonde = Math.random() < this.probabiliteInnondation;
        if (innonde) {
            terrain.setStatutTerrain(new Innonde((int) (Math.random() * NB_TOURS_MAX_INNONDATION) + NB_TOURS_MIN_INNONDATION));
            CombatLogger.log(terrain.getNomTerrain() + " est désormais inondé pendant " + terrain.getStatutTerrain().getNbToursEffet() + " tours.");
        }
    }
}
