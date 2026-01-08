package com.esiea.monstre.poche.models.battle.modes;

import java.io.IOException;
import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.Combat;
import com.esiea.monstre.poche.models.battle.logs.enums.LoggingMode;
import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.network.OnlineConnection;

/**
 * Variante en ligne du combat : le joueur1 est local, le joueur2 joue à distance.
 * Implémente la stratégie d'I/O avec Scanner pour le joueur local et OnlineConnection pour le joueur distant.
 */
public class CombatEnLigne extends Combat {
    private final OnlineConnection connection;
    private final Scanner scanner = new Scanner(System.in);

    private boolean communicatingWithRemote = false;

    public CombatEnLigne(Joueur joueurLocal, Joueur joueurDistant, OnlineConnection connection) {
        super(joueurLocal, joueurDistant);
        this.connection = connection;

        // Configuration du logger pour envoyer les logs via la connexion réseau
        CombatLogger.setNetworkCallback(connection::sendInfo);
    }

    // ========================================
    // Implémentation des hook methods (stratégie I/O hybride)
    // ========================================

    @Override
    protected void afficherMessage(String message) {
        setLoggingModeBasedOnContext();
        CombatLogger.info(message);
    }

    @Override
    protected void afficherTitre(String titre) {
        setLoggingModeBasedOnContext();
        CombatLogger.logTitre(titre);
    }

    @Override
    protected void afficherSousTitre(String sousTitre) {
        setLoggingModeBasedOnContext();
        CombatLogger.logSousTitre(sousTitre);
    }

    @Override
    protected void afficherErreur(String erreur) {
        setLoggingModeBasedOnContext();
        CombatLogger.error(erreur);
    }

    /**
     * Configure le mode de logging en fonction du contexte (local ou distant).
     */
    private void setLoggingModeBasedOnContext() {
        if (communicatingWithRemote) {
            CombatLogger.setLoggingMode(LoggingMode.NETWORK_ONLY);
        } else {
            CombatLogger.setLoggingMode(LoggingMode.LOCAL_ONLY);
        }
    }

    @Override
    protected String demanderSaisie(String prompt) {
        if (communicatingWithRemote) {
            try {
                return connection.ask(prompt);
            } catch (IOException e) {
                CombatLogger.error("Connexion fermee pendant la saisie.");
                return "";
            }
        } else {
            return GameVisual.demanderSaisie(scanner, prompt);
        }
    }

    // ========================================
    // Méthodes spécifiques au combat en ligne
    // ========================================

    @Override
    public void lancer() {
        afficherTitrePourTous("Configuration de vos monstres");

        communicatingWithRemote = false;
        this.selectionnerMonstre(joueur1);
        this.selectionnerAttaque(joueur1);
        this.selectionnerObjet(joueur1);

        afficherTitrePourTous("Configuration adversaire");
        communicatingWithRemote = true;
        this.selectionnerMonstre(joueur2);
        this.selectionnerAttaque(joueur2);
        this.selectionnerObjet(joueur2);

        afficherTitrePourTous("COMBAT EN LIGNE LANCE !");
        this.executerTour();
    }

    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            communicatingWithRemote = false;
            Object actionLocal = this.gereChoixAction(joueur1);
            communicatingWithRemote = true;
            Object actionDistant = this.gereChoixAction(joueur2);

            CombatLogger.setLoggingMode(LoggingMode.BOTH);
            super.gereOrdreExecutionActions(actionLocal, actionDistant);
        }
        this.finDePartie();
    }

    @Override
    public Object gereChoixAction(Joueur joueur) {
        afficherTitre("Tour de " + joueur.getNomJoueur());

        Monstre actif = joueur.getMonstreActuel();
        String infoMonstre = "Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse();
        afficherMessage(infoMonstre);

        CombatLogger.afficherActionsDisponibles();

        String choixAction = demanderSaisie("Votre choix >");
        while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
            afficherErreur("Saisie invalide. Merci de choisir 1, 2 ou 3.");
            choixAction = demanderSaisie("Votre choix >");
        }

        switch (choixAction) {
            case "1":
                return choixAttaque(joueur);
            case "2":
                return utiliseObjet(joueur);
            case "3":
                return changeMonstre(joueur);
            default:
                return null;
        }
    }

    // ========================================
    // Méthodes utilitaires spécifiques au combat en ligne
    // ========================================

    private void afficherTitrePourTous(String titre) {
        CombatLogger.setLoggingMode(LoggingMode.BOTH);
        CombatLogger.logTitre(titre);
    }


    @Override
    public void finDePartie() {
        Joueur gagnant;
        if (joueur1.sontMonstresMorts()) {
            gagnant = joueur2;
        } else {
            gagnant = joueur1;
        }

        // Activer le mode BOTH pour envoyer les logs au terminal local et au client
        CombatLogger.setLoggingMode(LoggingMode.BOTH);
        CombatLogger.log("");
        CombatLogger.log("========================================");
        CombatLogger.log("  VICTOIRE DE " + gagnant.getNomJoueur().toUpperCase() + " !");
        CombatLogger.log("========================================");
        CombatLogger.log("");

        // Message de fin uniquement pour le client
        connection.sendEnd("Fin de la partie. Merci d'avoir joue.");
    }
}
