package com.esiea.monstre.pochebis.views.gui.selection;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esiea.monstre.pochebis.models.entites.Joueur;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.utils.Loaders;

/**
 * Vue pour la sélection des monstres.
 */
public class MonsterSelectionView extends VBox {
    
    private Label lblPlayerName;
    private Button btnValidate;
    private Button btnBackToMenu;
    private VBox monsterListContainer;
    private Map<CheckBox, Monstre> monsterCheckBoxMap;
    private int maxMonstersToSelect = 3;
    
    public MonsterSelectionView(Joueur joueur) {
        this.monsterCheckBoxMap = new HashMap<>();
        initializeView(joueur);
    }
    
    /**
     * Initialise la vue de sélection des monstres.
     */
    private void initializeView(Joueur joueur) {
        // Configuration du conteneur principal
        this.setSpacing(20);
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
        
        // Titre avec le nom du joueur
        lblPlayerName = new Label(joueur.getNomJoueur() + " - Sélectionner vos monstres");
        lblPlayerName.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblPlayerName.getStyleClass().add("main-title");
        
        // Instructions
        Label instructions = new Label("Sélectionnez " + maxMonstersToSelect + " monstres pour votre équipe :");
        instructions.setFont(Font.font("System", FontWeight.BOLD, 16));
        instructions.getStyleClass().add("label-text");
        
        // Container pour la liste des monstres avec scroll
        monsterListContainer = new VBox(15);
        monsterListContainer.setAlignment(Pos.CENTER);
        monsterListContainer.setPadding(new Insets(20));
        
        // Ajout des monstres disponibles
        for (Monstre monstre : Loaders.monstreLoader.getRessources()) {
            HBox monsterBox = createMonsterBox(monstre);
            monsterListContainer.getChildren().add(monsterBox);
        }
        
        // ScrollPane pour les monstres
        ScrollPane scrollPane = new ScrollPane(monsterListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.getStyleClass().add("scroll-pane");
        
        // Bouton de validation
        btnValidate = new Button("Valider la sélection");
        btnValidate.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnValidate.setPrefWidth(300);
        btnValidate.getStyleClass().add("menu-button");
        btnValidate.setDisable(true); // Désactivé jusqu'à la sélection de 3 monstres
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, lblPlayerName, instructions, scrollPane, btnValidate);
    }
    
    /**
     * Crée une boîte pour un monstre avec ses informations et une checkbox.
     */
    private HBox createMonsterBox(Monstre monstre) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("monster-box");
        
        // Checkbox pour sélectionner le monstre
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("monster-checkbox");
        checkBox.setOnAction(e -> updateValidateButton());
        
        // Informations du monstre
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(monstre.getNomMonstre() + " (" + monstre.getTypeMonstre().getLabelType() + ")");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.getStyleClass().add("monster-name");
        
        Label statsLabel = new Label(String.format(
            "PV: %f | ATK: %d | DEF: %d | VIT: %d",
            monstre.getPointsDeVieMax(),
            monstre.getAttaque(),
            monstre.getDefense(),
            monstre.getVitesse()
        ));
        statsLabel.setFont(Font.font("System", 14));
        statsLabel.getStyleClass().add("monster-stats");
        
        infoBox.getChildren().addAll(nameLabel, statsLabel);
        box.getChildren().addAll(checkBox, infoBox);
        
        // Association du monstre avec la checkbox
        monsterCheckBoxMap.put(checkBox, monstre);
        
        return box;
    }
    
    /**
     * Met à jour l'état du bouton de validation selon le nombre de monstres sélectionnés.
     */
    private void updateValidateButton() {
        int selectedCount = (int) monsterCheckBoxMap.keySet().stream()
                .filter(CheckBox::isSelected)
                .count();
        
        btnValidate.setDisable(selectedCount != maxMonstersToSelect);
        
        // Désactiver les checkboxes non sélectionnées si on a atteint le max
        if (selectedCount >= maxMonstersToSelect) {
            monsterCheckBoxMap.keySet().forEach(cb -> {
                if (!cb.isSelected()) {
                    cb.setDisable(true);
                }
            });
        } else {
            monsterCheckBoxMap.keySet().forEach(cb -> cb.setDisable(false));
        }
    }
    
    /**
     * Retourne la liste des monstres sélectionnés.
     */
    public List<Monstre> getSelectedMonsters() {
        List<Monstre> selected = new ArrayList<>();
        monsterCheckBoxMap.forEach((checkBox, monstre) -> {
            if (checkBox.isSelected()) {
                selected.add(monstre);
            }
        });
        return selected;
    }
    
    // Getters
    public Button getBtnValidate() {
        return btnValidate;
    }
    
    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }
}
