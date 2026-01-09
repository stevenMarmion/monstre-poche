package com.esiea.monstre.poche.models.battle.modes;

import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.Combat;
import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.items.Objet;

/**
 * Combat pour deux joueurs locaux via terminal.
 * Implémente uniquement la stratégie d'I/O avec Scanner.
 * Toute la logique métier est héritée de Combat.
 */
public class CombatLocalTerminal extends Combat {
    private final Scanner scanner = new Scanner(System.in);

    public CombatLocalTerminal(Joueur joueur1, Joueur joueur2) {
        super(joueur1, joueur2);
    }

    // ========================================
    // Implémentation des hook methods (stratégie I/O)
    // ========================================

    @Override
    protected void afficherMessage(String message) {
        CombatLogger.info(message);
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
    // Méthodes spécifiques au combat local
    // ========================================

    @Override
    public void lancer() {
        this.selectionnerMonstre(joueur1);
        this.selectionnerMonstre(joueur2);

        this.selectionnerAttaque(joueur1);
        this.selectionnerAttaque(joueur2);

        this.selectionnerObjet(joueur1);
        this.selectionnerObjet(joueur2);

        this.afficherTitre("COMBAT LANCE !");
        executerTour();
    }

    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            Object actionJoueur1 = gereChoixAction(joueur1);
            Object actionJoueur2 = gereChoixAction(joueur2);
            this.gereOrdreExecutionActions(actionJoueur1, actionJoueur2);
        }
        super.finDePartie();
    }

    @Override
    public Object gereChoixAction(Joueur joueur) {
        this.afficherTitre("Tour de " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        this.afficherMessage("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());
        
        CombatLogger.afficherActionsDisponibles();

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
}
