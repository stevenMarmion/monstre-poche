package com.esiea.monstre.poche.models.status.utils;

import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.terrain.Asseche;
import com.esiea.monstre.poche.models.status.terrain.Innonde;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant, double degatsAffliges) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                Innonde innonde = (Innonde) terrain.getStatutTerrain();
                innonde.appliquerEffetsTerrain(monstreAttaquant, terrain, degatsAffliges);   
                break;
            case "Asseche":
                ((Asseche) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            default:
                break;
        }
    }
}
