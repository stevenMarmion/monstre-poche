package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue pour la sélection des monstres - Style Pokémon amélioré.
 */
public class MonsterSelectionView extends VBox {
    
    private Label lblPlayerName;
    private Button btnValidate;
    private Button btnBackToMenu;
    private FlowPane monsterCardsContainer;
    private Map<VBox, Monstre> monsterCardMap;
    private List<VBox> selectedCards;
    private Label selectionCounter;
    
    // Pas d'icônes - on utilise uniquement les couleurs et le texte
    
    public MonsterSelectionView(GameResourcesFactory resourcesFactory, Joueur joueur) {
        this.monsterCardMap = new HashMap<>();
        this.selectedCards = new ArrayList<>();
        initializeView(resourcesFactory, joueur);
    }
    
    /**
     * Initialise la vue de sélection des monstres.
     */
    private void initializeView(GameResourcesFactory resourcesFactory, Joueur joueur) {
        // Configuration du conteneur principal
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(15, 25, 25, 25));
        this.getStyleClass().add("main-container");
        
        HBox topBar = createTopBar(joueur);
        VBox titleBox = createTitleSection(joueur);
        
        // Container pour les cartes de monstres (grille fluide)
        monsterCardsContainer = new FlowPane();
        monsterCardsContainer.setHgap(20);
        monsterCardsContainer.setVgap(20);
        monsterCardsContainer.setAlignment(Pos.CENTER);
        monsterCardsContainer.setPadding(new Insets(20));
        
        // Ajout des monstres disponibles sous forme de cartes
        for (Monstre monstre : resourcesFactory.getTousLesMonstres()) {
            VBox monsterCard = createMonsterCard(monstre);
            monsterCardsContainer.getChildren().add(monsterCard);
        }
        
