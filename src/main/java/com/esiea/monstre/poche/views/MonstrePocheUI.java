package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
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
    
    // Loaders pour les monstres et attaques
    private MonstreLoader monstreLoader;
    private AttaqueLoader attaqueLoader;
    
    // Variables pour le flux du jeu local
    private String player1Name;
    private String player2Name;
    private List<Monstre> player1Monsters;
    private List<Monstre> player2Monsters;
    private int currentMonsterIndex;
    private String currentPlayerName;
    private boolean isCurrentlyPlayer1;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        
        // Initialisation des loaders
        monstreLoader = new MonstreLoader("monsters.txt");
        attaqueLoader = new AttaqueLoader("attacks.txt");
        monstreLoader.charger();
        attaqueLoader.charger();
        
        // Affichage du menu principal
        showMainMenu();
        
        // Configuration de la fenêtre
        primaryStage.setTitle("Monstre Poche");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    @Override
    public void showMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        new MainMenuController(mainMenuView, this);
        
        scene = new Scene(mainMenuView, 1000, 700);
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
    public void showMonsterSelection(String playerName, boolean isPlayer1) {
        if (isPlayer1) {
            player1Name = playerName;
            isCurrentlyPlayer1 = true;
        } else {
            player2Name = playerName;
            isCurrentlyPlayer1 = false;
        }
        
        currentPlayerName = playerName;
        List<Monstre> availableMonsters = monstreLoader.getRessources();
        
        MonsterSelectionView monsterSelectionView = new MonsterSelectionView(playerName, availableMonsters);
        new MonsterSelectionController(monsterSelectionView, this, playerName, isPlayer1);
        
        // Gestionnaire du bouton valider
        monsterSelectionView.getBtnValidate().setOnAction(e -> {
            List<Monstre> selectedMonsters = monsterSelectionView.getSelectedMonsters();
            
            if (isPlayer1) {
                player1Monsters = new ArrayList<>(selectedMonsters);
            } else {
                player2Monsters = new ArrayList<>(selectedMonsters);
            }
            
            currentMonsterIndex = 0;
            showAttackSelection(playerName, selectedMonsters, isPlayer1);
        });
        
        scene.setRoot(monsterSelectionView);
    }
    
    @Override
    public void showAttackSelection(String playerName, List<Monstre> monsters, boolean isPlayer1) {
        if (currentMonsterIndex >= monsters.size()) {
            // Tous les monstres ont leurs attaques
            if (isPlayer1) {
                // Passer à la sélection des monstres du joueur 2
                System.out.println("Attaques sélectionnées pour " + player1Name + ", au tour de " + player2Name);
                showMonsterSelection(player2Name, false);
            } else {
                // Les deux joueurs ont terminé
                System.out.println("Sélection terminée pour les deux joueurs ! Le jeu peut commencer.");
                // TODO: Lancer le combat local
                showMainMenu();
            }
            return;
        }
        
        Monstre currentMonstre = monsters.get(currentMonsterIndex);
        List<Attaque> availableAttacks = attaqueLoader.getRessources();
        
        AttackSelectionView attackSelectionView = new AttackSelectionView(playerName, currentMonstre, availableAttacks);
        
        // Gestionnaire du bouton retour
        attackSelectionView.getBtnBackToMenu().setOnAction(e -> showMainMenu());
        
        // Gestionnaire du bouton valider
        attackSelectionView.getBtnValidate().setOnAction(e -> {
            List<Attaque> selectedAttacks = attackSelectionView.getSelectedAttacks();
            currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));
            
            System.out.println(playerName + " a sélectionné " + selectedAttacks.size() + 
                             " attaques pour " + currentMonstre.getNomMonstre());
            
            currentMonsterIndex++;
            showAttackSelection(playerName, monsters, isPlayer1);
        });
        
        scene.setRoot(attackSelectionView);
    }

    public static void main(String[] args) {
        launch();
    }
}
