package com.esiea.monstre.pochebis.models.etats.utils;

import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.etats.Brule;
import com.esiea.monstre.pochebis.models.etats.Empoisonne;
import com.esiea.monstre.pochebis.models.etats.Paralyse;
import com.esiea.monstre.pochebis.models.etats.SousTerre;
import com.esiea.monstre.pochebis.models.etats.StatutMonstre;

public class StatutMonstreUtils {
    
    public static void appliquerStatutMonstre(StatutMonstre statutMonstre, Monstre cible, double degatsAffliges) {
        switch (statutMonstre.getLabelStatut()) {
            case "Brule":
                ((Brule) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "Empoisonne":
                ((Empoisonne) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "Paralyse":
                ((Paralyse) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "SousTerre":
                ((SousTerre) statutMonstre).appliquerEffets(cible);
                break;
            default:
                // System.err.println(new TypeIconnuException("Statut inconnu: " + statutMonstre.getLabelStatut()));
                break;
        }
    }
}
