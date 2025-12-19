package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.views.MainMenuView;

/**
 * Controller pour le menu principal.
 */
public class MainMenuController {
    
    private MainMenuView view;
    private NavigationCallback navigationCallback;
    
    public MainMenuController(MainMenuView view, NavigationCallback navigationCallback) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnLocalGame().setOnAction(e -> handleLocalGameClick());
        view.getBtnBotGame().setOnAction(e -> handleBotGameClick());
        view.getBtnOnlineGame().setOnAction(e -> handleOnlineGameClick());
    }
    
    /**
     * Gère le clic sur le bouton "Jouer en local".
     */
    private void handleLocalGameClick() {
        System.out.println("Navigation vers le mode local");
        navigationCallback.showLocalGameSetup();
    }
    
    /**
     * Gère le clic sur le bouton "Jouer contre un bot".
     */
    private void handleBotGameClick() {
        System.out.println("Navigation vers le mode bot");
        navigationCallback.showBotGameSetup();
    }
    
    /**
     * Gère le clic sur le bouton "Jouer en ligne".
     */
    private void handleOnlineGameClick() {
        System.out.println("Navigation vers le mode en ligne");
        navigationCallback.showOnlineGameSetup();
    }
}

