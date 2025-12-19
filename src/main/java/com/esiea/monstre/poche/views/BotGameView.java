package com.esiea.monstre.poche.views;

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
 * Vue pour le mode de jeu contre le bot.
 */
public class BotGameView extends VBox {
    
    private TextField txtPlayerName;
    private Button btnStartGame;
    private Button btnBackToMenu;
    
    public BotGameView() {
        initializeView();
    }
    
    /**
     * Initialise la vue du jeu contre le bot.
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
        Label title = new Label("Jouer contre le bot");
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.getStyleClass().add("main-title");
        
        // Conteneur pour le champ de saisie
        VBox inputBox = new VBox(20);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));
        
        // Champ pour le joueur
        Label lblPlayer = new Label("Nom du Joueur :");
        lblPlayer.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPlayer.getStyleClass().add("label-text");
        
        txtPlayerName = createTextField("Entrez votre nom");
        
        inputBox.getChildren().addAll(lblPlayer, txtPlayerName);
        
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
    public TextField getTxtPlayerName() {
        return txtPlayerName;
    }
    
    public Button getBtnStartGame() {
        return btnStartGame;
    }
    
    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }
}
