package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.views.WinnerView;

/**
 * Controller pour la sélection des attaques.
 */
public class WinnerController {
    
    private WinnerView view;
    private NavigationCallback navigationCallback;
    
    public WinnerController(WinnerView view, NavigationCallback navigationCallback) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnBackToMenu().setOnAction(e -> handleBackToMenu());
    }
    
    /**
     * Gère le clic sur le bouton "Revenir au menu".
     */
    private void handleBackToMenu() {
        navigationCallback.showMainMenu();
    }
}
