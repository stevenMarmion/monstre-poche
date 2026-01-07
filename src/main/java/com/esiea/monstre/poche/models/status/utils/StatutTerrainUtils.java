package com.esiea.monstre.poche.models.status.utils;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.monster.Asseche;
import com.esiea.monstre.poche.models.status.terrain.Innonde;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant, double degatsAffliges) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                Innonde innonde = (Innonde) terrain.getStatutTerrain();
                if (!monstreAttaquant.getTypeMonstre().getLabelType().equals("Eau")) {
                    innonde.appliquerEffetsTerrain(monstreAttaquant, degatsAffliges);   
                } else {
                    CombatLogger.log(monstreAttaquant.getNomMonstre() + " est de type Eau et n'est pas gêné par l'inondation.");
                    innonde.decrementerNbToursAvecEffet();
                }
                if (innonde.estTermine()) {
                    terrain.setStatutTerrain(new Asseche());
                    CombatLogger.log("Le terrain n'est plus inondé et redevient normal.");
                }
                break;
            case "Asseche":
                ((Asseche) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            default:
                break;
        }
    }
}
