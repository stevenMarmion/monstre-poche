package com.esiea.monstre.poche.models.combats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;

/**
 * Logger centralisé pour tous les événements du jeu.
 * Gère l'affichage terminal ET la transmission vers l'interface graphique.
 * Tous les messages passent par cette classe unique.
 */
public class CombatLogger {
    
    // ===================================================================
    // CONFIGURATION
    // ===================================================================
    
    private static final String SEPARATOR = "========================================";
    private static final String LIGHT_SEPARATOR = "----------------------------------------";
    
    /** Liste de tous les logs de la session */
    private static final List<String> logs = new ArrayList<>();
    
    /** Liste des logs du tour actuel uniquement */
    private static final List<String> currentTurnLogs = new ArrayList<>();
    
    /** Callback pour envoyer les logs à l'interface graphique (si défini) */
    private static Consumer<String> guiCallback = null;
    
    /** Mode silencieux (pas d'affichage console) */
    private static boolean silentMode = false;
    
    /** Mode debug (affiche plus de détails) */
    private static boolean debugMode = false;

    // ===================================================================
    // CONFIGURATION METHODS
    // ===================================================================
    
    /**
     * Définit le callback pour envoyer les logs à l'interface graphique.
     * @param callback Fonction qui reçoit chaque message de log
     */
    public static void setGuiCallback(Consumer<String> callback) {
        guiCallback = callback;
    }
    
    /**
     * Active/désactive le mode silencieux (pas d'affichage console).
     */
    public static void setSilentMode(boolean silent) {
        silentMode = silent;
    }
    
    /**
     * Active/désactive le mode debug.
     */
    public static void setDebugMode(boolean debug) {
        debugMode = debug;
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

    // ===================================================================
    // MÉTHODES DE LOG DE BASE
    // ===================================================================
    
    /**
     * Log un message simple.
     */
    public static void log(String message) {
        addLog(message);
    }
    
    /**
     * Log un message de debug (affiché seulement si debugMode est actif).
     */
    public static void debug(String message) {
        if (debugMode) {
            addLog("[DEBUG] " + message);
        }
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
        
        // Affichage console (sauf mode silencieux)
        if (!silentMode) {
            System.out.println(message);
        }
        
        // Envoi à l'interface graphique si callback défini
        if (guiCallback != null) {
            guiCallback.accept(message);
        }
    }

    // ===================================================================
    // LOGS DE STRUCTURE (TITRES, SECTIONS)
    // ===================================================================
    
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
        addLog("  ► " + sousTitre);
        addLog(LIGHT_SEPARATOR);
    }
    
    /**
     * Affiche un séparateur léger.
     */
    public static void logSeparateur() {
        addLog(LIGHT_SEPARATOR);
    }

    // ===================================================================
    // LOGS DE COMBAT
    // ===================================================================
    
    /**
     * Log le début d'un combat.
     */
    public static void logDebutCombat(Joueur joueur1, Joueur joueur2) {
        logTitre("COMBAT LANCE !");
        addLog("");
        addLog("  " + joueur1.getNomJoueur() + "  VS  " + joueur2.getNomJoueur());
        addLog("");
        logSeparateur();
    }
    
    /**
     * Log le début d'un tour.
     */
    public static void logDebutTour(int numeroTour) {
        addLog("");
        addLog(SEPARATOR);
        addLog("  TOUR " + numeroTour);
        addLog(SEPARATOR);
    }
    
    /**
     * Log le tour d'un joueur.
     */
    public static void logTourJoueur(Joueur joueur) {
        Monstre monstre = joueur.getMonstreActuel();
        addLog("");
        addLog("  ▶ Tour de " + joueur.getNomJoueur());
        if (monstre != null) {
            addLog("    Monstre actif: " + monstre.getNomMonstre() + 
                   " [" + monstre.getTypeMonstre().getLabelType() + "]");
            addLog("    PV: " + (int)monstre.getPointsDeVie() + "/" + (int)monstre.getPointsDeVieMax() +
                   " | ATK: " + monstre.getAttaque() + 
                   " | DEF: " + monstre.getDefense() + 
                   " | VIT: " + monstre.getVitesse());
        }
    }
    
    /**
     * Log une attaque.
     */
    public static void logAttaque(Monstre attaquant, Monstre cible, Attaque attaque, int degats) {
        addLog("");
        addLog("  " + attaquant.getNomMonstre() + " utilise " + 
               (attaque != null ? attaque.getNomAttaque() : "Charge") + " !");
        addLog("    -> " + cible.getNomMonstre() + " subit " + degats + " degats");
        addLog("    -> PV de " + cible.getNomMonstre() + ": " + 
               (int)cible.getPointsDeVie() + "/" + (int)cible.getPointsDeVieMax());
    }
    
    /**
     * Log une attaque ratée.
     */
    public static void logAttaqueRatee(Monstre attaquant, Attaque attaque) {
        addLog("");
        addLog("  " + attaquant.getNomMonstre() + " utilise " + 
               (attaque != null ? attaque.getNomAttaque() : "Charge") + "...");
        addLog("    -> L'attaque a echoue !");
    }
    
    /**
     * Log un K.O.
     */
    public static void logKO(Monstre monstre) {
        addLog("");
        addLog("  " + monstre.getNomMonstre() + " est K.O. !");
    }
    
