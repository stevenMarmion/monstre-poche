package com.esiea.monstre.poche.models.status.utils;

import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Brule;
import com.esiea.monstre.poche.models.status.monster.Empoisonne;
import com.esiea.monstre.poche.models.status.monster.SousTerre;
import com.esiea.monstre.poche.models.status.monster.StatutMonstre;
import com.esiea.monstre.poche.models.status.monster.Paralyse;

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
                ((Paralyse) statutMonstre).appliquerEffets(cible);
                break;
            case "SousTerre":
                ((SousTerre) statutMonstre).appliquerEffets(cible);
                break;
            default:
                break;
        }
    }
}
