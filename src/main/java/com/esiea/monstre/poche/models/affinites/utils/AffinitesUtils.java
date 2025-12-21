package com.esiea.monstre.poche.models.affinites.utils;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.affinites.Eau;
import com.esiea.monstre.poche.models.affinites.Feu;
import com.esiea.monstre.poche.models.affinites.Foudre;
import com.esiea.monstre.poche.models.affinites.Insecte;
import com.esiea.monstre.poche.models.affinites.Nature;
import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.affinites.Plante;
import com.esiea.monstre.poche.models.affinites.Terre;
import com.esiea.monstre.poche.models.affinites.Type;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;

/**
 * Classe utilitaire proposant des méthodes pour gérer les types et les comparaisons
 */
public class AffinitesUtils {

    /**
     * Crée une instance de Type à partir d'une chaîne de caractères
     * Utilisé pour le parsing de fichiers de chargement de monstres ou d'attaques
     */
    public static Type getTypeFromString(String typeStr) {
        switch (typeStr.toLowerCase()) {
            case "feu":
                return new Feu();
            case "eau":
                return new Eau();
            case "foudre":
                return new Foudre();
            case "terre":
                return new Terre();
            case "plante":
                return new Plante();
            case "insecte":
                return new Insecte();
            case "nature":
                return new Nature();
            case "normal":
                return new Normal();
            default:
                // System.err.println(new TypeIconnuException(String.format("Type '%s' non reconnu par le parser. Type Normal appliqué par défaut.", typeStr)));
                return new Normal();
        }
    }

    /**
     * Applique la capacité spéciale d'un type à un monstre cible en fonction du type
     * 
     * @param statut le type dont la capacité spéciale doit être appliquée
     * @param cible le monstre cible
     * @param terrain le terrain actuel
     */
    public static void appliqueCapaciteSpeciale(Type statut, Monstre cible, Terrain terrain) {
        switch (statut.getLabelType().toLowerCase()) {
            case "feu":
                CombatLogger.log("Capacité spéciale Feu appliquée sur " + cible.getNomMonstre() + ".");
                ((Feu) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "eau":
                String before = terrain.getStatutTerrain().getLabelStatut();
                ((Eau) statut).appliqueCapaciteSpeciale(terrain);
                String after = terrain.getStatutTerrain().getLabelStatut();
                if (!after.equals(before)) {
                    CombatLogger.log("La capacité Eau modifie le terrain: " + before + " -> " + after + ".");
                    if (after.equals("Innonde")) {
                        CombatLogger.log("Le terrain est désormais inondé.");
                    }
                } else {
                    CombatLogger.log("La capacité Eau n'a pas modifié le terrain.");
                }
                break;
            case "foudre":
                CombatLogger.log("Capacité spéciale Foudre appliquée sur " + cible.getNomMonstre() + ".");
                ((Foudre) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "terre":
                CombatLogger.log("Capacité spéciale Terre appliquée sur " + cible.getNomMonstre() + ".");
                ((Terre) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "plante":
                CombatLogger.log("Capacité spéciale Plante appliquée sur " + cible.getNomMonstre() + ".");
                ((Plante) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "insecte":
                CombatLogger.log("Capacité spéciale Insecte appliquée sur " + cible.getNomMonstre() + ".");
                ((Insecte) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "nature":
                if (terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
                    CombatLogger.log("Capacité spéciale Nature activée (terrain inondé) sur " + cible.getNomMonstre() + ".");
                    ((Nature) statut).appliqueCapaciteSpeciale(cible);
                }
                break;
            case "normal":
                break;
            default:
                // System.err.println(new TypeIconnuException("Type inconnu: " + statut.getLabelType()));
                break;
        }
    }

    /**
     * Applique la capacité spéciale du type Nature si le terrain est Inondé
     * 
     * @param statut le type dont la capacité spéciale doit être appliquée
     * @param cible le monstre cible
     * @param terrain le terrain actuel
     */
    public static void appliqueCapaciteSpecialeNature(Type statut, Monstre cible, Terrain terrain) {
        switch (statut.getLabelType().toLowerCase()) {
            case "nature":
                if (terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
                    CombatLogger.log("Capacité Nature de soutien (terrain inondé) sur " + cible.getNomMonstre() + ".");
                    ((Nature) statut).appliqueCapaciteSpeciale(cible);
                }
                break;
            default:
                // System.err.println(new TypeIconnuException("Type inconnu: " + statut.getLabelType()));
                break;
        }
    }
}
