package com.esiea.monstre.poche.models.battle.modes;

import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.Combat;
import com.esiea.monstre.poche.models.battle.ai.Bot;
import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.items.Objet;

/**
 * Classe CombatBot qui gère un combat entre un joueur et un Bot.
 * Hérite de Combat et adapte la logique pour les actions automatiques du Bot.
 * Implémente uniquement la stratégie d'I/O avec Scanner pour le joueur humain.
 */
public class CombatBot extends Combat {
    private Bot bot;
    private final Scanner scanner = new Scanner(System.in);

    public CombatBot(Joueur joueur, Bot bot) {
        super(joueur, bot);
        this.bot = bot;
    }

    // ========================================
    // Implémentation des hook methods (stratégie I/O)
    // ========================================

    @Override
    protected void afficherMessage(String message) {
        CombatLogger.log(message);
    }

    @Override
    protected void afficherTitre(String titre) {
        CombatLogger.logTitre(titre);
    }

    @Override
    protected void afficherSousTitre(String sousTitre) {
        CombatLogger.logSousTitre(sousTitre);
    }

    @Override
    protected void afficherErreur(String erreur) {
        CombatLogger.error(erreur);
    }

    @Override
    protected String demanderSaisie(String prompt) {
        return GameVisual.demanderSaisie(this.scanner, prompt);
    }

    // ========================================
    // Méthodes spécifiques au combat contre Bot
    // ========================================

    /**
     * Lance le combat avec sélection automatique pour le Bot
     */
    @Override
    public void lancer() {
        this.selectionnerMonstre(super.joueur1);
        this.selectionnerAttaque(super.joueur1);
        this.selectionnerObjet(super.joueur1);

        this.afficherTitre("COMBAT LANCE !");
        this.afficherMessage("Le Bot " + bot.getNomJoueur() + " est pret au combat.");

        this.executerTour();
    }

    /**
     * Exécute les tours de combat avec gestion des actions du Bot
     */
    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            Object actionJoueur = this.gereChoixAction(joueur1);
            Object actionBot = this.gereChoixActionBot();
            this.gereOrdreExecutionActions(actionJoueur, actionBot);
        }
        this.finDePartie();
    }

    /**
     * Gère l'action automatique du Bot
     */
    private Object gereChoixActionBot() {
        this.afficherTitre("Tour du Bot " + bot.getNomJoueur());

        Monstre monstreActifBot = bot.getMonstreActuel();
        Monstre monstreActifJoueur = joueur1.getMonstreActuel();

        if (monstreActifBot == null || monstreActifBot.getPointsDeVie() <= 0) {
            bot.changerMonstreAutomatiquement();
            return bot.getMonstreActuel();
        }

        this.afficherMessage("Monstre actif du Bot : " + monstreActifBot.getNomMonstre() + " | PV " + (int) monstreActifBot.getPointsDeVie() + "/" + (int) monstreActifBot.getPointsDeVieMax());
        Attaque attaqueBot = bot.choisirActionAutomatiquement(monstreActifJoueur);

        if (attaqueBot != null) {
            this.afficherMessage("Le Bot choisit l'attaque : " + attaqueBot.getNomAttaque());
        }

        return attaqueBot;
    }

    /**
     * Affiche l'interface de sélection d'action pour le joueur humain
     */
    @Override
    public Object gereChoixAction(Joueur joueur) {
        this.afficherTitre("Tour de " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        this.afficherMessage("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());

        // Afficher les monstres de l'adversaire (Bot)
        this.afficherSousTitre("Monstres du Bot adverse :");
        for (Monstre m : bot.getMonstres()) {
            String statut = m.getPointsDeVie() > 0 ? "Vivant" : "KO";
            this.afficherMessage("  - " + m.getNomMonstre() + " | PV " + (int) m.getPointsDeVie() + "/" + (int) m.getPointsDeVieMax() + " | " + statut);
        }

        this.afficherMessage("\nActions disponibles :");
        this.afficherMessage("  1) Attaquer");
        this.afficherMessage("  2) Utiliser un objet");
        this.afficherMessage("  3) Changer de monstre");

        String choixAction = this.demanderSaisie("Votre choix >");
        while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
            this.afficherErreur("Saisie invalide. Merci de choisir 1, 2 ou 3.");
            choixAction = this.demanderSaisie("Votre choix >");
        }

        Object actionEffectuee = null;
        switch (choixAction) {
            case "1":
                Attaque attaqueChoisie = this.choixAttaque(joueur);
                actionEffectuee = attaqueChoisie;
                break;
            case "2":
                Objet objetChoisi = this.utiliseObjet(joueur);
                actionEffectuee = objetChoisi;
                break;
            case "3":
                Monstre monstreChoisi = this.changeMonstre(joueur);
                actionEffectuee = monstreChoisi;
                break;
            default:
                break;
        }

        return actionEffectuee;
    }

    /**
     * Fin du combat avec message personnalisé
     */
    @Override
    public void finDePartie() {
        if (this.getAWinner() != null) {
            this.afficherTitre("FIN DU COMBAT");
            this.afficherMessage("Le gagnant est : " + this.getAWinner().getNomJoueur() + " !");
        }
    }
}
