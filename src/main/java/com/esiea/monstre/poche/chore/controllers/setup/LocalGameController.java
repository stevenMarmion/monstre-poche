package com.esiea.monstre.poche.chore.controllers.setup;

import com.esiea.monstre.poche.chore.controllers.INavigationCallback;
import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Joueur;
import com.esiea.monstre.poche.chore.views.gui.setup.LocalGameView;

/**
 * Controller pour le mode de jeu local.
 */
public class LocalGameController {
    
    private LocalGameView view;
    private INavigationCallback INavigationCallback;
    
    public LocalGameController(LocalGameView view, INavigationCallback INavigationCallback) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
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
        INavigationCallback.showMainMenu();
    }
    
    /**
     * Gère le clic sur le bouton "Commencer le jeu".
     */
    private void handleStartGame() {
        String player1Name = view.getFirstPlayerName().getText().trim();
        String player2Name = view.getSecondPlayerName().getText().trim();
        
        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            CombatLogger.error("Les deux noms doivent être remplis");
            return;
        }

        Joueur joueur1 = new Joueur(player1Name);
        Joueur joueur2 = new Joueur(player2Name);

        CombatLogger.info("Démarrage du jeu local - " + player1Name + " VS " + player2Name);

        INavigationCallback.showMonsterSelectionPlayer(joueur1, () -> {
            INavigationCallback.showMonsterSelectionPlayer(joueur2, () -> {
                INavigationCallback.showBattle(joueur1, joueur2);
            });
        });
    }
}
