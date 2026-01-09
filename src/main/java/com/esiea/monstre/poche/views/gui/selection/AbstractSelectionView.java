package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe mère abstraite pour toutes les vues de sélection (Attaques, Monstres, Objets).
 * Factorise tout le code commun : structure, animations, gestion des sélections, etc.
 *
 * @param <T> Le type d'élément sélectionnable (Attaque, Monstre, Objet)
 */
public abstract class AbstractSelectionView<T> extends VBox {
    private static final int MAIN_SPACING = 15;
    private static final int CARD_H_GAP = 20;
    private static final int CARD_V_GAP = 20;
    private static final int SCROLL_PANE_HEIGHT = 400;

    protected Label lblPlayerName;
    protected Button btnValidate;
    protected Button btnBackToMenu;
    protected FlowPane cardsContainer;
    protected Map<VBox, T> cardMap;
    protected List<VBox> selectedCards;
    protected Label selectionCounter;
    protected Joueur joueur;

    protected AbstractSelectionView(Joueur joueur) {
        this.cardMap = new HashMap<>();
        this.selectedCards = new ArrayList<>();
        this.joueur = joueur;
    }

    protected abstract String getMainTitle();
    protected abstract String getSubtitle();
    protected abstract String getInstructions();
    protected abstract String getCounterText(int selectedCount);
    protected abstract String getValidateButtonText(int selectedCount);
    protected abstract int getMaxSelection();
    protected abstract boolean requiresMaxSelection();
    protected abstract VBox createCard(T item);
    protected abstract void populateCards();
    public abstract List<T> getSelectedItems();


    /**
     * Initialise la vue selon le pattern Template Method.
     * Les sous-classes appellent cette méthode dans leur constructeur.
     */
    protected void initializeView() {
        this.setSpacing(MAIN_SPACING);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(15, 25, 25, 25));
        this.getStyleClass().add("main-container");

        // Construction de la vue
        HBox topBar = createTopBar();
        VBox titleSection = createTitleSection();
        VBox customSection = createCustomSection();

        // Container pour les cartes (grille fluide)
        cardsContainer = new FlowPane();
        cardsContainer.setHgap(CARD_H_GAP);
        cardsContainer.setVgap(CARD_V_GAP);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.setPadding(new Insets(15));

        // Ajout des cartes via méthode abstraite
        populateCards();

        // ScrollPane pour les cartes
        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(SCROLL_PANE_HEIGHT);
        scrollPane.setMaxHeight(SCROLL_PANE_HEIGHT);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Section de validation
        VBox validationSection = createValidationSection();

