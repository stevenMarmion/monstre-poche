package com.esiea.monstre.poche.combats;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.bot.Bot;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.inventaire.Objet;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;
import com.esiea.monstre.poche.visual.GameVisual;

/**
 * Classe CombatBot qui gère un combat entre un joueur et un Bot.
 * Hérite de Combat et adapte la logique pour les actions automatiques du Bot.
 */
public class CombatBot extends Combat {
    private Bot bot;
    private final Scanner scanner = new Scanner(System.in);

    public CombatBot(Joueur joueur, Bot bot, Terrain terrain) {
        super(joueur, bot, terrain);
        this.bot = bot;
    }

    /**
     * Lance le combat avec sélection automatique pour le Bot
     */
    @Override
    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        // Le joueur humain sélectionne ses monstres et attaques
        this.selectionnerMonstre(monstreLoader, joueur1);
        this.selectionnerAttaque(attaqueLoader, joueur1);

        // Le Bot charge automatiquement (déjà fait dans MonstrePoche, mais par sécurité)
        if (bot.getMonstres().isEmpty()) {
            bot.chargerMonstresAutomatiquement(monstreLoader);
            bot.chargerAttaquesAutomatiquement(attaqueLoader);
        }

        GameVisual.afficherTitreSection("COMBAT LANCE !");
        System.out.println("Le Bot " + bot.getNomJoueur() + " est pret au combat.");
        System.out.println();

        // Exécuter les tours de combat
        this.executerTour();
    }

    /**
     * Exécute les tours de combat avec gestion des actions du Bot
     */
    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            // Le joueur humain choisit son action
            Object actionJoueur = this.gereChoixAction(joueur1);

            // Le Bot choisit automatiquement son action
            Object actionBot = this.gereChoixActionBot();

            // Exécuter les actions
            Combat.gereOrdreExecutionActions(actionJoueur, actionBot);
        }
        this.finDePartie();
    }

    /**
     * Gère l'action automatique du Bot
     */
    private Object gereChoixActionBot() {
        GameVisual.afficherTitreSection("Tour du Bot " + bot.getNomJoueur());

        Monstre monstreActifBot = bot.getMonstreActuel();
        Monstre monstreActifJoueur = joueur1.getMonstreActuel();

        if (monstreActifBot == null || monstreActifBot.getPointsDeVie() <= 0) {
            bot.changerMonstreAutomatiquement();
            return bot.getMonstreActuel();
        }

        System.out.println("Monstre actif du Bot : " + monstreActifBot.getNomMonstre() + " | PV " + (int) monstreActifBot.getPointsDeVie() + "/" + (int) monstreActifBot.getPointsDeVieMax());

        // Le Bot choisit une attaque
        Attaque attaqueBot = bot.choisirActionAutomatiquement(monstreActifJoueur);

        if (attaqueBot != null) {
            System.out.println("Le Bot choisit l'attaque : " + attaqueBot.getNomAttaque());
        }

        return attaqueBot;
    }

    /**
     * Affiche l'interface de sélection d'action pour le joueur humain
     */
    @Override
    public Object gereChoixAction(Joueur joueur) {
        GameVisual.afficherTitreSection("Tour de " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        System.out.println("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());

        // Afficher les monstres de l'adversaire (Bot)
        GameVisual.afficherSousTitre("Monstres du Bot adverse :");
        for (Monstre m : bot.getMonstres()) {
            String statut = m.getPointsDeVie() > 0 ? "Vivant" : "KO";
            System.out.println("  - " + m.getNomMonstre() + " | PV " + (int) m.getPointsDeVie() + "/" + (int) m.getPointsDeVieMax() + " | " + statut);
        }

        System.out.println("\nActions disponibles :");
        System.out.println("  1) Attaquer");
        System.out.println("  2) Utiliser un objet");
        System.out.println("  3) Changer de monstre");

        String choixAction = GameVisual.demanderSaisie(this.scanner, "Votre choix >");
        while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
            GameVisual.afficherErreur("Saisie invalide. Merci de choisir 1, 2 ou 3.");
            choixAction = GameVisual.demanderSaisie(this.scanner, "Votre choix >");
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
        GameVisual.afficherTitreSection("FIN DU COMBAT");
        if (joueur1.sontMonstresMorts()) {
            System.out.println("Vous avez perdu ! Le Bot " + bot.getNomJoueur() + " a remporte la victoire !");
        } else if (joueur2.sontMonstresMorts()) {
            System.out.println("Felicitations ! Vous avez remporte la victoire contre le Bot " + bot.getNomJoueur() + " !");
        }
    }
}
