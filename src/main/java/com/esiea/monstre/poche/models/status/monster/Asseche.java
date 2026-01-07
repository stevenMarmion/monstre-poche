package com.esiea.monstre.poche.models.status.monster;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.terrain.StatutTerrain;

public class Asseche extends StatutTerrain {

    public Asseche() {
        this.labelStatut = "Asseche";
    }

    public void appliquerEffets(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
        CombatLogger.log(terrain.getNomTerrain() + " est maintenant asséché.");
    }
}
