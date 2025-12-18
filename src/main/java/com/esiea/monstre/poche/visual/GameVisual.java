package com.esiea.monstre.poche.visual;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Monstre;

public class GameVisual {
    private static final String SECTION_DIVIDER = "----------------------------------------";
    
    public static void afficherTitreSection(String titre) {
        System.out.println("\n" + SECTION_DIVIDER);
        System.out.println(" " + titre);
        System.out.println(SECTION_DIVIDER);
    }

    public static void afficherSousTitre(String sousTitre) {
        System.out.println("  > " + sousTitre);
    }

    public static void afficherErreur(String message) {
        System.out.println("  [!] " + message);
    }

    public static String demanderSaisie(Scanner scanner, String invite) {
        System.out.print(invite + " ");
        return scanner.nextLine().trim();
    }

    public static String formatterMonstre(Monstre monstre) {
        return String.format("%-15s | PV:%-4.0f/%-4.0f | ATK:%-3d | DEF:%-3d | VIT:%-3d | Type:%s", monstre.getNomMonstre(), monstre.getPointsDeVie(), monstre.getPointsDeVieMax(), monstre.getAttaque(), monstre.getDefense(), monstre.getVitesse(), monstre.getTypeMonstre().getLabelType());
    }

    public static String formatterAttaque(Attaque attaque) {
        return String.format("%-18s | Puissance:%-3d | Type:%s", attaque.getNomAttaque(), attaque.getPuissanceAttaque(), attaque.getTypeAttaque().getLabelType());
    }

    /**
     * Affiche le menu de sélection du mode de jeu
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return 1 pour jouer contre un Bot, 2 pour jouer à deux joueurs locaux
     */
    public static int afficherMenuModeJeu(Scanner scanner) {
        afficherTitreSection("MONSTRE POCHE - Selection du mode de jeu");
        
        System.out.println();
        System.out.println("  [1] Jouer contre un Bot");
        System.out.println("  [2] Jouer à deux joueurs (local)");
        System.out.println();
        
        String choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        
        while (!choix.equals("1") && !choix.equals("2")) {
            afficherErreur("Choix invalide. Veuillez entrer 1 ou 2.");
            choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        }
        
        return Integer.parseInt(choix);
    }

    /**
     * Affiche le menu de sélection du niveau de difficulté du Bot
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return 1 pour facile, 2 pour moyen, 3 pour difficile
     */
    public static int afficherMenuDifficulteBot(Scanner scanner) {
        afficherTitreSection("Selection du niveau de difficulte du Bot");
        
        System.out.println();
        System.out.println("  [1] Facile");
        System.out.println("  [2] Moyen");
        System.out.println("  [3] Difficile");
        System.out.println();
        
        String choix = demanderSaisie(scanner, "Votre choix (1, 2 ou 3) >");
        
        while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3")) {
            afficherErreur("Choix invalide. Veuillez entrer 1, 2 ou 3.");
            choix = demanderSaisie(scanner, "Votre choix (1, 2 ou 3) >");
        }
        
        return Integer.parseInt(choix);
    }
}
