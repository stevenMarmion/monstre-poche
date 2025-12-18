package src.com.esiea.monstre.poche.etats.utils;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.entites.Terrain;
import src.com.esiea.monstre.poche.etats.Asseche;
import src.com.esiea.monstre.poche.etats.Innonde;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant, double degatsAffliges) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                // on est censé pouvoir jouer sur le monstre pour calculer ses chance de rater son attaque
                if (!monstreAttaquant.getTypeMonstre().getLabelType().equals("Eau")) {
                    ((Innonde) terrain.getStatutTerrain()).appliquerEffetsTerrain(monstreAttaquant, degatsAffliges);   
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
