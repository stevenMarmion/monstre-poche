package com.esiea.monstre.poche.models.etats.utils;


import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Brule;
import com.esiea.monstre.poche.models.etats.Empoisonne;
import com.esiea.monstre.poche.models.etats.Paralyse;
import com.esiea.monstre.poche.models.etats.SousTerre;
import com.esiea.monstre.poche.models.etats.StatutMonstre;

public class StatutMonstreUtils {
    
    public static void appliquerStatutMonstre(StatutMonstre statutMonstre, Monstre cible, double degatsAffliges) {
        switch (statutMonstre.getLabelStatut()) {
            case "Brule":
                CombatLogger.log(cible.getNomMonstre() + " souffre de brûlures.");
                ((Brule) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "Empoisonne":
                CombatLogger.log(cible.getNomMonstre() + " est empoisonné.");
                ((Empoisonne) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "Paralyse":
                CombatLogger.log(cible.getNomMonstre() + " est paralysé.");
                ((Paralyse) statutMonstre).appliquerEffets(cible, degatsAffliges);
                break;
            case "SousTerre":
                CombatLogger.log(cible.getNomMonstre() + " est sous terre, difficile à atteindre.");
                ((SousTerre) statutMonstre).appliquerEffets(cible);
                break;
            default:
                // System.err.println(new TypeIconnuException("Statut inconnu: " + statutMonstre.getLabelStatut()));
                break;
        }
    }
}
