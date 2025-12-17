package src.com.esiea.monstre.poche.etats.utils;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.entites.Terrain;
import src.com.esiea.monstre.poche.etats.Asseche;
import src.com.esiea.monstre.poche.etats.Innonde;
import src.com.esiea.monstre.poche.etats.StatutTerrain;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                ((Innonde) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            case "Asseche":
                ((Asseche) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            default:
                break;
        }
    }
}
