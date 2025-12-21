package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.App;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.views.BotGameView;

/**
 * Controller pour le mode de jeu contre le bot.
 */
public class BotGameController {
    
    private BotGameView view;
    private NavigationCallback navigationCallback;
    
    public BotGameController(BotGameView view, NavigationCallback navigationCallback) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnBackToMenu().setOnAction(e -> handleBackToMenu());
        view.getBtnStartGame().setOnAction(e -> handleStartGame());
    }
    
    /**
     * Gère le clic sur le bouton "Revenir au menu".
     */
    private void handleBackToMenu() {
        navigationCallback.showMainMenu();
    }
    
    /**
     * Gère le clic sur le bouton "Commencer le jeu".
     */
    private void handleStartGame() {
        String playerName = view.getTxtPlayerName().getText().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Erreur : Le nom du joueur doit être rempli");
            return;
        }

        Joueur joueur = new Joueur(playerName);

        Bot bot = new Bot("Bot");
        bot.chargerMonstresAutomatiquement(App.monstreLoader);
        bot.chargerAttaquesAutomatiquement(App.attaqueLoader);

        System.out.println("Démarrage du jeu local - Joueur 1: " + playerName + ", Robot: " + bot.getNomJoueur());
        navigationCallback.showMonsterSelectionBotGame(joueur, bot);
    }
}
