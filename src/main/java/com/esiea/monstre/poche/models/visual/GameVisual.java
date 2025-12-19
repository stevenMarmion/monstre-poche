package com.esiea.monstre.poche.models.visual;

import java.util.Scanner;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

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

    public static String formatterTitre(String titre) {
        return "\n" + SECTION_DIVIDER + "\n " + titre + "\n" + SECTION_DIVIDER;
    }

    public static String afficherSousTitrePourTous(String sousTitre) {
        System.out.println("  > " + sousTitre);
        return "  > " + sousTitre;
    }

    public static String formatterErreur(String message) {
        return "  [!] " + message;
    }

    public static String formatterSousTitre(String sousTitre) {
        return "  > " + sousTitre;
    }

    /**
     * Affiche le menu de sélection du mode de jeu
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return 1 pour jouer contre un Bot, 2 pour jouer à deux joueurs locaux, 3 pour jouer en ligne
     */
    public static int afficherMenuModeJeu(Scanner scanner) {
        afficherTitreSection("MONSTRE POCHE - Selection du mode de jeu");
        
        System.out.println();
        System.out.println("  [1] Jouer contre un Bot");
        System.out.println("  [2] Jouer à deux joueurs (local)");
        System.out.println("  [3] Jouer en ligne");
        System.out.println();
        
        String choix = demanderSaisie(scanner, "Votre choix (1, 2 ou 3) >");
        
        while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3")) {
            afficherErreur("Choix invalide. Veuillez entrer 1, 2 ou 3.");
            choix = demanderSaisie(scanner, "Votre choix (1, 2 ou 3) >");
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

    /**
     * Affiche le menu pour le jeu en ligne
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return 1 pour créer un serveur, 2 pour se connecter à un serveur
     */
    public static int afficherMenuJeuEnLigne(Scanner scanner) {
        afficherTitreSection("Jeu en ligne - Mode de connexion");
        
        System.out.println();
        System.out.println("  [1] Créer un serveur (et attendre un adversaire)");
        System.out.println("  [2] Se connecter à un serveur");
        System.out.println();
        
        String choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        
        while (!choix.equals("1") && !choix.equals("2")) {
            afficherErreur("Choix invalide. Veuillez entrer 1 ou 2.");
            choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        }
        
        return Integer.parseInt(choix);
    }

    /**
     * Affiche le menu de configuration du serveur
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return Un array contenant [port]
     */
    public static int demanderPortServeur(Scanner scanner) {
        afficherTitreSection("Configuration du serveur");
        
        String portStr = demanderSaisie(scanner, "Port d'écoute (défaut: 5555) >");
        
        try {
            int port = portStr.isEmpty() ? 5555 : Integer.parseInt(portStr);
            if (port < 1024 || port > 65535) {
                afficherErreur("Port invalide. Doit être entre 1024 et 65535. Utilisation du port 5555.");
                return 5555;
            }
            return port;
        } catch (NumberFormatException e) {
            afficherErreur("Port invalide. Utilisation du port 5555 par défaut.");
            return 5555;
        }
    }

    /**
     * Affiche le menu de connexion au serveur
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return Un array contenant [adresse, port]
     */
    public static String[] demanderConfigurationServeur(Scanner scanner) {
        afficherTitreSection("Connexion au serveur");
        
        String adresse = demanderSaisie(scanner, "Adresse du serveur (défaut: localhost) >");
        adresse = adresse.isEmpty() ? "localhost" : adresse;
        
        String portStr = demanderSaisie(scanner, "Port du serveur (défaut: 5555) >");
        int port = 5555;
        try {
            port = portStr.isEmpty() ? 5555 : Integer.parseInt(portStr);
            if (port < 1024 || port > 65535) {
                afficherErreur("Port invalide. Utilisation du port 5555.");
                port = 5555;
            }
        } catch (NumberFormatException e) {
            afficherErreur("Port invalide. Utilisation du port 5555 par défaut.");
        }
        
        return new String[]{adresse, String.valueOf(port)};
    }
}
