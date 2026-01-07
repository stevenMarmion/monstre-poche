package com.esiea.monstre.poche.models.etats.utils;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.etats.Innonde;

public class StatutTerrainUtils {
    
    public static void appliquerStatutTerrain(Terrain terrain, Monstre monstreAttaquant, double degatsAffliges) {
        switch (terrain.getStatutTerrain().getLabelStatut()) {
            case "Innonde":
                Innonde innonde = (Innonde) terrain.getStatutTerrain();
                if (!monstreAttaquant.getTypeMonstre().getLabelType().equals("Eau")) {
                    CombatLogger.log("Le terrain inondé gêne " + monstreAttaquant.getNomMonstre() + ". (" + innonde.getNbToursAvecEffet() + " tours restants)");
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
                CombatLogger.log("Le terrain est asséché.");
                ((Asseche) terrain.getStatutTerrain()).appliquerEffets(terrain);
                break;
            default:
                break;
        }
    }
}
