package com.esiea.monstre.pochebis.models.etats.utils;

import com.esiea.monstre.pochebis.models.combats.CombatLogger;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.entites.Terrain;
import com.esiea.monstre.pochebis.models.etats.Asseche;
import com.esiea.monstre.pochebis.models.etats.Innonde;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant, double degatsAffliges) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                // on est censé pouvoir jouer sur le monstre pour calculer ses chance de rater son attaque
                if (!monstreAttaquant.getTypeMonstre().getLabelType().equals("Eau")) {
                    CombatLogger.log("Le terrain inondé gêne " + monstreAttaquant.getNomMonstre() + ".");
                    ((Innonde) terrain.getStatutTerrain()).appliquerEffetsTerrain(monstreAttaquant, degatsAffliges);   
                }
                break;
            case "Asseche":
                CombatLogger.log("Le terrain est asséché.");
                ((Asseche) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            default:
                // System.err.println(new TypeIconnuException("Statut de terrain inconnu: " + terrain.getStatutTerrain().getLabelStatut()));
                break;
        }
    }
}
