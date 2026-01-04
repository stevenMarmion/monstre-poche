package com.esiea.monstre.poche.models.visual;

import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

/**
 * Classe utilitaire pour l'affichage visuel dans le terminal.
 * Utilise CombatLogger pour centraliser tous les affichages.
 */
public class GameVisual {
    
    public static void afficherTitreSection(String titre) {
        CombatLogger.logTitre(titre);
    }

    public static void afficherSousTitre(String sousTitre) {
        CombatLogger.logSousTitre(sousTitre);
    }

    public static void afficherErreur(String message) {
        CombatLogger.error(message);
    }

    public static String demanderSaisie(Scanner scanner, String invite) {
        System.out.print(invite + " ");
        return scanner.nextLine().trim();
    }

    public static String formatterMonstre(Monstre monstre) {
        return String.format("%-15s | PV:%-4.0f/%-4.0f | ATK:%-3d | DEF:%-3d | VIT:%-3d | Type:%s", 
                monstre.getNomMonstre(), monstre.getPointsDeVie(), monstre.getPointsDeVieMax(), 
                monstre.getAttaque(), monstre.getDefense(), monstre.getVitesse(), 
                monstre.getTypeMonstre().getLabelType());
    }

    public static String formatterAttaque(Attaque attaque) {
        return String.format("%-18s | Puissance:%-3d | Type:%s", 
                attaque.getNomAttaque(), attaque.getPuissanceAttaque(), 
                attaque.getTypeAttaque().getLabelType());
    }

    public static String formatterTitre(String titre) {
        return "========================================\n  " + titre + "\n========================================";
    }

    public static String afficherSousTitrePourTous(String sousTitre) {
        CombatLogger.log("  ► " + sousTitre);
        return "  ► " + sousTitre;
    }

    public static String formatterErreur(String message) {
        return "[ERREUR] " + message;
    }

    public static String formatterSousTitre(String sousTitre) {
        return "  ► " + sousTitre;
    }

    /**
     * Affiche le menu de sélection du mode de jeu
     * @param scanner Le scanner pour lire l'entrée utilisateur
     * @return 1 pour jouer contre un Bot, 2 pour jouer à deux joueurs locaux, 3 pour jouer en ligne
     */
    public static int afficherMenuModeJeu(Scanner scanner) {
        CombatLogger.logMenu("MONSTRE POCHE - Sélection du mode de jeu",
                "Jouer contre un Bot",
                "Jouer à deux joueurs (local)",
                "Jouer en ligne");
        
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
        CombatLogger.logMenu("Sélection du niveau de difficulté du Bot",
                "Facile",
                "Moyen",
                "Difficile");
        
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
        CombatLogger.logMenu("Jeu en ligne - Mode de connexion",
                "Créer un serveur (et attendre un adversaire)",
                "Se connecter à un serveur");
        
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
        CombatLogger.logTitre("Configuration du serveur");
        
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
        CombatLogger.logTitre("Connexion au serveur");
        
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

    /**
     * Permet de demander au joueur s'il souhaite le jeu par interface ou par terminal
     */
    public static int afficherDemandeInterfaceTerminal(Scanner scanner) {
        CombatLogger.logMenu("Monstre Poche - Mode de jeu",
                "Jouer par interface",
                "Jouer par terminal");
        
        String choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        
        while (!choix.equals("1") && !choix.equals("2")) {
            afficherErreur("Choix invalide. Veuillez entrer 1 ou 2.");
            choix = demanderSaisie(scanner, "Votre choix (1 ou 2) >");
        }
        
        return Integer.parseInt(choix);
    }
}
