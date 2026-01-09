package com.esiea.monstre.poche.models.battle.logs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.esiea.monstre.poche.models.battle.logs.enums.LoggingMode;
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
    private static final List<String> logs = new ArrayList<>();
    private static final List<String> currentTurnLogs = new ArrayList<>();
    
    private static Consumer<String> guiCallback = null;
    private static Consumer<String> networkCallback = null;

    private static LoggingMode loggingMode = LoggingMode.LOCAL_ONLY;

    /**
     * Définit le callback pour envoyer les logs à l'interface graphique.
     * @param callback Fonction qui reçoit chaque message de log
     */
    public static void setGuiCallback(Consumer<String> callback) {
        guiCallback = callback;
    }

    /**
     * Définit le callback pour envoyer les logs à une connexion réseau.
     * @param callback Fonction qui reçoit chaque message de log (ex: connection::sendInfo)
     */
    public static void setNetworkCallback(Consumer<String> callback) {
        networkCallback = callback;
    }

    /**
     * Définit le mode de logging.
     * @param mode Mode de logging à utiliser
     */
    public static void setLoggingMode(LoggingMode mode) {
        loggingMode = mode;
    }

    /**
     * Réinitialise les callbacks et le mode de logging aux valeurs par défaut.
     */
    public static void resetCallbacks() {
        guiCallback = null;
        networkCallback = null;
        loggingMode = LoggingMode.LOCAL_ONLY;
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
     * Log un message pour le réseau.
     */
    public static void network(String message) {
        addLog("[NETWORK] " + message);
    }
    
    /**
     * Méthode interne pour ajouter un log.
     * Respecte le mode de logging configuré pour déterminer où envoyer les logs.
     */
    private static void addLog(String message) {
        logs.add(message);
        currentTurnLogs.add(message);

        // Envoi vers le terminal local et/ou la connexion réseau selon le mode
        switch (loggingMode) {
            case LOCAL_ONLY:
                System.out.println(message);
                break;
            case NETWORK_ONLY:
                if (networkCallback != null) {
                    networkCallback.accept(message);
                }
                break;
            case BOTH:
                System.out.println(message);
                if (networkCallback != null) {
                    networkCallback.accept(message);
                }
                break;
        }

        // Envoi à l'interface graphique si callback défini (indépendant du mode de logging)
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
        addLog(titre.toUpperCase());
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
        addLog(joueur1.getNomJoueur() + "  VS  " + joueur2.getNomJoueur());
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

    public static void afficherActionsDisponibles() {
        addLog("Actions disponibles :");
        addLog("  1) Attaquer");
        addLog("  2) Utiliser un objet");
        addLog("  3) Changer de monstre");
    }

    /**
     * Affiche un menu avec des options.
     */
    public static void logMenu(String titre, String... options) {
        logTitre(titre);
        addLog("");
        for (int i = 0; i < options.length; i++) {
            addLog("[" + (i + 1) + "] " + options[i]);
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