        // ScrollPane stylisé pour les monstres
        ScrollPane scrollPane = new ScrollPane(monsterCardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(420);
        scrollPane.setMaxHeight(420);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        VBox validationSection = createValidationSection();
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, titleBox, scrollPane, validationSection);
    }
    
    /**
     * Crée la barre supérieure avec le bouton retour et le compteur.
     */
    private HBox createTopBar(Joueur joueur) {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5, 10, 5, 10));
        
        btnBackToMenu = new Button("Menu");
        btnBackToMenu.getStyleClass().add("back-button");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Compteur de sélection stylisé
        selectionCounter = new Label("0 / " + Joueur.TAILLE_EQUIPE_MAX + " sélectionnés");
        selectionCounter.getStyleClass().addAll("label-text", "selection-counter");
        selectionCounter.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 8 15; -fx-background-radius: 20;");
        
        topBar.getChildren().addAll(btnBackToMenu, spacer, selectionCounter);
        return topBar;
    }
    
    /**
     * Crée la section titre avec le nom du joueur.
     */
    private VBox createTitleSection(Joueur joueur) {
        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);
        
        lblPlayerName = new Label(joueur.getNomJoueur());
        lblPlayerName.getStyleClass().add("main-title");
        
        Label subtitle = new Label("Composez votre équipe de combat !");
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setStyle("-fx-font-size: 16px;");
        
        Label instructions = new Label("Cliquez sur " + Joueur.TAILLE_EQUIPE_MAX + " monstres pour les ajouter à votre équipe");
        instructions.getStyleClass().add("label-text");
        instructions.setStyle("-fx-font-size: 14px; -fx-opacity: 0.8;");
        
        titleBox.getChildren().addAll(lblPlayerName, subtitle, instructions);
        return titleBox;
    }
    
    /**
     * Crée la section de validation avec le bouton.
     */
    private VBox createValidationSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(15, 0, 5, 0));
        
        btnValidate = new Button("LANCER LE COMBAT");
        btnValidate.getStyleClass().addAll("menu-button", "validate-button");
        btnValidate.setPrefWidth(350);
        btnValidate.setPrefHeight(50);
        btnValidate.setStyle("-fx-font-size: 20px;");
        btnValidate.setDisable(true);
        
        section.getChildren().add(btnValidate);
        return section;
    }
    
    /**
     * Crée une carte stylisée Pokémon pour un monstre.
     */
    private VBox createMonsterCard(Monstre monstre) {
        String typeLabel = monstre.getTypeMonstre().getLabelType();
        String typeColor = ColorConfig.fromString(typeLabel).getColorCode();
        
        // Carte principale
        VBox card = new VBox(8);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(200);
        card.setPrefHeight(280);
        card.getStyleClass().add("monster-card");
        card.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 35%%); " +
            "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 3;",
            typeColor + "40", typeColor
        ));
        
        // En-tête avec nom et type
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(monstre.getNomMonstre());
        nameLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 3, 0, 0, 1);");
        
        header.getChildren().add(nameLabel);
        
        HBox typeBadge = createTypeBadge(typeLabel, typeColor);
        StackPane avatar = createMonsterAvatar(monstre, typeColor);
        VBox statsSection = createStatsSection(monstre, typeColor);
        
        // Points de vie affichés
        Label hpLabel = new Label(String.format("PV: %.0f", monstre.getPointsDeVieMax()));
        hpLabel.setTextFill(Color.web("#ff6b6b"));
        hpLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 13));
        
        // Indicateur de sélection
        Label selectionIndicator = new Label("SÉLECTIONNÉ");
        selectionIndicator.setVisible(false);
        selectionIndicator.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 5 10; " +
            "-fx-background-radius: 10; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 11px;"
        );
        
        card.getChildren().addAll(header, typeBadge, avatar, hpLabel, statsSection, selectionIndicator);
        
        // Tooltip avec plus de détails
        // Tooltip tooltip = new Tooltip(String.format(
        //     "%s\nType: %s\nPV: %.0f | ATK: %d | DEF: %d | VIT: %d\n\nCliquez pour sélectionner !",
        //     monstre.getNomMonstre(), typeLabel,
        //     monstre.getPointsDeVieMax(), monstre.getAttaque(),
        //     monstre.getDefense(), monstre.getVitesse()
        // ));
        Tooltip tooltip = new Tooltip(GameVisual.formatterMonstre(monstre));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);
        
        card.setOnMouseClicked(e -> handleCardClick(card, monstre, selectionIndicator));
        card.setOnMouseEntered(e -> {
            if (!selectedCards.contains(card)) {
                card.setStyle(card.getStyle() + "-fx-scale-x: 1.03; -fx-scale-y: 1.03;");
                card.setEffect(new DropShadow(20, Color.web(typeColor)));
            }
        });
        card.setOnMouseExited(e -> {
            if (!selectedCards.contains(card)) {
                card.setStyle(String.format(
                    "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 35%%); " +
                    "-fx-background-radius: 15; " +
                    "-fx-border-radius: 15; " +
                    "-fx-border-color: %s; " +
                    "-fx-border-width: 3;",
                    typeColor + "40", typeColor
                ));
                card.setEffect(null);
            }
        });
        
        // Association de la carte avec le monstre
        monsterCardMap.put(card, monstre);
        
        return card;
    }
    
    /**
     * Crée un badge de type stylisé.
     */
    private HBox createTypeBadge(String typeLabel, String typeColor) {
        HBox badge = new HBox(5);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(4, 12, 4, 12));
        badge.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-background-radius: 12; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 4, 0, 0, 2);",
            typeColor
        ));
        
        Label typeText = new Label(typeLabel.toUpperCase());
        typeText.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 11));
        typeText.setTextFill(Color.WHITE);
        
        badge.getChildren().add(typeText);
        return badge;
    }
    
    /**
     * Crée l'avatar du monstre.
     */
    private StackPane createMonsterAvatar(Monstre monstre, String typeColor) {
        StackPane avatarContainer = new StackPane();
        avatarContainer.setPadding(new Insets(10));
        
        // Cercle de fond
        Circle circle = new Circle(45);
        circle.setFill(Color.web(typeColor + "60"));
        circle.setStroke(Color.web(typeColor));
        circle.setStrokeWidth(3);
        
        // Initiales du monstre (2 premières lettres)
        Label initialLabel = new Label(monstre.getNomMonstre().substring(0, Math.min(2, monstre.getNomMonstre().length())).toUpperCase());
        initialLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 24));
        initialLabel.setTextFill(Color.WHITE);
        initialLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 1);");
        
        avatarContainer.getChildren().addAll(circle, initialLabel);
        
        return avatarContainer;
    }
    
    /**
     * Crée la section des statistiques avec barres de progression.
     */
    private VBox createStatsSection(Monstre monstre, String typeColor) {
        VBox stats = new VBox(6);
        stats.setAlignment(Pos.CENTER_LEFT);
        stats.setPadding(new Insets(5));
        
        // Normaliser les stats (max théorique ~150 pour les stats)
        double maxStat = 150.0;
        
        stats.getChildren().addAll(
            createStatBar("ATK", monstre.getAttaque(), maxStat, "#ff6b6b"),
            createStatBar("DEF", monstre.getDefense(), maxStat, "#4dabf7"),
            createStatBar("VIT", monstre.getVitesse(), maxStat, "#69db7c")
        );
        
        return stats;
    }
    
    /**
     * Crée une barre de statistique.
     */
    private HBox createStatBar(String statName, int value, double max, String color) {
        HBox statRow = new HBox(8);
        statRow.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(statName);
        nameLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 10));
        nameLabel.setTextFill(Color.web("#aaa"));
        nameLabel.setPrefWidth(30);
        
        ProgressBar bar = new ProgressBar(value / max);
        bar.setPrefWidth(90);
        bar.setPrefHeight(8);
        bar.setStyle(String.format(
            "-fx-accent: %s; " +
            "-fx-background-color: #333; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5;",
            color
        ));
        
        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 10));
        valueLabel.setTextFill(Color.web(color));
        valueLabel.setPrefWidth(25);
        
        statRow.getChildren().addAll(nameLabel, bar, valueLabel);
        return statRow;
    }
    
    /**
     * Gère le clic sur une carte de monstre.
     */
    private void handleCardClick(VBox card, Monstre monstre, Label selectionIndicator) {
        String typeLabel = monstre.getTypeMonstre().getLabelType();
        String typeColor = ColorConfig.fromString(typeLabel).getColorCode();
        
        if (selectedCards.contains(card)) {
            // Désélectionner
            selectedCards.remove(card);
            selectionIndicator.setVisible(false);
            card.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 35%%); " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: %s; " +
                "-fx-border-width: 3;",
                typeColor + "40", typeColor
            ));
            card.setEffect(null);
            
            // Réactiver toutes les cartes
            monsterCardMap.keySet().forEach(c -> c.setDisable(false));
        } else if (selectedCards.size() < Joueur.TAILLE_EQUIPE_MAX) {
            // Sélectionner
            selectedCards.add(card);
            selectionIndicator.setVisible(true);
            
            // Animation de sélection
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(1.05);
            st.setToY(1.05);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
            
            card.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 35%%); " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #4CAF50; " +
                "-fx-border-width: 4;",
                typeColor + "60"
            ));
            
            Glow glow = new Glow(0.3);
            DropShadow shadow = new DropShadow(15, Color.web("#4CAF50"));
            glow.setInput(shadow);
            card.setEffect(glow);
            
            // Désactiver les autres cartes si on a atteint le max
            if (selectedCards.size() >= Joueur.TAILLE_EQUIPE_MAX) {
                monsterCardMap.keySet().forEach(c -> {
                    if (!selectedCards.contains(c)) {
                        c.setDisable(true);
                        c.setOpacity(0.5);
                    }
                });
            }
        }
        
        updateValidateButton();
    }
    
    /**
     * Met à jour l'état du bouton de validation selon le nombre de monstres sélectionnés.
     */
    private void updateValidateButton() {
        int selectedCount = selectedCards.size();
        
        selectionCounter.setText(selectedCount + " / " + Joueur.TAILLE_EQUIPE_MAX + " sélectionnés");
        
        if (selectedCount == Joueur.TAILLE_EQUIPE_MAX) {
            btnValidate.setDisable(false);
            btnValidate.setText("LANCER LE COMBAT (" + selectedCount + "/" + Joueur.TAILLE_EQUIPE_MAX + ")");
            selectionCounter.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20; " +
                "-fx-text-fill: white;"
            );
        } else {
            btnValidate.setDisable(true);
            btnValidate.setText("Sélectionnez " + (Joueur.TAILLE_EQUIPE_MAX - selectedCount) + " monstre(s) de plus");
            selectionCounter.setStyle(
                "-fx-background-color: rgba(0,0,0,0.5); " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20;"
            );
        }
        
        // Réactiver les cartes non sélectionnées si on n'a pas atteint le max
        if (selectedCount < Joueur.TAILLE_EQUIPE_MAX) {
            monsterCardMap.keySet().forEach(c -> {
                if (!selectedCards.contains(c)) {
                    c.setDisable(false);
                    c.setOpacity(1.0);
                }
            });
        }
    }
    
    /**
     * Retourne la liste des monstres sélectionnés.
     */
    public List<Monstre> getSelectedMonsters() {
        List<Monstre> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Monstre monstre = monsterCardMap.get(card);
            if (monstre != null) {
                selected.add(monstre);
            }
        }
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
