package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.views.gui.battle.BattleView;
import com.esiea.monstre.poche.views.gui.battle.WinnerView;
import com.esiea.monstre.poche.views.gui.menu.MainMenuView;
import com.esiea.monstre.poche.views.gui.selection.AttackSelectionView;
import com.esiea.monstre.poche.views.gui.selection.MonsterSelectionView;
import com.esiea.monstre.poche.views.gui.setup.BotGameView;
import com.esiea.monstre.poche.views.gui.setup.LocalGameView;
import com.esiea.monstre.poche.views.gui.setup.OnlineGameView;
import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.controllers.batlle.BattleController;
import com.esiea.monstre.poche.controllers.batlle.WinnerController;
import com.esiea.monstre.poche.controllers.menu.MainMenuController;
import com.esiea.monstre.poche.controllers.selection.AttackSelectionController;
import com.esiea.monstre.poche.controllers.selection.MonsterSelectionController;
import com.esiea.monstre.poche.controllers.setup.BotGameController;
import com.esiea.monstre.poche.controllers.setup.LocalGameController;
import com.esiea.monstre.poche.controllers.setup.OnlineGameController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale de l'application Monstre Poche.
 * Point d'entrée de l'interface graphique JavaFX.
 * Implémente INavigationCallback pour la gestion de la navigation entre les vues.
 */
public class MonstrePocheUI extends Application implements INavigationCallback {
    private Stage stage;
    private Scene scene;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        showMainMenu();
        
        stage.setTitle("Monstre Poche");
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }
    
    @Override
    public void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        new MainMenuController(mainMenuView, this);
        
        scene = new Scene(mainMenuView, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
    }
    
    @Override
    public void showLocalGameSetup() {
        LocalGameView localGameView = new LocalGameView();
        new LocalGameController(localGameView, this);
        
        scene.setRoot(localGameView);
    }
    
    @Override
    public void showBotGameSetup() {
        BotGameView botGameView = new BotGameView();
        new BotGameController(botGameView, this);
        
        scene.setRoot(botGameView);
    }
    
    @Override
    public void showOnlineGameSetup() {
        OnlineGameView onlineGameView = new OnlineGameView();
        new OnlineGameController(onlineGameView, this);
        
        scene.setRoot(onlineGameView);
    }
    
    @Override
    public void showMonsterSelectionPlayer(Joueur joueur, Runnable onComplete) {
        MonsterSelectionView monsterSelectionView = new MonsterSelectionView(joueur);
        new MonsterSelectionController(monsterSelectionView, this, joueur, onComplete);

        scene.setRoot(monsterSelectionView);
    }

    @Override
    public void showAttackSelectionPlayer(Joueur joueur, Runnable onComplete) {
        AttackSelectionView attackSelectionView = new AttackSelectionView(joueur);
        new AttackSelectionController(attackSelectionView, this, joueur, onComplete);
        
        scene.setRoot(attackSelectionView);
    }
    
    @Override
    public void showBattle(Joueur joueur1, Joueur joueur2) {
        BattleView battleView = new BattleView(joueur1, joueur2);
        new BattleController(battleView, this);

        scene.setRoot(battleView);
    }

    @Override
    public void showBattleBot(Joueur joueur1, Bot bot) {
        BattleView battleView = new BattleView(joueur1, bot);
        new BattleController(battleView, this);

        scene.setRoot(battleView);
    }

    @Override
    public void showWinnerView(Joueur winner) {
        WinnerView winnerView = new WinnerView(winner);
        new WinnerController(winnerView, this);
        
        scene.setRoot(winnerView);
    }

    @Override
    public void showBattleOnline(BattleView battleView) {
        // La BattleView est deja creee et configuree par OnlineGameController
        scene.setRoot(battleView);
    }
}
