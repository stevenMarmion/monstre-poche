package com.esiea.monstre.poche.etats.utils;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Brule;
import com.esiea.monstre.poche.etats.Empoisonne;
import com.esiea.monstre.poche.etats.Paralyse;
import com.esiea.monstre.poche.etats.SousTerre;
import com.esiea.monstre.poche.etats.StatutMonstre;

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
                break;
        }
    }
}
