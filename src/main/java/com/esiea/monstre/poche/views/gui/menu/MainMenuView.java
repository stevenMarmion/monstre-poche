package com.esiea.monstre.poche.views.gui.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue du menu principal de l'application.
 */
public class MainMenuView extends VBox {
    
    private Button btnLocalGame;
    private Button btnBotGame;
    private Button btnOnlineGame;
    
    public MainMenuView() {
        initializeView();
    }
    
    /**
     * Initialise la vue du menu principal.
     */
    private void initializeView() {
        // Configuration du conteneur principal
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(60));
        this.getStyleClass().add("main-container");
        this.setFillWidth(true);
        
        // Titre principal - Logo Pokémon style
        Label title = new Label("MONSTRE POCHE");
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
        title.getStyleClass().add("main-title");
        
        // Sous-titre
        Label subtitle = new Label("Bienvenue au monde des monstres !");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        subtitle.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 5, 0, 1, 1);");
        
        // Conteneur pour les boutons
        VBox buttonBox = new VBox(25);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setFillWidth(true);
        
        // Bouton "Jouer en local (à deux)"
        btnLocalGame = createMenuButton("Jouer en local (à deux)");
        
        // Bouton "Jouer contre un bot"
        btnBotGame = createMenuButton("Jouer contre un bot");
        
        // Bouton "Jouer en ligne"
        btnOnlineGame = createMenuButton("Jouer en ligne");
        
        // Ajout des boutons au conteneur
        buttonBox.getChildren().addAll(btnLocalGame, btnBotGame, btnOnlineGame);
        
        // Ajout du titre, sous-titre et des boutons au conteneur principal
        this.getChildren().addAll(title, subtitle, buttonBox);
    }
    
    /**
     * Crée un bouton de menu stylisé.
     */
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial Black", FontWeight.BOLD, 20));
        button.setPrefWidth(350);
        button.setMinWidth(350);
        button.setMaxWidth(350);
        button.setPrefHeight(60);
        button.getStyleClass().add("menu-button");
        return button;
    }
    
    // Getters pour accéder aux boutons depuis l'extérieur
    public Button getBtnLocalGame() {
        return btnLocalGame;
    }
    
    public Button getBtnBotGame() {
        return btnBotGame;
    }
    
    public Button getBtnOnlineGame() {
        return btnOnlineGame;
    }
}
