package com.esiea.monstre.poche.controllers.menu;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.views.gui.menu.MainMenuView;

/**
 * Controller pour le menu principal.
 */
public class MainMenuController {
    
    private MainMenuView view;
    private INavigationCallback INavigationCallback;
    
    public MainMenuController(MainMenuView view, INavigationCallback INavigationCallback) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
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
        CombatLogger.debug("Navigation vers le mode local");
        INavigationCallback.showLocalGameSetup();
    }
    
    /**
     * Gère le clic sur le bouton "Jouer contre un bot".
     */
    private void handleBotGameClick() {
        CombatLogger.debug("Navigation vers le mode bot");
        INavigationCallback.showBotGameSetup();
    }
    
    /**
     * Gère le clic sur le bouton "Jouer en ligne".
     */
    private void handleOnlineGameClick() {
        CombatLogger.debug("Navigation vers le mode en ligne");
        INavigationCallback.showOnlineGameSetup();
    }
}