    /**
     * Log un changement de monstre.
     */
    public static void logChangementMonstre(Joueur joueur, Monstre ancienMonstre, Monstre nouveauMonstre) {
        addLog("");
        if (ancienMonstre != null) {
            addLog("  " + joueur.getNomJoueur() + " rappelle " + ancienMonstre.getNomMonstre());
        }
        addLog("  " + joueur.getNomJoueur() + " envoie " + nouveauMonstre.getNomMonstre() + " !");
        addLog("    [" + nouveauMonstre.getTypeMonstre().getLabelType() + "] " +
               "PV: " + (int)nouveauMonstre.getPointsDeVie() + "/" + (int)nouveauMonstre.getPointsDeVieMax());
    }
    
    /**
     * Log l'utilisation d'un objet.
     */
    public static void logUtilisationObjet(Joueur joueur, String nomObjet, Monstre cible) {
        addLog("");
        addLog("  " + joueur.getNomJoueur() + " utilise " + nomObjet + " sur " + cible.getNomMonstre());
    }
    
    /**
     * Log un effet de statut.
     */
    public static void logEffetStatut(Monstre monstre, String effet) {
        addLog("    " + monstre.getNomMonstre() + " " + effet);
    }
    
    /**
     * Log un effet de terrain.
     */
    public static void logEffetTerrain(String effet) {
        addLog("    [TERRAIN] " + effet);
    }
    
    /**
     * Log la fin du combat.
     */
    public static void logFinCombat(Joueur gagnant) {
        addLog("");
        addLog(SEPARATOR);
        addLog("  VICTOIRE DE " + gagnant.getNomJoueur().toUpperCase() + " !");
        addLog(SEPARATOR);
        addLog("");
    }

    // ===================================================================
    // LOGS DE SÉLECTION
    // ===================================================================
    
    /**
     * Log la sélection d'un monstre.
     */
    public static void logSelectionMonstre(Joueur joueur, Monstre monstre) {
        addLog("  " + joueur.getNomJoueur() + " a choisi: " + monstre.getNomMonstre() + 
               " [" + monstre.getTypeMonstre().getLabelType() + "]");
    }
    
    /**
     * Log la sélection d'une attaque.
     */
    public static void logSelectionAttaque(Monstre monstre, Attaque attaque) {
        addLog("  " + monstre.getNomMonstre() + " apprend: " + attaque.getNomAttaque() + 
               " (Puissance: " + attaque.getPuissanceAttaque() + ")");
    }
    
    /**
     * Affiche la liste des monstres disponibles.
     */
    public static void logListeMonstres(List<Monstre> monstres) {
        int index = 1;
        for (Monstre m : monstres) {
            addLog(String.format("  [%d] %-15s | Type: %-8s | PV: %3.0f | ATK: %3d | DEF: %3d | VIT: %3d",
                    index++, m.getNomMonstre(), m.getTypeMonstre().getLabelType(),
                    m.getPointsDeVieMax(), m.getAttaque(), m.getDefense(), m.getVitesse()));
        }
    }
    
    /**
     * Affiche la liste des attaques disponibles.
     */
    public static void logListeAttaques(List<Attaque> attaques) {
        int index = 1;
        for (Attaque a : attaques) {
            addLog(String.format("  [%d] %-18s | Type: %-8s | Puissance: %3d | Utilisations: %2d",
                    index++, a.getNomAttaque(), a.getTypeAttaque().getLabelType(),
                    a.getPuissanceAttaque(), a.getNbUtilisations()));
        }
    }

    // ===================================================================
    // LOGS DE MENU (TERMINAL)
    // ===================================================================
    
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
     * Affiche les actions disponibles pendant un combat.
     */
    public static void logActionsDisponibles() {
        addLog("");
        addLog("  Actions disponibles:");
        addLog("    [1] Attaquer");
        addLog("    [2] Utiliser un objet");
        addLog("    [3] Changer de monstre");
        addLog("");
    }

    // ===================================================================
    // LOGS BOT
    // ===================================================================
    
    /**
     * Log une action du bot.
     */
    public static void logActionBot(String nomBot, String action) {
        addLog("  [BOT] " + nomBot + " " + action);
    }

    // ===================================================================
    // LOGS RÉSEAU / EN LIGNE
    // ===================================================================
    
    /**
     * Log un événement réseau.
     */
    public static void logReseau(String message) {
        addLog("  [RESEAU] " + message);
    }
    
    /**
     * Log la connexion d'un joueur.
     */
    public static void logConnexion(String nomJoueur) {
        addLog("  [RESEAU] " + nomJoueur + " s'est connecte");
    }
    
    /**
     * Log la déconnexion d'un joueur.
     */
    public static void logDeconnexion(String nomJoueur) {
        addLog("  [RESEAU] " + nomJoueur + " s'est deconnecte");
    }

    // ===================================================================
    // ACCESSEURS
    // ===================================================================
    
    /**
     * Retourne une copie de tous les logs.
     */
    public static List<String> getLogs() {
        return new ArrayList<>(logs);
    }
    
    /**
     * Retourne une copie des logs du tour actuel.
     */
    public static List<String> getCurrentTurnLogs() {
        return new ArrayList<>(currentTurnLogs);
    }
    
    /**
     * Retourne tous les logs formatés en une seule chaîne.
     */
    public static String getFormattedLogs() {
        return String.join("\n", logs);
    }
    
    /**
     * Retourne les logs du tour actuel formatés en une seule chaîne.
     */
    public static String getFormattedCurrentTurnLogs() {
        return String.join("\n", currentTurnLogs);
    }
    
    /**
     * Retourne le dernier log.
     */
    public static String getLastLog() {
        return logs.isEmpty() ? "" : logs.get(logs.size() - 1);
    }
    
    /**
     * Retourne les N derniers logs.
     */
    public static List<String> getLastLogs(int count) {
        int start = Math.max(0, logs.size() - count);
        return new ArrayList<>(logs.subList(start, logs.size()));
    }
}
