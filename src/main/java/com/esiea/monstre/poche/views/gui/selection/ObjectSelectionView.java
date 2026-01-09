package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.items.Objet;
import com.esiea.monstre.poche.models.items.medicaments.Medicament;
import com.esiea.monstre.poche.models.items.potions.Potion;
import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Vue pour la sélection des objets - Style Pokémon amélioré.
 * Hérite de AbstractSelectionView pour réutiliser le code commun.
 */
public class ObjectSelectionView extends AbstractSelectionView<Objet> {

    private List<Objet> allObjects;

    public ObjectSelectionView(Joueur joueur) {
        super(joueur);
        this.allObjects = GameResourcesFactory.getInstance().getTousLesObjets();
        initializeView();
    }

    // === IMPLÉMENTATION DES MÉTHODES ABSTRAITES ===

    @Override
    protected String getMainTitle() {
        return joueur.getNomJoueur();
    }

    @Override
    protected String getSubtitle() {
        return "Composez votre inventaire d'objets !";
    }

    @Override
    protected String getInstructions() {
        return "Sélectionnez jusqu'à " + Joueur.TAILLE_INVENTAIRE_MAX + " objets pour le combat";
    }

    @Override
    protected String getCounterText(int selectedCount) {
        return selectedCount + " / " + Joueur.TAILLE_INVENTAIRE_MAX + " objets";
    }

    @Override
    protected String getValidateButtonText(int selectedCount) {
        if (selectedCount > 0) {
            return "VALIDER (" + selectedCount + " OBJET" + (selectedCount > 1 ? "S" : "") + ")";
        } else {
            return "VALIDER L'INVENTAIRE";
        }
    }

    @Override
    protected int getMaxSelection() {
        return Joueur.TAILLE_INVENTAIRE_MAX;
    }

    @Override
    protected boolean requiresMaxSelection() {
        return true;
    }

    @Override
    protected void populateCards() {
        for (Objet objet : allObjects) {
            VBox objectCard = createCard(objet);
            cardsContainer.getChildren().add(objectCard);
        }
    }

    @Override
    public List<Objet> getSelectedItems() {
        List<Objet> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Objet objet = cardMap.get(card);
            if (objet != null) {
                selected.add(objet);
            }
        }
        return selected;
    }

    @Override
    protected VBox createCustomSection() {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);

        HBox legend = createLegend();

        Label hint = new Label("(Vous pouvez valider avec 0 à " + Joueur.TAILLE_INVENTAIRE_MAX + " objets)");
        hint.setTextFill(Color.web("#888"));
        hint.setFont(Font.font(FontConfig.SYSTEM.getFontName(), 12));

        container.getChildren().addAll(legend, hint);
        return container;
    }

    private HBox createLegend() {
        HBox legend = new HBox(30);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(5, 0, 5, 0));

        HBox potionLegend = createLegendItem("POTIONS", ColorConfig.POTION.getColorCode());
        HBox medicamentLegend = createLegendItem("MEDICAMENTS", ColorConfig.MEDICAMENT.getColorCode());

        legend.getChildren().addAll(potionLegend, medicamentLegend);
        return legend;
    }

    private HBox createLegendItem(String text, String color) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER);

        Circle colorDot = new Circle(8);
        colorDot.setFill(Color.web(color));
        colorDot.setStroke(Color.WHITE);
        colorDot.setStrokeWidth(1);

        Label label = new Label(text);
        label.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));
        label.setTextFill(Color.web(color));

        item.getChildren().addAll(colorDot, label);
        return item;
    }

    // === CRÉATION DE CARTE SPÉCIFIQUE ===

    @Override
    protected VBox createCard(Objet objet) {
        String objectType;
        String typeColor;
        String description;

        if (objet instanceof Potion) {
            objectType = "POTION";
            typeColor = ColorConfig.POTION.getColorCode();
            description = getDescriptionPotion(objet);
        } else if (objet instanceof Medicament) {
            objectType = "MEDICAMENT";
            typeColor = ColorConfig.MEDICAMENT.getColorCode();
            description = getDescriptionMedicament(objet);
        } else {
            objectType = "OBJET";
            typeColor = ColorConfig.OBJET.getColorCode();
            description = "Objet spécial";
        }

        // Carte principale
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(180);
        card.setPrefHeight(200);
        card.getStyleClass().add("object-card");

        String normalStyle = String.format(
            "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 40%%); " +
            "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 3; " +
            "-fx-cursor: hand;",
            typeColor + "60", typeColor
        );
        card.setStyle(normalStyle);

        // Icône stylisée (cercle avec initiales)
        StackPane iconContainer = new StackPane();
        Circle circle = new Circle(35);
        circle.setFill(Color.web(typeColor + "80"));
        circle.setStroke(Color.web(typeColor));
        circle.setStrokeWidth(3);

        // Initiales de l'objet
        String initials = getInitials(objet.getNomObjet());
        Label initialLabel = new Label(initials);
        initialLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 18));
        initialLabel.setTextFill(Color.WHITE);
        iconContainer.getChildren().addAll(circle, initialLabel);

        // Nom de l'objet
        Label nameLabel = new Label(formatObjectName(objet.getNomObjet()));
        nameLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 1);");

        // Badge de type
        Label typeBadge = new Label(objectType);
        typeBadge.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 10));
        typeBadge.setTextFill(Color.WHITE);
        typeBadge.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-padding: 3 10; " +
            "-fx-background-radius: 10;",
            typeColor
        ));

        // Description courte
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), 11));
        descLabel.setTextFill(Color.web("#aaa"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);

        // Indicateur de sélection
        Label selectionIndicator = createSelectionIndicator();

        card.getChildren().addAll(iconContainer, nameLabel, typeBadge, descLabel, selectionIndicator);

        // Tooltip
        Tooltip tooltip = new Tooltip(String.format(
            "%s\n\nType: %s\n%s\n\nCliquez pour sélectionner/désélectionner",
            formatObjectName(objet.getNomObjet()),
            objectType,
            description
        ));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);

        // Événements
        card.setOnMouseClicked(e -> handleCardClick(card, objet, selectionIndicator, typeColor, normalStyle));
        addCardHoverEffects(card, typeColor);

        // Association de la carte avec l'objet
        cardMap.put(card, objet);

        return card;
    }

    // === MÉTHODES UTILITAIRES ===

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

    private String formatObjectName(String name) {
        return name.replace("_", " ");
    }

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

    // Alias pour rétro-compatibilité
    public List<Objet> getSelectedObjects() {
        return getSelectedItems();
    }
}
