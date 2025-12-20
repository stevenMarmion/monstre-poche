package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.AppTerminal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.controllers.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale de l'application Monstre Poche.
 * Point d'entrée de l'interface graphique JavaFX.
 * Implémente NavigationCallback pour la gestion de la navigation entre les vues.
 */
public class MonstrePocheUI extends Application implements NavigationCallback {
    
    private Stage primaryStage;
    private Scene scene;
    
    private int currentMonsterIndex;
    private String currentPlayerName;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        AppTerminal.chargeLoaders();
        
        // Affichage du menu principal
        showMainMenu();
        
        // Configuration de la fenêtre
        primaryStage.setTitle("Monstre Poche");
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    @Override
    public void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        new MainMenuController(mainMenuView, this);
        
        scene = new Scene(mainMenuView, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
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
    public void showMonsterSelection(Joueur joueur1, Joueur joueur2, boolean isPlayer1) {
        if (isPlayer1) {
            currentPlayerName = joueur1.getNomJoueur();
        } else {
            currentPlayerName = joueur2.getNomJoueur();
        }
        
        List<Monstre> availableMonsters = AppTerminal.monstreLoader.getRessources();
        
        MonsterSelectionView monsterSelectionView = new MonsterSelectionView(currentPlayerName, availableMonsters);
        new MonsterSelectionController(monsterSelectionView, this, joueur1, joueur2, isPlayer1);
        
        // Gestionnaire du bouton valider
        monsterSelectionView.getBtnValidate().setOnAction(e -> {
            List<Monstre> selectedMonsters = monsterSelectionView.getSelectedMonsters();
            
            if (isPlayer1) {
                joueur1.setMonstres(selectedMonsters);
                joueur1.setMonstreActuel(selectedMonsters.get(0));
            } else {
                joueur2.setMonstres(selectedMonsters);
                joueur2.setMonstreActuel(selectedMonsters.get(0));
            }
            
            currentMonsterIndex = 0;
            showAttackSelection(joueur1, joueur2, isPlayer1);
        });
        
        scene.setRoot(monsterSelectionView);
    }
    
    @Override
    public void showAttackSelection(Joueur joueur1, Joueur joueur2, boolean isPlayer1) {
        Joueur activePlayer = isPlayer1 ? joueur1 : joueur2;

        if (currentMonsterIndex >= activePlayer.getMonstres().size()) {
            if (isPlayer1) {
                System.out.println("Attaques sélectionnées pour " + joueur1.getNomJoueur() + ", au tour de " + joueur2.getNomJoueur());
                showMonsterSelection(joueur1, joueur2, false);
            } else {
                System.out.println("Sélection terminée pour les deux joueurs ! Le jeu peut commencer.");
                showBattle(joueur1, joueur2);
            }
            return;
        }

        Monstre currentMonstre = activePlayer.getMonstres().get(currentMonsterIndex);
        List<Attaque> availableAttacks = AppTerminal.attaqueLoader.getRessources();
        
        AttackSelectionView attackSelectionView = new AttackSelectionView(currentPlayerName, currentMonstre, availableAttacks);
        
        // Gestionnaire du bouton retour
        attackSelectionView.getBtnBackToMenu().setOnAction(e -> showMainMenu());
        
        // Gestionnaire du bouton valider
        attackSelectionView.getBtnValidate().setOnAction(e -> {
            List<Attaque> selectedAttacks = attackSelectionView.getSelectedAttacks();
            currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));
            
            System.out.println(currentPlayerName + " a sélectionné " + selectedAttacks.size() + " attaques pour " + currentMonstre.getNomMonstre());
            
            currentMonsterIndex++;
            showAttackSelection(joueur1, joueur2, isPlayer1);
        });
        
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
    public void showMonsterSelectionBotGame(Joueur joueur1, Joueur bot) {
        currentPlayerName = joueur1.getNomJoueur();
        List<Monstre> availableMonsters = AppTerminal.monstreLoader.getRessources();
        
        MonsterSelectionView monsterSelectionView = new MonsterSelectionView(currentPlayerName, availableMonsters);
        new MonsterSelectionController(monsterSelectionView, this, joueur1, bot, true);
        
        // Gestionnaire du bouton valider
        monsterSelectionView.getBtnValidate().setOnAction(e -> {
            List<Monstre> selectedMonsters = monsterSelectionView.getSelectedMonsters();
            
            joueur1.setMonstres(selectedMonsters);
            joueur1.setMonstreActuel(selectedMonsters.get(0));
            
            currentMonsterIndex = 0;
            showAttackSelectionBotGame(joueur1, bot);
        });
        
        scene.setRoot(monsterSelectionView);
    }

    @Override
    public void showAttackSelectionBotGame(Joueur joueur1, Joueur bot) {
        Joueur activePlayer = joueur1;

        if (currentMonsterIndex >= activePlayer.getMonstres().size()) {
            showBattle(joueur1, bot);
            return;
        }

        Monstre currentMonstre = activePlayer.getMonstres().get(currentMonsterIndex);
        List<Attaque> availableAttacks = AppTerminal.attaqueLoader.getRessources();
        
        AttackSelectionView attackSelectionView = new AttackSelectionView(currentPlayerName, currentMonstre, availableAttacks);
        
        // Gestionnaire du bouton retour
        attackSelectionView.getBtnBackToMenu().setOnAction(e -> showMainMenu());
        
        // Gestionnaire du bouton valider
        attackSelectionView.getBtnValidate().setOnAction(e -> {
            List<Attaque> selectedAttacks = attackSelectionView.getSelectedAttacks();
            currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));
            
            System.out.println(currentPlayerName + " a sélectionné " + selectedAttacks.size() + " attaques pour " + currentMonstre.getNomMonstre());
            
            currentMonsterIndex++;
            showAttackSelectionBotGame(joueur1, bot);
        });
        
        scene.setRoot(attackSelectionView);
    }

    public static void main(String[] args) {
        launch();
    }
}
