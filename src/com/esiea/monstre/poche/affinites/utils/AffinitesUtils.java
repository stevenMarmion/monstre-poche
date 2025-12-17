package src.com.esiea.monstre.poche.affinites.utils;

import src.com.esiea.monstre.poche.affinites.Eau;
import src.com.esiea.monstre.poche.affinites.Feu;
import src.com.esiea.monstre.poche.affinites.Foudre;
import src.com.esiea.monstre.poche.affinites.Insecte;
import src.com.esiea.monstre.poche.affinites.Nature;
import src.com.esiea.monstre.poche.affinites.Plante;
import src.com.esiea.monstre.poche.affinites.Terre;
import src.com.esiea.monstre.poche.affinites.Type;
import src.com.esiea.monstre.poche.entites.Monstre;

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
            //TODO type normal qui va etre créé par steven
                return new Nature();
            default:
                System.out.println("Type '%s' non reconnu par le parser. Type Normal appliqué par défaut.");
                //TODO type normal qui va etre créé par steven
                return new Nature();
        }
    }


    public static void appliqueCapaciteSpeciale(Type statut, Monstre cible) {
        switch (statut.getLabelType().toLowerCase()) {
            case "feu":
                ((Feu) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "eau":
                ((Eau) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "foudre":
                ((Foudre) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "terre":
                ((Terre) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "plante":
                ((Plante) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "insecte":
                ((Insecte) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "nature":
                ((Nature) statut).appliqueCapaciteSpeciale(cible);
                break;
            case "normal":
                break;
            default:
                break;
        }
    }
}
