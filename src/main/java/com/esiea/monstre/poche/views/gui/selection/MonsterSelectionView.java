package com.esiea.monstre.poche.views.gui.selection;

import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
 * Vue pour la sélection des monstres - Style Pokémon amélioré.
 * Hérite de AbstractSelectionView pour réutiliser le code commun.
 */
public class MonsterSelectionView extends AbstractSelectionView<Monstre> {
    public MonsterSelectionView(Joueur joueur) {
        super(joueur);
        initializeView();
    }

    @Override
    protected String getMainTitle() {
        return joueur.getNomJoueur();
    }

    @Override
    protected String getSubtitle() {
        return "Composez votre équipe de combat !";
    }

    @Override
    protected String getInstructions() {
        return "Cliquez sur " + Joueur.TAILLE_EQUIPE_MAX + " monstres pour les ajouter à votre équipe";
    }

    @Override
    protected String getCounterText(int selectedCount) {
        return selectedCount + " / " + Joueur.TAILLE_EQUIPE_MAX + " sélectionnés";
    }

    @Override
    protected String getValidateButtonText(int selectedCount) {
        if (selectedCount == Joueur.TAILLE_EQUIPE_MAX) {
            return "LANCER LE COMBAT (" + selectedCount + "/" + Joueur.TAILLE_EQUIPE_MAX + ")";
        } else {
            return "Sélectionnez " + (Joueur.TAILLE_EQUIPE_MAX - selectedCount) + " monstre(s) de plus";
        }
    }

    @Override
    protected int getMaxSelection() {
        return Joueur.TAILLE_EQUIPE_MAX;
    }

    @Override
    protected boolean requiresMaxSelection() {
        return true;
    }

    @Override
    protected void populateCards() {
        for (Monstre monstre : GameResourcesFactory.getInstance().getTousLesMonstres()) {
            VBox monsterCard = createCard(monstre);
            cardsContainer.getChildren().add(monsterCard);
        }
    }

    @Override
    public List<Monstre> getSelectedItems() {
        List<Monstre> selected = new ArrayList<>();
        for (VBox card : selectedCards) {
            Monstre monstre = cardMap.get(card);
            if (monstre != null) {
                selected.add(monstre);
            }
        }
        return selected;
    }

    @Override
    protected VBox createCard(Monstre monstre) {
        String typeLabel = monstre.getTypeMonstre().getLabelType();
        String typeColor = ColorConfig.fromString(typeLabel).getColorCode();

        // Carte principale
        VBox card = new VBox(8);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(200);
        card.setPrefHeight(280);
        card.getStyleClass().add("monster-card");

        String normalStyle = String.format(
            "-fx-background-color: linear-gradient(to bottom, %s 0%%, #1a1a1a 35%%); " +
            "-fx-background-radius: 15; " +
            "-fx-border-radius: 15; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 3;",
            typeColor + "40", typeColor
        );
        card.setStyle(normalStyle);

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
        VBox statsSection = createStatsSection(monstre);

        // Points de vie affichés
        Label hpLabel = new Label(String.format("PV: %.0f", monstre.getPointsDeVieMax()));
        hpLabel.setTextFill(Color.web("#ff6b6b"));
        hpLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 13));

        // Indicateur de sélection
        Label selectionIndicator = createSelectionIndicator();

        card.getChildren().addAll(header, typeBadge, avatar, hpLabel, statsSection, selectionIndicator);

        // Tooltip
        Tooltip tooltip = new Tooltip(GameVisual.formatterMonstre(monstre));
        tooltip.setStyle("-fx-font-size: 12px;");
        Tooltip.install(card, tooltip);

        // Événements
        card.setOnMouseClicked(e -> handleCardClick(card, monstre, selectionIndicator, typeColor, normalStyle));
        addCardHoverEffects(card, typeColor);

        // Association de la carte avec le monstre
        cardMap.put(card, monstre);

        return card;
    }

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

    private VBox createStatsSection(Monstre monstre) {
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

    // Alias pour rétro-compatibilité
    public List<Monstre> getSelectedMonsters() {
        return getSelectedItems();
    }
}
