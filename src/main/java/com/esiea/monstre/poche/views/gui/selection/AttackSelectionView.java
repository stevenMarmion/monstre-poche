package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.game.resources.GameResourcesLoader;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
 * Vue pour la sélection des attaques - Style Pokémon Combat amélioré.
 */
public class AttackSelectionView extends VBox {
    
    private Label lblPlayerName;
    private Label lblMonsterName;
    private Button btnValidate;
    private Button btnBackToMenu;
    private FlowPane attackCardsContainer;
    private Map<VBox, Attaque> attackCardMap;
    private List<VBox> selectedCards;
    private int maxAttacksToSelect = 4;
    private Label selectionCounter;
    private Joueur joueur;
    private Monstre currentMonstre;

    private final GameResourcesLoader resourcesLoader = new GameResourcesLoader();
    private final GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);
    
    // Couleurs par type (style Pokémon)
    private static final Map<String, String> TYPE_COLORS = Map.ofEntries(
        Map.entry("Feu", "#F08030"),
        Map.entry("Eau", "#6890F0"),
        Map.entry("Plante", "#78C850"),
        Map.entry("Foudre", "#F8D030"),
        Map.entry("Terre", "#E0C068"),
        Map.entry("Normal", "#A8A878"),
        Map.entry("Insecte", "#A8B820"),
        Map.entry("Nature", "#228B22")
    );
    
    // Pas d'icônes - on utilise uniquement les couleurs et le texte
    
    public AttackSelectionView(Joueur joueur) {
        this.attackCardMap = new HashMap<>();
        this.selectedCards = new ArrayList<>();
        this.joueur = joueur;
        this.currentMonstre = joueur.getMonstreActuel();
        initializeView(joueur);
    }
    
    /**
     * Initialise la vue de sélection des attaques.
     */
    private void initializeView(Joueur joueur) {
        // Filtrer les attaques par type du monstre
        List<Attaque> filteredAttacks = new ArrayList<>();
        for (Attaque attaque : resourcesFactory.getToutesLesAttaques()) {
            if (attaque.getTypeAttaque().getLabelType().equals(currentMonstre.getTypeMonstre().getLabelType())) {
                filteredAttacks.add(attaque);
            }
        }
        
        // Configuration du conteneur principal
        this.setSpacing(12);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(15, 25, 25, 25));
        this.getStyleClass().add("main-container");
        
        // Barre supérieure
        HBox topBar = createTopBar();
        
        // Section d'information du monstre actuel
        VBox monsterInfoSection = createMonsterInfoSection();
        
        // Titre des attaques
        VBox attackTitleSection = createAttackTitleSection(filteredAttacks.size());
        
        // Container pour les cartes d'attaques (grille fluide style combat Pokémon)
        attackCardsContainer = new FlowPane();
        attackCardsContainer.setHgap(15);
        attackCardsContainer.setVgap(15);
        attackCardsContainer.setAlignment(Pos.CENTER);
        attackCardsContainer.setPadding(new Insets(15));
        
        // Ajout des attaques filtrées sous forme de boutons stylisés
        for (Attaque attaque : filteredAttacks) {
            VBox attackCard = createAttackCard(attaque);
            attackCardsContainer.getChildren().add(attackCard);
        }
        
        // ScrollPane pour les attaques
        ScrollPane scrollPane = new ScrollPane(attackCardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(320);
        scrollPane.setMaxHeight(320);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Section de validation
        VBox validationSection = createValidationSection();
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, monsterInfoSection, attackTitleSection, scrollPane, validationSection);
    }
    
    /**
     * Crée la barre supérieure.
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5, 10, 5, 10));
        
        btnBackToMenu = new Button("◀ Menu");
        btnBackToMenu.getStyleClass().add("back-button");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Compteur de sélection stylisé
        selectionCounter = new Label("0 / " + maxAttacksToSelect + " attaques");
        selectionCounter.getStyleClass().addAll("label-text", "selection-counter");
        selectionCounter.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 8 15; -fx-background-radius: 20;");
        
        topBar.getChildren().addAll(btnBackToMenu, spacer, selectionCounter);
        return topBar;
    }
    
    /**
     * Crée la section d'information du monstre actuel.
     */
    private VBox createMonsterInfoSection() {
        String typeLabel = currentMonstre.getTypeMonstre().getLabelType();
        String typeColor = TYPE_COLORS.getOrDefault(typeLabel, "#A8A878");
        
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(15));
        section.setStyle(String.format(
            "-fx-background-color: linear-gradient(to right, %s40, transparent, %s40); " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 15;",
            typeColor, typeColor, typeColor
        ));
        
        // Titre du joueur
        lblPlayerName = new Label(joueur.getNomJoueur());
        lblPlayerName.getStyleClass().add("main-title");
        lblPlayerName.setStyle("-fx-font-size: 24px;");
        
        // Info du monstre avec avatar
        HBox monsterInfo = new HBox(15);
        monsterInfo.setAlignment(Pos.CENTER);
        
        // Mini avatar du monstre
        StackPane avatar = new StackPane();
        Circle circle = new Circle(30);
        circle.setFill(Color.web(typeColor + "80"));
        circle.setStroke(Color.web(typeColor));
        circle.setStrokeWidth(2);
        
        Label initialLabel = new Label(currentMonstre.getNomMonstre().substring(0, Math.min(2, currentMonstre.getNomMonstre().length())).toUpperCase());
        initialLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        initialLabel.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(circle, initialLabel);
        
        // Nom et type du monstre
        VBox nameBox = new VBox(3);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        
        lblMonsterName = new Label(currentMonstre.getNomMonstre());
        lblMonsterName.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblMonsterName.setTextFill(Color.WHITE);
        
        HBox typeBadge = createTypeBadge(typeLabel, typeColor);
        
        // Stats rapides du monstre
        Label statsLabel = new Label(String.format(
            "PV: %.0f  |  ATK: %d  |  DEF: %d  |  VIT: %d",
            currentMonstre.getPointsDeVie(),
            currentMonstre.getAttaque(),
            currentMonstre.getDefense(),
            currentMonstre.getVitesse()
        ));
        statsLabel.setTextFill(Color.web("#ccc"));
        statsLabel.setFont(Font.font("System", 12));
        
        nameBox.getChildren().addAll(lblMonsterName, typeBadge, statsLabel);
        monsterInfo.getChildren().addAll(avatar, nameBox);
        
        // Indicateur de progression (monstre X sur Y)
        int currentIndex = joueur.getMonstres().indexOf(currentMonstre) + 1;
        int totalMonstres = joueur.getMonstres().size();
        Label progressLabel = new Label("Monstre " + currentIndex + " / " + totalMonstres);
        progressLabel.setStyle(
            "-fx-background-color: #333; " +
            "-fx-text-fill: #f8eec7; " +
            "-fx-padding: 5 12; " +
            "-fx-background-radius: 10; " +
            "-fx-font-size: 12px;"
        );
        
        section.getChildren().addAll(lblPlayerName, monsterInfo, progressLabel);
        return section;
    }
    
    /**
     * Crée un badge de type stylisé.
     */
    private HBox createTypeBadge(String typeLabel, String typeColor) {
        HBox badge = new HBox(5);
        badge.setAlignment(Pos.CENTER_LEFT);
        badge.setPadding(new Insets(3, 10, 3, 10));
        badge.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-background-radius: 10;",
            typeColor
        ));
        
        Label text = new Label(typeLabel.toUpperCase());
        text.setFont(Font.font("System", FontWeight.BOLD, 10));
        text.setTextFill(Color.WHITE);
        
        badge.getChildren().add(text);
        return badge;
    }
    
    /**
     * Crée la section titre des attaques.
     */
    private VBox createAttackTitleSection(int nbAttacks) {
        VBox section = new VBox(5);
        section.setAlignment(Pos.CENTER);
        
        Label title = new Label("Choisissez vos attaques");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#f8eec7"));
        
        Label subtitle = new Label("Sélectionnez " + maxAttacksToSelect + " attaques parmi les " + nbAttacks + " disponibles");
        subtitle.setTextFill(Color.web("#aaa"));
        subtitle.setFont(Font.font("System", 13));
        
        section.getChildren().addAll(title, subtitle);
        return section;
    }
    
    /**
     * Crée la section de validation.
     */
    private VBox createValidationSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(15, 0, 5, 0));
        
        btnValidate = new Button("VALIDER LES ATTAQUES");
        btnValidate.getStyleClass().addAll("menu-button", "validate-button");
        btnValidate.setPrefWidth(350);
        btnValidate.setPrefHeight(50);
        btnValidate.setStyle("-fx-font-size: 18px;");
        btnValidate.setDisable(true);
        
        section.getChildren().add(btnValidate);
        return section;
    }
    
    /**
     * Crée une carte d'attaque stylisée (style bouton combat Pokémon).
     */
    private VBox createAttackCard(Attaque attaque) {
        String typeLabel = attaque.getTypeAttaque().getLabelType();
        String typeColor = TYPE_COLORS.getOrDefault(typeLabel, "#A8A878");
        
        // Carte principale
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12, 15, 12, 15));
        card.setPrefWidth(220);
        card.setPrefHeight(140);
        card.getStyleClass().add("attack-card");
        card.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s90); " +
            "-fx-background-radius: 12; " +
            "-fx-border-radius: 12; " +
            "-fx-border-color: %scc; " +
            "-fx-border-width: 3; " +
            "-fx-cursor: hand;",
            typeColor, typeColor, typeColor
        ));
        
        // Nom de l'attaque
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(attaque.getNomAttaque().replace("_", " "));
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 1);");
        
        nameRow.getChildren().add(nameLabel);
        
        // Type badge
        HBox typeBadgeRow = new HBox();
        typeBadgeRow.setAlignment(Pos.CENTER);
        
        Label typeBadge = new Label(typeLabel.toUpperCase());
        typeBadge.setFont(Font.font("System", FontWeight.BOLD, 9));
        typeBadge.setTextFill(Color.WHITE);
        typeBadge.setStyle(
            "-fx-background-color: rgba(0,0,0,0.4); " +
            "-fx-padding: 2 8; " +
            "-fx-background-radius: 8;"
        );
        typeBadgeRow.getChildren().add(typeBadge);
        
        // Statistiques de l'attaque
        HBox statsRow = new HBox(12);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.setPadding(new Insets(5, 0, 0, 0));
        
        // Puissance
        VBox powerBox = createStatBox("PWR", String.valueOf(attaque.getPuissanceAttaque()), "#ff6b6b");
        
        // Utilisations
        VBox usesBox = createStatBox("PP", String.valueOf(attaque.getNbUtilisations()), "#4dabf7");
        
        // Précision
        int precision = (int) ((1 - attaque.getProbabiliteEchec()) * 100);
        VBox precisionBox = createStatBox("ACC", precision + "%", "#69db7c");
        
        statsRow.getChildren().addAll(powerBox, usesBox, precisionBox);
        
        // Indicateur de sélection
        Label selectionIndicator = new Label("✓ SÉLECTIONNÉE");
        selectionIndicator.setVisible(false);
        selectionIndicator.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6); " +
            "-fx-text-fill: #4CAF50; " +
            "-fx-padding: 3 8; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 10px;"
        );
        
        card.getChildren().addAll(nameRow, typeBadgeRow, statsRow, selectionIndicator);
        
        // Tooltip avec plus de détails
        Tooltip tooltip = new Tooltip(String.format(
            "%s\n\nPuissance: %d\nUtilisations: %d\nPrécision: %d%%\n\nCliquez pour sélectionner",
            attaque.getNomAttaque().replace("_", " "),
            attaque.getPuissanceAttaque(),
            attaque.getNbUtilisations(),
            precision
        ));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);
        
        // Gestion du clic
        card.setOnMouseClicked(e -> handleCardClick(card, attaque, selectionIndicator, typeColor));
        
        // Effet de survol
        card.setOnMouseEntered(e -> {
            if (!selectedCards.contains(card)) {
                card.setScaleX(1.05);
                card.setScaleY(1.05);
                card.setEffect(new DropShadow(15, Color.WHITE));
            }
        });
        
        card.setOnMouseExited(e -> {
            if (!selectedCards.contains(card)) {
                card.setScaleX(1.0);
                card.setScaleY(1.0);
                card.setEffect(null);
            }
        });
        
        // Association de la carte avec l'attaque
        attackCardMap.put(card, attaque);
        
        return card;
    }
    
    /**
     * Crée une boîte de statistique pour une attaque.
     */
    private VBox createStatBox(String label, String value, String color) {
        VBox box = new VBox(1);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(4, 10, 4, 10));
        box.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3); " +
            "-fx-background-radius: 8;"
        );
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("System", FontWeight.BOLD, 9));
        labelText.setTextFill(Color.web("#aaa"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        valueLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(labelText, valueLabel);
        return box;
    }
    
    /**
     * Gère le clic sur une carte d'attaque.
     */
    private void handleCardClick(VBox card, Attaque attaque, Label selectionIndicator, String typeColor) {
        if (selectedCards.contains(card)) {
            // Désélectionner
            selectedCards.remove(card);
            selectionIndicator.setVisible(false);
            card.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, %s, %s90); " +
                "-fx-background-radius: 12; " +
                "-fx-border-radius: 12; " +
                "-fx-border-color: %scc; " +
                "-fx-border-width: 3; " +
                "-fx-cursor: hand;",
                typeColor, typeColor, typeColor
            ));
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setEffect(null);
            
            // Réactiver toutes les cartes
            attackCardMap.keySet().forEach(c -> {
                c.setDisable(false);
                c.setOpacity(1.0);
            });
        } else if (selectedCards.size() < maxAttacksToSelect) {
            // Sélectionner
            selectedCards.add(card);
            selectionIndicator.setVisible(true);
            
            // Animation de sélection
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setFromX(1.0);
            st.setFromY(1.0);
            st.setToX(1.08);
            st.setToY(1.08);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();
            
            card.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, %s, %s90); " +
                "-fx-background-radius: 12; " +
                "-fx-border-radius: 12; " +
                "-fx-border-color: #4CAF50; " +
                "-fx-border-width: 4; " +
                "-fx-cursor: hand;",
                typeColor, typeColor
            ));
            
            Glow glow = new Glow(0.4);
            DropShadow shadow = new DropShadow(12, Color.web("#4CAF50"));
            glow.setInput(shadow);
            card.setEffect(glow);
            
            // Désactiver les autres cartes si on a atteint le max
            if (selectedCards.size() >= maxAttacksToSelect) {
                attackCardMap.keySet().forEach(c -> {
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
     * Met à jour l'état du bouton de validation.
     */
    private void updateValidateButton() {
        int selectedCount = selectedCards.size();
        
        selectionCounter.setText(selectedCount + " / " + maxAttacksToSelect + " attaques");
        
        if (selectedCount == maxAttacksToSelect) {
            btnValidate.setDisable(false);
            
            // Vérifier s'il y a encore des monstres à configurer
            int currentIndex = joueur.getMonstres().indexOf(currentMonstre) + 1;
            int totalMonstres = joueur.getMonstres().size();
            
            if (currentIndex < totalMonstres) {
                btnValidate.setText("MONSTRE SUIVANT (" + (currentIndex + 1) + "/" + totalMonstres + ")");
            } else {
                btnValidate.setText("TERMINER LA SÉLECTION");
            }
            
            selectionCounter.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20; " +
                "-fx-text-fill: white;"
            );
        } else {
            btnValidate.setDisable(true);
            btnValidate.setText("Sélectionnez " + (maxAttacksToSelect - selectedCount) + " attaque(s) de plus");
            selectionCounter.setStyle(
                "-fx-background-color: rgba(0,0,0,0.5); " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20;"
            );
        }
        
        // Réactiver les cartes non sélectionnées si on n'a pas atteint le max
        if (selectedCount < maxAttacksToSelect) {
            attackCardMap.keySet().forEach(c -> {
                if (!selectedCards.contains(c)) {
                    c.setDisable(false);
                    c.setOpacity(1.0);
                }
            });
        }
    }
    
    /**
     * Retourne la liste des attaques sélectionnées.
     */
    public List<Attaque> getSelectedAttacks() {
        List<Attaque> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Attaque attaque = attackCardMap.get(card);
            if (attaque != null) {
                selected.add(attaque);
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
