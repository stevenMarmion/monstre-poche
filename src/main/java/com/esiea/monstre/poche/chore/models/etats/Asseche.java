package com.esiea.monstre.poche.chore.models.etats;

import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Terrain;

public class Asseche extends StatutTerrain {

    public Asseche() {
        this.labelStatut = "Asseche";
    }

    public void appliquerEffets(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
        CombatLogger.logEffetTerrain(terrain.getNomTerrain() + " est maintenant asséché.");
    }
}