        // Ajout des éléments au conteneur principal
        if (customSection != null) {
            this.getChildren().addAll(topBar, titleSection, customSection, scrollPane, validationSection);
        } else {
            this.getChildren().addAll(topBar, titleSection, scrollPane, validationSection);
        }
    }

    /**
     * Crée la barre supérieure avec le bouton retour et le compteur.
     */
    protected HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5, 10, 5, 10));

        btnBackToMenu = new Button("Menu");
        btnBackToMenu.getStyleClass().add("back-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Compteur de sélection stylisé
        selectionCounter = new Label(getCounterText(0));
        selectionCounter.getStyleClass().addAll("label-text", "selection-counter");
        selectionCounter.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 8 15; -fx-background-radius: 20;");

        topBar.getChildren().addAll(btnBackToMenu, spacer, selectionCounter);
        return topBar;
    }

    /**
     * Crée la section titre avec le nom du joueur.
     */
    protected VBox createTitleSection() {
        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);

        lblPlayerName = new Label(getMainTitle());
        lblPlayerName.getStyleClass().add("main-title");
        lblPlayerName.setStyle("-fx-text-fill: #FFFFFF;");
        lblPlayerName.setEffect(new DropShadow(2, Color.BLACK));


        Label subtitle = new Label(getSubtitle());
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setStyle("-fx-font-size: 16px;");
        subtitle.setStyle("-fx-text-fill: #FFFFFF;");
        subtitle.setEffect(new DropShadow(2, Color.BLACK));

        Label instructions = new Label(getInstructions());
        instructions.getStyleClass().add("label-text");
        instructions.setStyle("-fx-font-size: 14px; -fx-opacity: 0.8;-fx-text-fill: #FFFFFF;");

        titleBox.getChildren().addAll(lblPlayerName, subtitle, instructions);
        return titleBox;
    }

    /**
     * Crée la section de validation avec le bouton.
     */
    protected VBox createValidationSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(15, 0, 5, 0));

        btnValidate = new Button(getValidateButtonText(0));
        btnValidate.getStyleClass().addAll("menu-button", "validate-button");
        btnValidate.setPrefWidth(350);
        btnValidate.setPrefHeight(50);
        btnValidate.setStyle("-fx-font-size: 18px;");
        btnValidate.setDisable(requiresMaxSelection());

        section.getChildren().add(btnValidate);
        return section;
    }

    /**
     * Crée un badge de type stylisé (utilitaire commun).
     */
    protected HBox createTypeBadge(String typeLabel, String typeColor) {
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
     * Ajoute les effets de hover standard à une carte.
     */
    protected void addCardHoverEffects(VBox card, String typeColor) {
        card.setOnMouseEntered(e -> {
            if (!selectedCards.contains(card)) {
                card.setScaleX(1.05);
                card.setScaleY(1.05);
                card.setEffect(new DropShadow(15, Color.web(typeColor != null ? typeColor : "#FFFFFF")));
            }
        });

        card.setOnMouseExited(e -> {
            if (!selectedCards.contains(card)) {
                card.setScaleX(1.0);
                card.setScaleY(1.0);
                card.setEffect(null);
            }
        });
    }

    /**
     * Ajoute un indicateur de sélection à une carte.
     */
    protected Label createSelectionIndicator() {
        Label indicator = new Label("SÉLECTIONNÉ");
        indicator.setVisible(false);
        indicator.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6); " +
            "-fx-text-fill: #4CAF50; " +
            "-fx-padding: 3 8; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 10px;"
        );
        return indicator;
    }

    /**
     * Gère le clic sur une carte (logique commune de sélection/désélection).
     */
    protected void handleCardClick(VBox card, T item, Label selectionIndicator, String typeColor, String normalStyle) {
        if (selectedCards.contains(card)) {
            // Désélectionner
            selectedCards.remove(card);
            selectionIndicator.setVisible(false);
            card.setStyle(normalStyle);
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setEffect(null);

            // Réactiver toutes les cartes
            cardMap.keySet().forEach(c -> {
                c.setDisable(false);
                c.setOpacity(1.0);
            });

        } else if (selectedCards.size() < getMaxSelection()) {
            // Vérifier si la sélection est autorisée
            if (!beforeCardSelect(card, item)) {
                return;
            }

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

            // Style de sélection
            String selectedStyle = normalStyle.replace(typeColor, typeColor)
                .replaceAll("-fx-border-color: [^;]+;", "-fx-border-color: #FFFFFF;")
                .replaceAll("-fx-border-width: [^;]+;", "-fx-border-width: 4;");
            card.setStyle(selectedStyle);

            Glow glow = new Glow(0.4);
            DropShadow shadow = new DropShadow(12, Color.web("#FFFFFF"));
            glow.setInput(shadow);
            card.setEffect(glow);

            // Désactiver les autres cartes si on a atteint le max
            if (selectedCards.size() >= getMaxSelection()) {
                cardMap.keySet().forEach(c -> {
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
     * Met à jour l'état du bouton de validation selon le nombre de sélections.
     */
    protected void updateValidateButton() {
        int selectedCount = selectedCards.size();

        selectionCounter.setText(getCounterText(selectedCount));

        boolean canValidate = requiresMaxSelection()? (selectedCount == getMaxSelection()) : (selectedCount >= 0);

        if (canValidate && selectedCount > 0) {
            btnValidate.setDisable(false);
            btnValidate.setText(getValidateButtonText(selectedCount));
            selectionCounter.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20; " +
                "-fx-text-fill: white;"
            );
        } else {
            btnValidate.setDisable(requiresMaxSelection());
            btnValidate.setText(getValidateButtonText(selectedCount));
            selectionCounter.setStyle(
                "-fx-background-color: rgba(0,0,0,0.5); " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20;"
            );
        }

        // Réactiver les cartes non sélectionnées si on n'a pas atteint le max
        if (selectedCount < getMaxSelection()) {
            cardMap.keySet().forEach(c -> {
                if (!selectedCards.contains(c)) {
                    c.setDisable(false);
                    c.setOpacity(1.0);
                }
            });
        }
    }
    
    /**
     * Crée une section personnalisée entre le titre et les cartes.
     * Par défaut retourne null (pas de section).
     */
    protected VBox createCustomSection() {
        return null;
    }

    /**
     * Hook appelé avant la sélection d'une carte.
     * Retourne true pour autoriser, false pour empêcher.
     */
    protected boolean beforeCardSelect(VBox card, T item) {
        return true;
    }

    public Button getBtnValidate() {
        return btnValidate;
    }

    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }

    public Joueur getJoueur() {
        return joueur;
    }
    
}
