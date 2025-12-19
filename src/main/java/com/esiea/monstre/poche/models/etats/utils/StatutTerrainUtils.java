package com.esiea.monstre.poche.models.etats.utils;

import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.etats.Innonde;

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
                // System.err.println(new TypeIconnuException("Statut de terrain inconnu: " + terrain.getStatutTerrain().getLabelStatut()));
                break;
        }
    }
}
