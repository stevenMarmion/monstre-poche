package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.views.OnlineGameView;

/**
 * Controller pour le mode de jeu en ligne.
 */
public class OnlineGameController {
    
    private OnlineGameView view;
    private NavigationCallback navigationCallback;
    
    public OnlineGameController(OnlineGameView view, NavigationCallback navigationCallback) {
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
        
        System.out.println("Démarrage du jeu en ligne - Joueur: " + playerName);
        // TODO: Implémenter la logique pour lancer le jeu en ligne
    }
}
