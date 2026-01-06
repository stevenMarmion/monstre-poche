package com.esiea.monstre.poche.controllers.battle;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.views.gui.battle.WinnerView;

/**
 * Controller pour la sélection des attaques.
 */
public class WinnerController {
    
    private WinnerView view;
    private INavigationCallback INavigationCallback;
    
    public WinnerController(WinnerView view, INavigationCallback INavigationCallback) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
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
        INavigationCallback.showMainMenu();
    }
}
