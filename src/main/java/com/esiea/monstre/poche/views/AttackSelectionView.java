package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.utils.Loaders;

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

/**
 * Vue pour la sélection des attaques pour un monstre.
 */
public class AttackSelectionView extends VBox {
    
    private Label lblPlayerName;
    private Label lblMonsterName;
    private Button btnValidate;
    private Button btnBackToMenu;
    private VBox attackListContainer;
    private Map<CheckBox, Attaque> attackCheckBoxMap;
    private int maxAttacksToSelect = 4;
    
    public AttackSelectionView(Joueur joueur) {
        this.attackCheckBoxMap = new HashMap<>();
        initializeView(joueur);
    }
    
    /**
     * Initialise la vue de sélection des attaques.
     */
    private void initializeView(Joueur joueur) {
        Monstre currentMonstre = joueur.getMonstreActuel();

        // Filtrer les attaques par type du monstre
        List<Attaque> filteredAttacks = new ArrayList<>();
        for (Attaque attaque : Loaders.attaqueLoader.getRessources()) {
            if (attaque.getTypeAttaque().getLabelType().equals(currentMonstre.getTypeMonstre().getLabelType())) {
                filteredAttacks.add(attaque);
            }
        }
        
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
        lblPlayerName = new Label(joueur.getNomJoueur() + " - Sélectionner les attaques");
        lblPlayerName.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblPlayerName.getStyleClass().add("main-title");
        
        // Nom du monstre
        lblMonsterName = new Label("Monstre : " + currentMonstre.getNomMonstre() + " (" + currentMonstre.getTypeMonstre().getLabelType() + ")");
        lblMonsterName.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblMonsterName.getStyleClass().add("label-text");
        
        // Instructions
        Label instructions = new Label("Sélectionnez " + maxAttacksToSelect + " attaques pour ce monstre :");
        instructions.setFont(Font.font("System", FontWeight.BOLD, 16));
        instructions.getStyleClass().add("label-text");
        
        // Container pour la liste des attaques avec scroll
        attackListContainer = new VBox(15);
        attackListContainer.setAlignment(Pos.CENTER);
        attackListContainer.setPadding(new Insets(20));
        
        // Ajout des attaques filtrées (même type que le monstre)
        for (Attaque attaque : filteredAttacks) {
            HBox attackBox = createAttackBox(attaque);
            attackListContainer.getChildren().add(attackBox);
        }
        
        // ScrollPane pour les attaques
        ScrollPane scrollPane = new ScrollPane(attackListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.getStyleClass().add("scroll-pane");
        
        // Bouton de validation
        btnValidate = new Button("Valider la sélection");
        btnValidate.setFont(Font.font("System", FontWeight.BOLD, 18));
        btnValidate.setPrefWidth(300);
        btnValidate.getStyleClass().add("menu-button");
        btnValidate.setDisable(true); // Désactivé jusqu'à la sélection de 4 attaques
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, lblPlayerName, lblMonsterName, instructions, scrollPane, btnValidate);
    }
    
    /**
     * Crée une boîte pour une attaque avec ses informations et une checkbox.
     */
    private HBox createAttackBox(Attaque attaque) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("attack-box");
        
        // Checkbox pour sélectionner l'attaque
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("attack-checkbox");
        checkBox.setOnAction(e -> updateValidateButton());
        
        // Informations de l'attaque
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(attaque.getNomAttaque() + " (" + attaque.getTypeAttaque().getLabelType() + ")");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.getStyleClass().add("attack-name");
        
        Label statsLabel = new Label(String.format(
            "Puissance: %d | Utilisations: %d | Échec: %.0f%%",
            attaque.getPuissanceAttaque(),
            attaque.getNbUtilisations(),
            attaque.getProbabiliteEchec() * 100
        ));
        statsLabel.setFont(Font.font("System", 14));
        statsLabel.getStyleClass().add("attack-stats");
        
        infoBox.getChildren().addAll(nameLabel, statsLabel);
        box.getChildren().addAll(checkBox, infoBox);
        
        // Association de l'attaque avec la checkbox
        attackCheckBoxMap.put(checkBox, attaque);
        
        return box;
    }
    
    /**
     * Met à jour l'état du bouton de validation selon le nombre d'attaques sélectionnées.
     */
    private void updateValidateButton() {
        int selectedCount = (int) attackCheckBoxMap.keySet().stream()
                .filter(CheckBox::isSelected)
                .count();
        
        btnValidate.setDisable(selectedCount != maxAttacksToSelect);
        
        // Désactiver les checkboxes non sélectionnées si on a atteint le max
        if (selectedCount >= maxAttacksToSelect) {
            attackCheckBoxMap.keySet().forEach(cb -> {
                if (!cb.isSelected()) {
                    cb.setDisable(true);
                }
            });
        } else {
            attackCheckBoxMap.keySet().forEach(cb -> cb.setDisable(false));
        }
    }
    
    /**
     * Retourne la liste des attaques sélectionnées.
     */
    public List<Attaque> getSelectedAttacks() {
        List<Attaque> selected = new ArrayList<>();
        attackCheckBoxMap.forEach((checkBox, attaque) -> {
            if (checkBox.isSelected()) {
                selected.add(attaque);
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
