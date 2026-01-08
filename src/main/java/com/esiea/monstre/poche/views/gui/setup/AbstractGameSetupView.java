package com.esiea.monstre.poche.views.gui.setup;

import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Classe mère abstraite pour toutes les vues de configuration de jeu.
 * Factorise le code commun : top bar, titre, création de TextField, etc.
 */
public abstract class AbstractGameSetupView extends VBox {

    protected Button btnBackToMenu;
    protected Button btnStartGame;

    protected abstract String getTitle();
    protected abstract VBox createCustomContent();

    /**
     * Initialise la vue selon le pattern Template Method.
     * Les sous-classes appellent cette méthode dans leur constructeur.
     */
    protected void initializeView() {
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("main-container");

        HBox topBar = createTopBar();
        Label title = createTitle();
        VBox customContent = createCustomContent();

        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, title);
        if (customContent != null) {
            this.getChildren().add(customContent);
        }
    }

    /**
     * Crée la barre supérieure avec le bouton "Revenir au menu".
     */
    protected HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));

        btnBackToMenu = new Button("Revenir au menu");
        btnBackToMenu.setFont(Font.font(FontConfig.SYSTEM.getFontName(), 14));
        btnBackToMenu.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBackToMenu);

        return topBar;
    }

    /**
     * Crée le titre principal de la vue.
     */
    protected Label createTitle() {
        Label title = new Label(getTitle());
        title.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 36));
        title.getStyleClass().add("main-title");
        return title;
    }

    /**
     * Crée un label stylisé pour les champs de formulaire.
     */
    protected Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        label.getStyleClass().add("label-text");
        return label;
    }

    /**
     * Crée un champ de texte stylisé.
     */
    protected TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(300);
        textField.setMaxWidth(300);
        textField.setFont(Font.font(FontConfig.SYSTEM.getFontName(), 14));
        textField.getStyleClass().add("text-field");
        return textField;
    }

    /**
     * Crée un bouton de démarrage de jeu stylisé.
     */
    protected Button createStartGameButton(String text) {
        btnStartGame = new Button(text);
        btnStartGame.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 18));
        btnStartGame.setPrefWidth(300);
        btnStartGame.getStyleClass().add("menu-button");
        return btnStartGame;
    }

    /**
     * Crée un conteneur d'entrées centré.
     */
    protected VBox createInputBox() {
        VBox inputBox = new VBox(20);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));
        return inputBox;
    }

    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }

    public Button getBtnStartGame() {
        return btnStartGame;
    }
}
