package com.esiea.monstre.pochebis.views.gui.setup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour le mode de jeu local à deux joueurs.
 */
public class LocalGameView extends VBox {
    
    private TextField firstPlayerName;
    private TextField secondPlayerName;
    private Button btnStartGame;
    private Button btnBackToMenu;
    
    public LocalGameView() {
        initializeView();
    }
    
    /**
     * Initialise la vue du jeu local.
     */
    private void initializeView() {
        // Configuration du conteneur principal
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("main-container");
        
        // Bouton "Revenir au menu" en haut à gauche
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));
        
        btnBackToMenu = new Button("Revenir au menu");
        btnBackToMenu.setFont(Font.font("System", 14));
        btnBackToMenu.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBackToMenu);
        
        // Titre
        Label title = new Label("Jouer en local à deux");
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.getStyleClass().add("main-title");
        
        // Conteneur pour les champs de saisie
        VBox inputBox = new VBox(20);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));
        
        // Champ pour le joueur 1
        Label lblPlayer1 = new Label("Nom du Joueur 1 :");
        lblPlayer1.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPlayer1.getStyleClass().add("label-text");
        
        firstPlayerName = createTextField("Entrez le nom du joueur 1");
        
        // Champ pour le joueur 2
        Label lblPlayer2 = new Label("Nom du Joueur 2 :");
        lblPlayer2.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPlayer2.getStyleClass().add("label-text");
        
        secondPlayerName = createTextField("Entrez le nom du joueur 2");
        
        inputBox.getChildren().addAll(lblPlayer1, firstPlayerName, lblPlayer2, secondPlayerName);
        
        // Bouton pour commencer le jeu
        btnStartGame = new Button("Commencer le jeu");
        btnStartGame.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnStartGame.setPrefWidth(300);
        btnStartGame.getStyleClass().add("menu-button");
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, title, inputBox, btnStartGame);
    }
    
    /**
     * Crée un champ de texte stylisé.
     */
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        textField.setFont(Font.font("System", 14));
        textField.getStyleClass().add("text-field");
        return textField;
    }
    
    // Getters
    public TextField getFirstPlayerName() {
        return firstPlayerName;
    }
    
    public TextField getSecondPlayerName() {
        return secondPlayerName;
    }
    
    public Button getBtnStartGame() {
        return btnStartGame;
    }
    
    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }
}
