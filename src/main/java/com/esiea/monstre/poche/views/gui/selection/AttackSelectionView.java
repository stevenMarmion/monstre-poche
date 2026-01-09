package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
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
 * Vue pour la sélection des attaques - Style Pokémon Combat amélioré.
 * Hérite de AbstractSelectionView pour réutiliser le code commun.
 */
public class AttackSelectionView extends AbstractSelectionView<Attaque> {
    private Monstre currentMonstre;
    private List<Attaque> filteredAttacks;

    public AttackSelectionView(Joueur joueur) {
        super(joueur);
        this.currentMonstre = joueur.getMonstreActuel();

        this.filteredAttacks = new ArrayList<>();
        for (Attaque attaque : GameResourcesFactory.getInstance().getToutesLesAttaques()) {
            if (attaque.getTypeAttaque().getLabelType().equals(currentMonstre.getTypeMonstre().getLabelType()) || attaque.getTypeAttaque().getLabelType().equals("Normal")) {
                filteredAttacks.add(attaque);
            }
        }

        initializeView();
    }

    @Override
    protected String getMainTitle() {
        return joueur.getNomJoueur();
    }

    @Override
    protected String getSubtitle() {
        return "Choisissez vos attaques";
    }

    @Override
    protected String getInstructions() {
        return "Sélectionnez " + Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE + " attaques parmi les " + filteredAttacks.size() + " disponibles";
    }

    @Override
    protected String getCounterText(int selectedCount) {
        return selectedCount + " / " + Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE + " attaques";
    }

    @Override
    protected String getValidateButtonText(int selectedCount) {
        if (selectedCount == Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE) {
            int currentIndex = joueur.getMonstres().indexOf(currentMonstre) + 1;
            int totalMonstres = joueur.getMonstres().size();

            if (currentIndex < totalMonstres) {
                return "MONSTRE SUIVANT (" + (currentIndex + 1) + "/" + totalMonstres + ")";
            } else {
                return "TERMINER LA SÉLECTION";
            }
        } else {
            return "Sélectionnez " + (Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE - selectedCount) + " attaque(s) de plus";
        }
    }

    @Override
    protected int getMaxSelection() {
        return Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE;
    }

    @Override
    protected boolean requiresMaxSelection() {
        return true;
    }

    @Override
    protected void populateCards() {
        for (Attaque attaque : filteredAttacks) {
            VBox attackCard = createCard(attaque);
            cardsContainer.getChildren().add(attackCard);
        }
    }

    @Override
    public List<Attaque> getSelectedItems() {
        List<Attaque> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Attaque attaque = cardMap.get(card);
            if (attaque != null) {
                selected.add(attaque);
            }
        }
        return selected;
    }

    @Override
    protected VBox createCustomSection() {
        String typeLabel = currentMonstre.getTypeMonstre().getLabelType();
        String typeColor = ColorConfig.fromString(typeLabel).getColorCode();

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
        initialLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        initialLabel.setTextFill(Color.WHITE);
        avatar.getChildren().addAll(circle, initialLabel);

        // Nom et type du monstre
        VBox nameBox = new VBox(3);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label monsterName = new Label(currentMonstre.getNomMonstre());
        monsterName.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 22));
        monsterName.setTextFill(Color.WHITE);

        HBox typeBadge = createTypeBadge(typeLabel, typeColor);

        // Stats rapides du monstre
        Label statsLabel = new Label(String.format(
            "PV: %.0f  |  ATK: %d  |  DEF: %d  |  VIT: %d",
            currentMonstre.getPointsDeVie(),
            currentMonstre.getAttaque(),
            currentMonstre.getDefense(),
            currentMonstre.getVitesse()
        ));
        statsLabel.setStyle("-fx-text-fill: #FFFFFF;");
        statsLabel.setEffect(new DropShadow(2, Color.BLACK));

        statsLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));

        nameBox.getChildren().addAll(monsterName, typeBadge, statsLabel);
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

        section.getChildren().addAll(monsterInfo, progressLabel);
        return section;
    }

    // === CRÉATION DE CARTE SPÉCIFIQUE ===

    @Override
    protected VBox createCard(Attaque attaque) {
        String typeLabel = attaque.getTypeAttaque().getLabelType();
        String typeColor = ColorConfig.fromString(typeLabel).getColorCode();

        // Carte principale
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12, 15, 12, 15));
        card.setPrefWidth(220);
        card.setPrefHeight(140);
        card.getStyleClass().add("attack-card");

        String normalStyle = String.format(
            "-fx-background-color: linear-gradient(to bottom, %s, %s90); " +
            "-fx-background-radius: 12; " +
            "-fx-border-radius: 12; " +
            "-fx-border-color: %scc; " +
            "-fx-border-width: 3; " +
            "-fx-cursor: hand;",
            typeColor, typeColor, typeColor
        );
        card.setStyle(normalStyle);

        // Nom de l'attaque
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(attaque.getNomAttaque().replace("_", " "));
        nameLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 1);");

        nameRow.getChildren().add(nameLabel);

        // Type badge
        HBox typeBadgeRow = new HBox();
        typeBadgeRow.setAlignment(Pos.CENTER);

        Label typeBadge = new Label(typeLabel.toUpperCase());
        typeBadge.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 9));
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

        VBox powerBox = createStatBox("PWR", String.valueOf(attaque.getPuissanceAttaque()), "#ff6b6b");
        VBox usesBox = createStatBox("PP", String.valueOf(attaque.getNbUtilisations()), "#4dabf7");
        int precision = (int) ((1 - attaque.getProbabiliteEchec()) * 100);
        VBox precisionBox = createStatBox("ACC", precision + "%", "#69db7c");

        statsRow.getChildren().addAll(powerBox, usesBox, precisionBox);

        // Indicateur de sélection
        Label selectionIndicator = createSelectionIndicator();

        card.getChildren().addAll(nameRow, typeBadgeRow, statsRow, selectionIndicator);

        // Tooltip
        Tooltip tooltip = new Tooltip(GameVisual.formatterAttaque(attaque));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);

        // Événements
        card.setOnMouseClicked(e -> handleCardClick(card, attaque, selectionIndicator, typeColor, normalStyle));
        addCardHoverEffects(card, typeColor);

        // Association de la carte avec l'attaque
        cardMap.put(card, attaque);

        return card;
    }

    private VBox createStatBox(String label, String value, String color) {
        VBox box = new VBox(1);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(4, 10, 4, 10));
        box.setStyle(
            "-fx-background-color: rgba(0,0,0,0.3); " +
            "-fx-background-radius: 8;"
        );

        Label labelText = new Label(label);
        labelText.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 9));
        labelText.setTextFill(Color.web("#aaa"));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));
        valueLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(labelText, valueLabel);
        return box;
    }

    public List<Attaque> getSelectedAttacks() {
        return getSelectedItems();
    }
}
