package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.game.resources.GameResourcesLoader;
import com.esiea.monstre.poche.models.items.Objet;
import com.esiea.monstre.poche.models.items.medicaments.Medicament;
import com.esiea.monstre.poche.models.items.potions.Potion;

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
 * Vue pour la sélection des objets - Style Pokémon amélioré.
 */
public class ObjectSelectionView extends VBox {
    
    private Label lblPlayerName;
    private Button btnValidate;
    private Button btnBackToMenu;
    private FlowPane objectCardsContainer;
    private Map<VBox, Objet> objectCardMap;
    private List<VBox> selectedCards;
    private Label selectionCounter;
    private Joueur joueur;
    
    private final GameResourcesLoader resourcesLoader = new GameResourcesLoader();
    private final GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);
    
    private static final String POTION_COLOR = "#FF6B9D";      // Rose/Magenta pour potions
    private static final String MEDICAMENT_COLOR = "#4ECDC4"; // Turquoise pour médicaments
    private static final String OBJET_COLOR = "#95A5A6";      // Gris pour autres objets
    
    public ObjectSelectionView(Joueur joueur) {
        this.objectCardMap = new HashMap<>();
        this.selectedCards = new ArrayList<>();
        this.joueur = joueur;
        initializeView();
    }
    
    /**
     * Initialise la vue de sélection des objets.
     */
    private void initializeView() {
        List<Objet> allObjects = resourcesFactory.getTousLesObjets();
        
        // Configuration du conteneur principal
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(15, 25, 25, 25));
        this.getStyleClass().add("main-container");
        
        // Barre supérieure avec bouton retour et compteur
        HBox topBar = createTopBar();
        
        // Titre principal stylisé
        VBox titleBox = createTitleSection();
        
        // Légende des types d'objets
        HBox legend = createLegend();
        
        // Container pour les cartes d'objets (grille fluide)
        objectCardsContainer = new FlowPane();
        objectCardsContainer.setHgap(20);
        objectCardsContainer.setVgap(20);
        objectCardsContainer.setAlignment(Pos.CENTER);
        objectCardsContainer.setPadding(new Insets(20));
        
        // Ajout des objets disponibles sous forme de cartes
        for (Objet objet : allObjects) {
            VBox objectCard = createObjectCard(objet);
            objectCardsContainer.getChildren().add(objectCard);
        }
        
        // ScrollPane stylisé pour les objets
        ScrollPane scrollPane = new ScrollPane(objectCardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setMaxHeight(400);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Section de validation avec bouton amélioré
        VBox validationSection = createValidationSection();
        
        // Ajout des éléments au conteneur principal
        this.getChildren().addAll(topBar, titleBox, legend, scrollPane, validationSection);
    }
    
    /**
     * Crée la barre supérieure avec le bouton retour et le compteur.
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
        selectionCounter = new Label("0 / " + Joueur.TAILLE_INVENTAIRE_MAX + " objets");
        selectionCounter.getStyleClass().addAll("label-text", "selection-counter");
        selectionCounter.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 8 15; -fx-background-radius: 20;");
        
        topBar.getChildren().addAll(btnBackToMenu, spacer, selectionCounter);
        return topBar;
    }
    
    /**
     * Crée la section titre avec le nom du joueur.
     */
    private VBox createTitleSection() {
        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);
        
        lblPlayerName = new Label(joueur.getNomJoueur());
        lblPlayerName.getStyleClass().add("main-title");
        
        Label subtitle = new Label("Composez votre inventaire d'objets !");
        subtitle.getStyleClass().add("subtitle-text");
        subtitle.setStyle("-fx-font-size: 16px;");
        
        Label instructions = new Label("Sélectionnez jusqu'à " + Joueur.TAILLE_INVENTAIRE_MAX + " objets pour le combat");
        instructions.getStyleClass().add("label-text");
        instructions.setStyle("-fx-font-size: 14px; -fx-opacity: 0.8;");
        
        titleBox.getChildren().addAll(lblPlayerName, subtitle, instructions);
        return titleBox;
    }
    
    /**
     * Crée la légende des types d'objets.
     */
    private HBox createLegend() {
        HBox legend = new HBox(30);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(10, 0, 5, 0));
        
        // Légende Potions
        HBox potionLegend = createLegendItem("POTIONS", POTION_COLOR);
        
        // Légende Médicaments
        HBox medicamentLegend = createLegendItem("MEDICAMENTS", MEDICAMENT_COLOR);
        
        legend.getChildren().addAll(potionLegend, medicamentLegend);
        return legend;
    }
    
    /**
     * Crée un élément de légende.
     */
    private HBox createLegendItem(String text, String color) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER);
        
        Circle colorDot = new Circle(8);
        colorDot.setFill(Color.web(color));
        colorDot.setStroke(Color.WHITE);
        colorDot.setStrokeWidth(1);
        
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 12));
        label.setTextFill(Color.web(color));
        
        item.getChildren().addAll(colorDot, label);
        return item;
    }
    
    /**
     * Crée la section de validation avec le bouton.
     */
    private VBox createValidationSection() {
        VBox section = new VBox(10);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(15, 0, 5, 0));
        
        btnValidate = new Button("VALIDER L'INVENTAIRE");
        btnValidate.getStyleClass().addAll("menu-button", "validate-button");
        btnValidate.setPrefWidth(350);
        btnValidate.setPrefHeight(50);
        btnValidate.setStyle("-fx-font-size: 20px;");
        // Le bouton est activé dès qu'on a au moins 1 objet (on peut avoir 0 à 5)
        btnValidate.setDisable(false);
        
        Label hint = new Label("(Vous pouvez valider avec 0 à " + Joueur.TAILLE_INVENTAIRE_MAX + " objets)");
        hint.setTextFill(Color.web("#888"));
        hint.setFont(Font.font("System", 12));
        
        section.getChildren().addAll(btnValidate, hint);
        return section;
    }
    
    /**
     * Crée une carte stylisée Pokémon pour un objet.
     */
    private VBox createObjectCard(Objet objet) {
        // Déterminer le type et la couleur de l'objet
        String objectType;
        String typeColor;
        String description;
        
        if (objet instanceof Potion) {
            objectType = "POTION";
            typeColor = POTION_COLOR;
            description = getDescriptionPotion(objet);
        } else if (objet instanceof Medicament) {
            objectType = "MEDICAMENT";
            typeColor = MEDICAMENT_COLOR;
            description = getDescriptionMedicament(objet);
        } else {
            objectType = "OBJET";
            typeColor = OBJET_COLOR;
            description = "Objet spécial";
        }
        
        // Carte principale
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(180);
        card.setPrefHeight(200);
        card.getStyleClass().add("object-card");
        card.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 40%%); " +
            "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 3; " +
            "-fx-cursor: hand;",
            typeColor + "60", typeColor
        ));
        
        // Icône stylisée (cercle avec initiales)
        StackPane iconContainer = new StackPane();
        Circle circle = new Circle(35);
        circle.setFill(Color.web(typeColor + "80"));
        circle.setStroke(Color.web(typeColor));
        circle.setStrokeWidth(3);
        
        // Initiales de l'objet
        String initials = getInitials(objet.getNomObjet());
        Label initialLabel = new Label(initials);
        initialLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        initialLabel.setTextFill(Color.WHITE);
        iconContainer.getChildren().addAll(circle, initialLabel);
        
        // Nom de l'objet
        Label nameLabel = new Label(formatObjectName(objet.getNomObjet()));
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 1);");
        
        // Badge de type
        Label typeBadge = new Label(objectType);
        typeBadge.setFont(Font.font("System", FontWeight.BOLD, 10));
        typeBadge.setTextFill(Color.WHITE);
        typeBadge.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-padding: 3 10; " +
            "-fx-background-radius: 10;",
            typeColor
        ));
        
        // Description courte
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", 11));
        descLabel.setTextFill(Color.web("#aaa"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);
        
        // Indicateur de sélection
        Label selectionIndicator = new Label("✓ SELECTIONNE");
        selectionIndicator.setVisible(false);
        selectionIndicator.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6); " +
            "-fx-text-fill: #4CAF50; " +
            "-fx-padding: 3 8; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 10px;"
        );
        
        card.getChildren().addAll(iconContainer, nameLabel, typeBadge, descLabel, selectionIndicator);
        
        // Tooltip avec plus de détails
        Tooltip tooltip = new Tooltip(String.format(
            "%s\n\nType: %s\n%s\n\nCliquez pour sélectionner/désélectionner",
            formatObjectName(objet.getNomObjet()),
            objectType,
            description
        ));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);
        
        // Gestion du clic
        card.setOnMouseClicked(e -> handleCardClick(card, objet, selectionIndicator, typeColor));
        
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
        
        // Association de la carte avec l'objet
        objectCardMap.put(card, objet);
        
        return card;
    }
    
    /**
     * Obtient les initiales d'un nom d'objet.
     */
    private String getInitials(String name) {
        String[] parts = name.replace("_", " ").split(" ");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) {
            if (!parts[i].isEmpty()) {
                initials.append(parts[i].substring(0, 1).toUpperCase());
            }
        }
        return initials.toString();
    }
    
    /**
     * Formate le nom d'un objet pour l'affichage.
     */
    private String formatObjectName(String name) {
        return name.replace("_", " ");
    }
    
    /**
     * Obtient la description d'une potion.
     */
    private String getDescriptionPotion(Objet objet) {
        String name = objet.getNomObjet().toLowerCase();
        if (name.contains("sante") || name.contains("vie")) {
            return "Restaure les PV";
        } else if (name.contains("vitesse")) {
            return "Augmente la vitesse";
        } else if (name.contains("degat") || name.contains("attaque")) {
            return "Boost les dégâts";
        }
        return "Effet de potion";
    }
    
    /**
     * Obtient la description d'un médicament.
     */
    private String getDescriptionMedicament(Objet objet) {
        String name = objet.getNomObjet().toLowerCase();
        if (name.contains("brulure")) {
            return "Soigne les brûlures";
        } else if (name.contains("paralysie")) {
            return "Soigne la paralysie";
        } else if (name.contains("poison")) {
            return "Soigne le poison";
        } else if (name.contains("innond") || name.contains("inond")) {
            return "Contre l'inondation";
        }
        return "Soigne un statut";
    }
    
    /**
     * Gère le clic sur une carte d'objet.
     */
    private void handleCardClick(VBox card, Objet objet, Label selectionIndicator, String typeColor) {
        if (selectedCards.contains(card)) {
            // Désélectionner
            selectedCards.remove(card);
            selectionIndicator.setVisible(false);
            card.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, %s60, #1a1a1a 40%%); " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: %s; " +
                "-fx-border-width: 3; " +
                "-fx-cursor: hand;",
                typeColor, typeColor
            ));
            card.setScaleX(1.0);
            card.setScaleY(1.0);
            card.setEffect(null);
            
            // Réactiver toutes les cartes
            objectCardMap.keySet().forEach(c -> {
                c.setDisable(false);
                c.setOpacity(1.0);
            });
        } else if (selectedCards.size() < Joueur.TAILLE_INVENTAIRE_MAX) {
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
                "-fx-background-color: linear-gradient(to bottom, %s60, #1a1a1a 40%%); " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: #4CAF50; " +
                "-fx-border-width: 4; " +
                "-fx-cursor: hand;",
                typeColor
            ));
            
            Glow glow = new Glow(0.4);
            DropShadow shadow = new DropShadow(12, Color.web("#4CAF50"));
            glow.setInput(shadow);
            card.setEffect(glow);
            
            // Désactiver les autres cartes si on a atteint le max
            if (selectedCards.size() >= Joueur.TAILLE_INVENTAIRE_MAX) {
                objectCardMap.keySet().forEach(c -> {
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
     * Met à jour l'état du bouton de validation et du compteur.
     */
    private void updateValidateButton() {
        int selectedCount = selectedCards.size();
        
        selectionCounter.setText(selectedCount + " / " + Joueur.TAILLE_INVENTAIRE_MAX + " objets");
        
        if (selectedCount > 0) {
            selectionCounter.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20; " +
                "-fx-text-fill: white;"
            );
            btnValidate.setText("VALIDER (" + selectedCount + " OBJET" + (selectedCount > 1 ? "S" : "") + ")");
        } else {
            selectionCounter.setStyle(
                "-fx-background-color: rgba(0,0,0,0.5); " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 20;"
            );
            btnValidate.setText("VALIDER L'INVENTAIRE");
        }
        
        // Réactiver les cartes non sélectionnées si on n'a pas atteint le max
        if (selectedCount < Joueur.TAILLE_INVENTAIRE_MAX) {
            objectCardMap.keySet().forEach(c -> {
                if (!selectedCards.contains(c)) {
                    c.setDisable(false);
                    c.setOpacity(1.0);
                }
            });
        }
    }
    
    /**
     * Retourne la liste des objets sélectionnés.
     */
    public List<Objet> getSelectedObjects() {
        List<Objet> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Objet objet = objectCardMap.get(card);
            if (objet != null) {
                selected.add(objet);
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
