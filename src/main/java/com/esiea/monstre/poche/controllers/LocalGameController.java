package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.views.LocalGameView;

/**
 * Controller pour le mode de jeu local.
 */
public class LocalGameController {
    
    private LocalGameView view;
    private NavigationCallback navigationCallback;
    
    public LocalGameController(LocalGameView view, NavigationCallback navigationCallback) {
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
        String player1Name = view.getTxtPlayer1Name().getText().trim();
        String player2Name = view.getTxtPlayer2Name().getText().trim();
        
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            System.out.println("Erreur : Les deux noms doivent être remplis");
            // TODO: Afficher un message d'erreur à l'utilisateur
            return;
        }
        
        System.out.println("Démarrage du jeu local - Joueur 1: " + player1Name + ", Joueur 2: " + player2Name);
        navigationCallback.showMonsterSelection(player1Name, true);
    }
}
