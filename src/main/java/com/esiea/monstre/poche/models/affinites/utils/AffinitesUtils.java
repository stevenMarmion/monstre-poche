package com.esiea.monstre.poche.models.affinites.utils;

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
     * @param monstreAttaquant le monstre qui attaque (nécessaire pour Eau)
     * @param cible le monstre cible
     * @param terrain le terrain actuel
     */
    public static void appliqueCapaciteSpeciale(Type statut, Monstre monstreAttaquant, Monstre cible, Terrain terrain) {
        switch (statut.getLabelType().toLowerCase()) {
            case "feu":
                ((Feu) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "eau":
                ((Eau) statut).appliqueCapaciteSpeciale(terrain, monstreAttaquant);
                break;
            case "foudre":
                ((Foudre) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "terre":
                // La capacité spéciale Terre affecte le monstre attaquant lui-même
                ((Terre) statut).appliqueCapaciteSpeciale(monstreAttaquant);
                break;
            case "plante":
                // Plante se soigne lui-même
                ((Plante) statut).appliqueCapaciteSpeciale(monstreAttaquant);
                break;
            case "insecte":
                ((Insecte) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "normal":
                break;
            default:
                // System.err.println(new TypeIconnuException("Type inconnu: " + statut.getLabelType()));
                break;
        }
    }

    /**
     * Applique la capacité spéciale du type Nature si le terrain est Inondé.
     * CDC: Les monstres de type nature (Plante, Insecte) récupèrent 1/20 de leurs PV max sur terrain inondé.
     * 
     * @param statut le type dont la capacité spéciale doit être appliquée
     * @param cible le monstre cible
     * @param terrain le terrain actuel
     */
    public static void appliqueCapaciteSpecialeNature(Type statut, Monstre cible, Terrain terrain) {
        if (!terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
            return; // Pas d'effet si le terrain n'est pas inondé
        }
        
        switch (statut.getLabelType().toLowerCase()) {
            case "nature":
            case "plante":
            case "insecte":
                // Tous les types Nature bénéficient de la récupération
                // Pour Plante et Insecte, on appelle directement la méthode de Nature
                new Nature().appliqueCapaciteSpeciale(cible);
                break;
            default:
                // Pas un type Nature, pas d'effet
                break;
        }
    }
}
