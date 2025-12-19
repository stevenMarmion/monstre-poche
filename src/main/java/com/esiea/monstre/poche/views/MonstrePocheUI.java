package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
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
    private int currentMonsterIndex;
    private String currentPlayerName;

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
            player1Name = joueur1.getNomJoueur();
            currentPlayerName = player1Name;
        } else {
            player2Name = joueur2.getNomJoueur();
            currentPlayerName = player2Name;
        }
        
        List<Monstre> availableMonsters = monstreLoader.getRessources();
        
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
            // Tous les monstres ont leurs attaques
            if (isPlayer1) {
                // Passer à la sélection des monstres du joueur 2
                System.out.println("Attaques sélectionnées pour " + player1Name + ", au tour de " + player2Name);
                // Ne pas inverser l'ordre des joueurs: isPlayer1=false cible joueur2
                showMonsterSelection(joueur1, joueur2, false);
            } else {
                // Les deux joueurs ont terminé
                System.out.println("Sélection terminée pour les deux joueurs ! Le jeu peut commencer.");
                showBattle(joueur1, joueur2);
            }
            return;
        }

        Monstre currentMonstre = activePlayer.getMonstres().get(currentMonsterIndex);
        List<Attaque> availableAttacks = attaqueLoader.getRessources();
        
        AttackSelectionView attackSelectionView = new AttackSelectionView(currentPlayerName, currentMonstre, availableAttacks);
        
        // Gestionnaire du bouton retour
        attackSelectionView.getBtnBackToMenu().setOnAction(e -> showMainMenu());
        
        // Gestionnaire du bouton valider
        attackSelectionView.getBtnValidate().setOnAction(e -> {
            List<Attaque> selectedAttacks = attackSelectionView.getSelectedAttacks();
            currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));
            
            System.out.println(currentPlayerName + " a sélectionné " + selectedAttacks.size() + 
                             " attaques pour " + currentMonstre.getNomMonstre());
            
            currentMonsterIndex++;
            showAttackSelection(joueur1, joueur2, isPlayer1);
        });
        
        scene.setRoot(attackSelectionView);
    }
    
    @Override
    public void showBattle(Joueur joueur1, Joueur joueur2) {
        // Créer la vue de combat avec les premiers monstres
        BattleView battleView = new BattleView(joueur1, joueur2);
        
        // Créer le contrôleur de combat
        new BattleController(battleView, this);
        
        System.out.println("Combat lancé entre " + player1Name + " et " + player2Name);
        
        scene.setRoot(battleView);
    }

    public static void main(String[] args) {
        launch();
    }
}
