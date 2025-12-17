package com.esiea.monstre.poche.affinites.utils;

import com.esiea.monstre.poche.affinites.*;

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
            case "electric":
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
}
