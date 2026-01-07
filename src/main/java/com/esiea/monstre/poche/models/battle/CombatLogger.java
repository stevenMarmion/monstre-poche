package com.esiea.monstre.poche.models.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;

/**
 * Logger centralisé pour tous les événements du jeu.
 * Gère l'affichage terminal ET la transmission vers l'interface graphique.
 * Tous les messages passent par cette classe unique.
 */
public class CombatLogger {
    private static final String SEPARATOR = "========================================";
    private static final String LIGHT_SEPARATOR = "----------------------------------------";
    
    /** Liste de tous les logs de la session */
    private static final List<String> logs = new ArrayList<>();
    
    /** Liste des logs du tour actuel uniquement */
    private static final List<String> currentTurnLogs = new ArrayList<>();
    
    /** Callback pour envoyer les logs à l'interface graphique (si défini) */
    private static Consumer<String> guiCallback = null;

    /**
     * Définit le callback pour envoyer les logs à l'interface graphique.
     * @param callback Fonction qui reçoit chaque message de log
     */
    public static void setGuiCallback(Consumer<String> callback) {
        guiCallback = callback;
    }
    
    /**
     * Efface tous les logs.
     */
    public static void clear() {
        logs.clear();
        currentTurnLogs.clear();
    }
    
    /**
     * Efface les logs du tour actuel uniquement.
     * À appeler au début de chaque nouveau tour.
     */
    public static void clearCurrentTurn() {
        currentTurnLogs.clear();
    }

    /**
     * Log un message simple.
     */
    public static void log(String message) {
        addLog(message);
    }
    
    /**
     * Log un message de debug.
     */
    public static void debug(String message) {
        addLog("[DEBUG] " + message);
    }
    
    /**
     * Log un message d'erreur.
     */
    public static void error(String message) {
        addLog("[ERREUR] " + message);
    }
    
    /**
     * Log un message d'information.
     */
    public static void info(String message) {
        addLog("[INFO] " + message);
    }
    
    /**
     * Méthode interne pour ajouter un log.
     */
    private static void addLog(String message) {
        logs.add(message);
        currentTurnLogs.add(message);
        
        System.out.println(message);
        
        // Envoi à l'interface graphique si callback défini
        if (guiCallback != null) {
            guiCallback.accept(message);
        }
    }

    /**
     * Affiche un titre de section principal.
     */
    public static void logTitre(String titre) {
        addLog("");
        addLog(SEPARATOR);
        addLog("  " + titre.toUpperCase());
        addLog(SEPARATOR);
    }
    
    /**
     * Affiche un sous-titre.
     */
    public static void logSousTitre(String sousTitre) {
        addLog("");
        addLog("  --> " + sousTitre);
        addLog(LIGHT_SEPARATOR);
    }

    /**
     * Log le début d'un combat.
     */
    public static void logDebutCombat(Joueur joueur1, Joueur joueur2) {
        logTitre("COMBAT LANCE !");
        addLog("");
        addLog("  " + joueur1.getNomJoueur() + "  VS  " + joueur2.getNomJoueur());
        addLog("");
        addLog(LIGHT_SEPARATOR);
    }
    
    /**
     * Log le tour d'un joueur.
     */
    public static void logTourJoueur(Joueur joueur) {
        Monstre monstre = joueur.getMonstreActuel();
        addLog("");
        addLog("  --> Tour de " + joueur.getNomJoueur());
        if (monstre != null) {
            addLog("    Monstre actif: " + monstre.getNomMonstre() + " [" + monstre.getTypeMonstre().getLabelType() + "]");
            addLog("    PV: " + (int)monstre.getPointsDeVie() + "/" + (int)monstre.getPointsDeVieMax() +
                   " | ATK: " + monstre.getAttaque() + 
                   " | DEF: " + monstre.getDefense() + 
                   " | VIT: " + monstre.getVitesse());
        }
    }

    /**
     * Affiche un menu avec des options.
     */
    public static void logMenu(String titre, String... options) {
        logTitre(titre);
        addLog("");
        for (int i = 0; i < options.length; i++) {
            addLog("  [" + (i + 1) + "] " + options[i]);
        }
        addLog("");
    }
    
    /**
     * Retourne les logs du tour actuel formatés en une seule chaîne.
     */
    public static String getFormattedCurrentTurnLogs() {
        return String.join("\n", currentTurnLogs);
    }
}
