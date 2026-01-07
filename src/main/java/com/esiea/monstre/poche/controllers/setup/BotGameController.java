package com.esiea.monstre.poche.controllers.setup;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.battle.ai.Bot;
import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.game.resources.GameResourcesLoader;
import com.esiea.monstre.poche.views.gui.setup.BotGameView;

/**
 * Controller pour le mode de jeu contre le bot.
 */
public class BotGameController {
    
    private BotGameView view;
    private INavigationCallback INavigationCallback;

    // TODO gameresources factory passe, mais  plusieurs instances définies dans l'app
    // ca devrait etre un singleton
    private final GameResourcesLoader resourcesLoader = new GameResourcesLoader();
    private final GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);
    
    public BotGameController(BotGameView view, INavigationCallback INavigationCallback) {
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
        String playerName = view.getTxtPlayerName().getText().trim();
        
        if (playerName.isEmpty()) {
            CombatLogger.error("Le nom du joueur doit être rempli");
            return;
        }

        Joueur joueur = new Joueur(playerName);

        Bot bot = new Bot("Bot");
        bot.chargerMonstresAutomatiquement(this.resourcesFactory);
        bot.chargerAttaquesAutomatiquement(this.resourcesFactory);

        CombatLogger.info("Démarrage du jeu - " + playerName + " VS " + bot.getNomJoueur());
        INavigationCallback.showMonsterSelectionPlayer(joueur, () -> {
            INavigationCallback.showBattle(joueur, bot);
        });
    }
}
