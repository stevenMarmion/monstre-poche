package com.esiea.monstre.poche.views.gui.battle;

import com.esiea.monstre.poche.models.core.Joueur;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour afficher le gagnant de la partie.
 */
public class WinnerView extends VBox {
    
    private Button btnBackToMenu;
    
    public WinnerView(Joueur winner) {
        this.btnBackToMenu = new Button("Retour au menu principal");
        initializeView(winner);
    }
    
    /**
     * Initialise la vue du gagnant.
     */
    private void initializeView(Joueur winner) {
        // Configuration du conteneur principal
        this.setSpacing(40);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(60));
        this.getStyleClass().add("main-container");
        
        // Titre de victoire
        Label lblTitle = new Label("VICTOIRE !");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 72));
        lblTitle.getStyleClass().add("main-title");
        
        // Nom du gagnant
        Label lblWinner = new Label(winner.getNomJoueur());
        lblWinner.setFont(Font.font("System", FontWeight.BOLD, 48));
        lblWinner.getStyleClass().add("battle-log");
        
        // Message de félicitations
        Label lblMessage = new Label("Félicitations ! Vous avez remporté la victoire !");
        lblMessage.setFont(Font.font("System", 24));
        lblMessage.getStyleClass().add("label-text");
        
        // Bouton retour au menu
        btnBackToMenu.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnBackToMenu.setPrefWidth(300);
        btnBackToMenu.setPrefHeight(60);
        btnBackToMenu.getStyleClass().add("menu-button");
        
        // Ajout des éléments
        this.getChildren().addAll(lblTitle, lblWinner, lblMessage, btnBackToMenu);
    }
    
    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }
}
