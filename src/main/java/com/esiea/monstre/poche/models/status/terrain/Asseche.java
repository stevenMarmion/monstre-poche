package com.esiea.monstre.poche.models.status.terrain;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Terrain;

public class Asseche extends StatutTerrain {

    public Asseche() {
        this.labelStatut = "Asseche";
    }

    public void appliquerEffets(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
        CombatLogger.info(terrain.getNomTerrain() + " est maintenant asséché.");
    }
}
