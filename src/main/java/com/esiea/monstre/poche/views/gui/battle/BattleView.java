package com.esiea.monstre.poche.views.gui.battle;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.inventaire.Objet;

/**
 * Vue de combat style Pokémon - Design moderne inspiré des jeux GBA/DS.
 * Image de fond en plein écran avec interface overlay.
 */
public class BattleView extends StackPane {

    // Couleurs par type (palette Pokémon officielle)
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

    // Éléments d'interface - Monstre Ennemi
    private Label lblEnemyName;
    private Label lblEnemyHpText;
    private Rectangle hpBarEnemyFill;
    private VBox enemySpriteBox;

    // Éléments d'interface - Monstre Joueur
    private Label lblPlayerName;
    private Label lblPlayerHpText;
    private Rectangle hpBarPlayerFill;
    private VBox playerSpriteBox;

    // Boutons d'action
    private Button btnAttack;
    private Button btnItem;
    private Button btnSwitch;
    private Button btnFlee;

    // Zone de logs
    private VBox logBox;
    private ScrollPane logScrollPane;
    private List<String> battleLogBlocks = new ArrayList<>();
    private static final int MAX_LOG_BLOCKS = 50;

    // Panneaux de choix
    private HBox actionButtonsContainer;
    private GridPane attackGrid;
    private HBox monsterChoicePanel;
    private HBox itemChoicePanel;
    private VBox bottomPanel;

    // Variables de jeu
    private Joueur joueur1;
    private Joueur joueur2;
    private boolean isPlayer1Turn;

    public BattleView(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.isPlayer1Turn = true;

        initializeView();
    }

    private void initializeView() {
        this.setAlignment(Pos.CENTER);

        // === IMAGE DE FOND EN PLEIN ÉCRAN ===
        try {
            Image bgImage = new Image(getClass().getResource("/images/exemple_combat.png").toExternalForm());
            ImageView bgView = new ImageView(bgImage);
            bgView.setPreserveRatio(false);
            bgView.fitWidthProperty().bind(this.widthProperty());
            bgView.fitHeightProperty().bind(this.heightProperty());
            this.getChildren().add(bgView);
        } catch (Exception e) {
            this.setStyle("-fx-background-color: linear-gradient(to bottom, #4a90a4 0%, #68b888 50%, #4a7848 100%);");
        }

        // === LAYOUT PRINCIPAL : GAUCHE (combat) + DROITE (logs) ===
        HBox mainLayout = new HBox(0);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.prefWidthProperty().bind(this.widthProperty());
        mainLayout.prefHeightProperty().bind(this.heightProperty());

        // === PARTIE GAUCHE : Zone de combat (monstres + actions) ===
        VBox leftPanel = new VBox(0);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.prefWidthProperty().bind(this.widthProperty().multiply(0.7));
        leftPanel.prefHeightProperty().bind(this.heightProperty());
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        // Zone supérieure (monstres + infos)
        HBox battleArea = createBattleArea();
        VBox.setVgrow(battleArea, Priority.ALWAYS);

        // Zone inférieure (actions)
        bottomPanel = createActionBottomPanel();

        leftPanel.getChildren().addAll(battleArea, bottomPanel);

        // === PARTIE DROITE : Zone des logs ===
        VBox rightPanel = createLogPanel();
        rightPanel.prefWidthProperty().bind(this.widthProperty().multiply(0.3));
        rightPanel.minWidthProperty().bind(this.widthProperty().multiply(0.25));
        rightPanel.maxWidthProperty().bind(this.widthProperty().multiply(0.35));
        rightPanel.prefHeightProperty().bind(this.heightProperty());

        mainLayout.getChildren().addAll(leftPanel, rightPanel);
        this.getChildren().add(mainLayout);
    }

    /**
     * Zone de combat avec les deux monstres face à face.
     */
    private HBox createBattleArea() {
        HBox area = new HBox();
        area.setAlignment(Pos.CENTER);
        area.setPadding(new Insets(20, 30, 10, 30));

        // === CÔTÉ JOUEUR (gauche) ===
        VBox playerSide = new VBox(10);
        playerSide.setAlignment(Pos.BOTTOM_CENTER);
        playerSide.setPrefWidth(280);
        HBox.setHgrow(playerSide, Priority.ALWAYS);

        // Info box joueur
        VBox playerInfoBox = createPlayerInfoBox();
        
        // Sprite joueur
        playerSpriteBox = createMonsterSpriteBox(joueur1.getMonstreActuel(), true);

        playerSide.getChildren().addAll(playerInfoBox, playerSpriteBox);

        // === ZONE CENTRALE (VS) ===
        VBox centerZone = new VBox();
        centerZone.setAlignment(Pos.CENTER);
        centerZone.setPrefWidth(80);

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        vsLabel.setTextFill(Color.WHITE);
        vsLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0, 2, 2);");
        centerZone.getChildren().add(vsLabel);

        // === CÔTÉ ENNEMI (droite) ===
        VBox enemySide = new VBox(10);
        enemySide.setAlignment(Pos.BOTTOM_CENTER);
        enemySide.setPrefWidth(280);
        HBox.setHgrow(enemySide, Priority.ALWAYS);

        // Info box ennemi
        VBox enemyInfoBox = createEnemyInfoBox();

        // Sprite ennemi
        enemySpriteBox = createMonsterSpriteBox(joueur2.getMonstreActuel(), false);

        enemySide.getChildren().addAll(enemyInfoBox, enemySpriteBox);

        area.getChildren().addAll(playerSide, centerZone, enemySide);
        return area;
    }

    /**
     * Crée le panneau d'info du joueur.
     */
    private VBox createPlayerInfoBox() {
        Monstre m = joueur1.getMonstreActuel();
        String typeColor = TYPE_COLORS.getOrDefault(m.getTypeMonstre().getLabelType(), "#A8A878");

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12, 18, 12, 18));
        box.setMaxWidth(250);
        box.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.92); " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: " + typeColor + "; " +
            "-fx-border-width: 3; " +
            "-fx-border-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);"
        );

        // Nom du joueur
        Label playerLabel = new Label(joueur1.getNomJoueur());
        playerLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        playerLabel.setTextFill(Color.web("#666"));

        // Nom du monstre + type
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER);

        lblPlayerName = new Label(m.getNomMonstre());
        lblPlayerName.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPlayerName.setTextFill(Color.web("#303030"));

        Label typeLabel = new Label(m.getTypeMonstre().getLabelType());
        typeLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setPadding(new Insets(2, 8, 2, 8));
        typeLabel.setStyle("-fx-background-color: " + typeColor + "; -fx-background-radius: 10;");

        nameRow.getChildren().addAll(lblPlayerName, typeLabel);

        // Barre de PV
        HBox hpRow = createHpBar(m, true);

        // Valeur PV
        lblPlayerHpText = new Label(String.format("%.0f / %.0f PV", m.getPointsDeVie(), m.getPointsDeVieMax()));
        lblPlayerHpText.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblPlayerHpText.setTextFill(Color.web("#404040"));

        box.getChildren().addAll(playerLabel, nameRow, hpRow, lblPlayerHpText);
        return box;
    }

    /**
     * Crée le panneau d'info de l'ennemi.
     */
    private VBox createEnemyInfoBox() {
        Monstre m = joueur2.getMonstreActuel();
        String typeColor = TYPE_COLORS.getOrDefault(m.getTypeMonstre().getLabelType(), "#A8A878");

        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12, 18, 12, 18));
        box.setMaxWidth(250);
        box.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.92); " +
            "-fx-background-radius: 15; " +
            "-fx-border-color: " + typeColor + "; " +
            "-fx-border-width: 3; " +
            "-fx-border-radius: 15; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);"
        );

        // Nom du joueur
        Label playerLabel = new Label(joueur2.getNomJoueur());
        playerLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        playerLabel.setTextFill(Color.web("#666"));

        // Nom du monstre + type
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER);

        lblEnemyName = new Label(m.getNomMonstre());
        lblEnemyName.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblEnemyName.setTextFill(Color.web("#303030"));

        Label typeLabel = new Label(m.getTypeMonstre().getLabelType());
        typeLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setPadding(new Insets(2, 8, 2, 8));
        typeLabel.setStyle("-fx-background-color: " + typeColor + "; -fx-background-radius: 10;");

        nameRow.getChildren().addAll(lblEnemyName, typeLabel);

        // Barre de PV
        HBox hpRow = createHpBar(m, false);

        // Valeur PV
        lblEnemyHpText = new Label(String.format("%.0f / %.0f PV", m.getPointsDeVie(), m.getPointsDeVieMax()));
        lblEnemyHpText.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblEnemyHpText.setTextFill(Color.web("#404040"));

        box.getChildren().addAll(playerLabel, nameRow, hpRow, lblEnemyHpText);
        return box;
    }

    /**
     * Crée une barre de PV.
     */
    private HBox createHpBar(Monstre m, boolean isPlayer) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER);

        Label hpLabel = new Label("PV");
        hpLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        hpLabel.setTextFill(Color.web("#f8a800"));

        StackPane barContainer = new StackPane();
        barContainer.setAlignment(Pos.CENTER_LEFT);

        double maxWidth = 160;
        double ratio = Math.max(0, m.getPointsDeVie() / m.getPointsDeVieMax());

        Rectangle bg = new Rectangle(maxWidth, 12);
        bg.setFill(Color.web("#404040"));
        bg.setArcWidth(8);
        bg.setArcHeight(8);

        Rectangle fill = new Rectangle(maxWidth * ratio, 12);
        fill.setArcWidth(8);
        fill.setArcHeight(8);
        if (ratio > 0.5) fill.setFill(Color.web("#48d048"));
        else if (ratio > 0.2) fill.setFill(Color.web("#f8c800"));
        else fill.setFill(Color.web("#f85858"));

        barContainer.getChildren().addAll(bg, fill);

        if (isPlayer) {
            hpBarPlayerFill = fill;
        } else {
            hpBarEnemyFill = fill;
        }

        row.getChildren().addAll(hpLabel, barContainer);
        return row;
    }

    /**
     * Crée le sprite d'un monstre.
     */
    private VBox createMonsterSpriteBox(Monstre m, boolean isPlayer) {
        String typeColor = TYPE_COLORS.getOrDefault(m.getTypeMonstre().getLabelType(), "#A8A878");
        double size = isPlayer ? 100 : 90;

        VBox spriteBox = new VBox(5);
        spriteBox.setAlignment(Pos.CENTER);

        // Cercle avec initiales
        Label sprite = new Label(m.getNomMonstre().substring(0, Math.min(2, m.getNomMonstre().length())).toUpperCase());
        sprite.setFont(Font.font("System", FontWeight.BOLD, isPlayer ? 38 : 34));
        sprite.setTextFill(Color.WHITE);
        sprite.setPrefSize(size, size);
        sprite.setMinSize(size, size);
        sprite.setMaxSize(size, size);
        sprite.setAlignment(Pos.CENTER);
        sprite.setStyle(String.format(
            "-fx-background-color: radial-gradient(center 50%% 35%%, radius 55%%, %s, derive(%s, -30%%)); " +
            "-fx-background-radius: 50%%; " +
            "-fx-border-color: derive(%s, -45%%); " +
            "-fx-border-width: 4; " +
            "-fx-border-radius: 50%%; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 8);",
            typeColor, typeColor, typeColor
        ));

        // Ombre au sol
        Rectangle shadow = new Rectangle(size * 1.2, 15);
        shadow.setFill(Color.rgb(0, 0, 0, 0.25));
        shadow.setArcWidth(size);
        shadow.setArcHeight(15);

        spriteBox.getChildren().addAll(sprite, shadow);
        return spriteBox;
    }

    /**
     * Crée le panneau inférieur avec les actions (sans les logs).
     */
    private VBox createActionBottomPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15, 20, 20, 20));
        panel.setMinHeight(180);
        panel.setPrefHeight(200);
        panel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(30, 40, 50, 0.95), rgba(20, 25, 35, 0.98)); " +
            "-fx-border-color: #5a6a7a; " +
            "-fx-border-width: 3 0 0 0;"
        );

        // Zone des boutons d'action
        actionButtonsContainer = createActionPanel();

        // Panneaux de choix (cachés par défaut)
        attackGrid = new GridPane();
        attackGrid.setHgap(10);
        attackGrid.setVgap(8);
        attackGrid.setAlignment(Pos.CENTER);
        attackGrid.setVisible(false);
        attackGrid.setManaged(false);

        monsterChoicePanel = new HBox(10);
        monsterChoicePanel.setAlignment(Pos.CENTER);
        monsterChoicePanel.setPadding(new Insets(8, 0, 0, 0));
        monsterChoicePanel.setVisible(false);
        monsterChoicePanel.setManaged(false);

        itemChoicePanel = new HBox(10);
        itemChoicePanel.setAlignment(Pos.CENTER);
        itemChoicePanel.setPadding(new Insets(8, 0, 0, 0));
        itemChoicePanel.setVisible(false);
        itemChoicePanel.setManaged(false);

        panel.getChildren().addAll(actionButtonsContainer, attackGrid, monsterChoicePanel, itemChoicePanel);
        return panel;
    }

    /**
     * Crée le panneau des logs à droite de l'écran.
     */
    private VBox createLogPanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(new Insets(15, 15, 15, 15));
        panel.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(25, 35, 50, 0.96), rgba(15, 20, 30, 0.98)); " +
            "-fx-border-color: #4a5a7a; " +
            "-fx-border-width: 0 0 0 3;"
        );

        // Titre
        Label title = new Label("⚔ LOGS DU TOUR");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setTextFill(Color.web("#c0d0e0"));
        title.setPadding(new Insets(0, 0, 5, 0));

        // Conteneur des logs avec scroll
        logBox = new VBox(8);
        logBox.setAlignment(Pos.TOP_LEFT);
        logBox.setPadding(new Insets(10, 5, 10, 5));

        logScrollPane = new ScrollPane(logBox);
        logScrollPane.setFitToWidth(true);
        logScrollPane.setStyle(
            "-fx-background: transparent; " +
            "-fx-background-color: transparent; " +
            "-fx-border-color: transparent;"
        );
        logScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        logScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(logScrollPane, Priority.ALWAYS);

        // Message initial
        battleLogBlocks.add("Le combat commence ! Que doit faire " + joueur1.getMonstreActuel().getNomMonstre() + " ?");
        refreshLogDisplay();

        panel.getChildren().addAll(title, logScrollPane);
        return panel;
    }

    /**
     * Crée le panneau inférieur (logs + actions).
     * @deprecated Remplacé par createActionBottomPanel() et createLogPanel()
     */
    @Deprecated
    private VBox createBottomPanel() {
        return createActionBottomPanel();
    }

    /**
     * Crée le panneau des boutons d'action.
     */
    private HBox createActionPanel() {
        HBox panel = new HBox();
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15, 25, 15, 15));
        panel.setPrefWidth(260);
        panel.setMinWidth(240);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        btnAttack = createActionButton("ATTAQUE", "#e85858", "#c84848");
        btnItem = createActionButton("SAC", "#58b858", "#48a848");
        btnSwitch = createActionButton("POKEMON", "#5898e8", "#4888d8");
        btnFlee = createActionButton("FUITE", "#888898", "#707080");

        grid.add(btnAttack, 0, 0);
        grid.add(btnItem, 1, 0);
        grid.add(btnSwitch, 0, 1);
        grid.add(btnFlee, 1, 1);

        panel.getChildren().add(grid);
        return panel;
    }

    /**
     * Crée un bouton d'action stylisé.
     */
    private Button createActionButton(String text, String color, String darkColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.BOLD, 12));
        btn.setPrefWidth(105);
        btn.setPrefHeight(42);

        String normalStyle = String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 0 0 4 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            color, darkColor
        );
        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(String.format(
            "-fx-background-color: derive(%s, 12%%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 0 0 4 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            color, darkColor
        )));

        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

        btn.setOnMousePressed(e -> btn.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 4 0 0 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            darkColor, darkColor
        )));

        btn.setOnMouseReleased(e -> btn.setStyle(normalStyle));

        return btn;
    }

    /**
     * Rafraîchit l'affichage des logs.
     */
    private void refreshLogDisplay() {
        logBox.getChildren().clear();

        // Afficher tous les blocs de logs
        for (int i = 0; i < battleLogBlocks.size(); i++) {
            String block = battleLogBlocks.get(i);
            
            VBox blockBox = new VBox(3);
            blockBox.setPadding(new Insets(8, 12, 8, 12));
            
            // Le dernier bloc est mis en évidence
            boolean isLast = (i == battleLogBlocks.size() - 1);
            
            if (isLast) {
                blockBox.setStyle(
                    "-fx-background-color: rgba(80, 100, 130, 0.85); " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-color: #6a8aaa; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 10;"
                );
            } else {
                blockBox.setStyle(
                    "-fx-background-color: rgba(50, 60, 75, 0.7); " +
                    "-fx-background-radius: 8;"
                );
            }

            // Afficher chaque ligne du bloc
            String[] lines = block.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                Label lbl = new Label(line);
                lbl.setWrapText(true);
                lbl.maxWidthProperty().bind(logBox.widthProperty().subtract(30));

                if (isLast) {
                    lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
                    lbl.setTextFill(Color.web("#f0f8ff"));
                } else {
                    lbl.setFont(Font.font("System", FontWeight.NORMAL, 11));
                    lbl.setTextFill(Color.web("#a0b0c0"));
                }

                blockBox.getChildren().add(lbl);
            }

            logBox.getChildren().add(blockBox);
        }
        
        // Scroll automatique vers le bas
        if (logScrollPane != null) {
            logScrollPane.layout();
            logScrollPane.setVvalue(1.0);
        }
    }

    /**
     * Ajoute un bloc de log complet.
     */
    public void updateBattleLog(String message) {
        if (message == null || message.isEmpty()) return;

        // Découper les messages en blocs séparés par ===
        String[] rawBlocks = message.split("={10,}");

        for (String rawBlock : rawBlocks) {
            String block = rawBlock.trim();
            if (block.isEmpty()) continue;

            // Nettoyer les lignes de séparation
            block = block.replace("----------------------------------------", "").trim();

            if (!block.isEmpty()) {
                battleLogBlocks.add(block);
            }
        }

        // Limiter le nombre de blocs
        while (battleLogBlocks.size() > MAX_LOG_BLOCKS) {
            battleLogBlocks.remove(0);
        }

        refreshLogDisplay();

        // Animation
        FadeTransition fade = new FadeTransition(Duration.millis(250), logBox);
        fade.setFromValue(0.7);
        fade.setToValue(1.0);
        fade.play();
    }

    public void clearBattleLog() {
        battleLogBlocks.clear();
        Monstre current = isPlayer1Turn ? joueur1.getMonstreActuel() : joueur2.getMonstreActuel();
        battleLogBlocks.add("Que doit faire " + current.getNomMonstre() + " ?");
        refreshLogDisplay();
    }

    /**
     * Met à jour l'affichage des monstres.
     */
    public void updatePokemonDisplay() {
        // Joueur
        if (joueur1.getMonstreActuel() != null) {
            Monstre m1 = joueur1.getMonstreActuel();
            double ratio = Math.max(0, m1.getPointsDeVie() / m1.getPointsDeVieMax());

            lblPlayerName.setText(m1.getNomMonstre());
            lblPlayerHpText.setText(String.format("%.0f / %.0f PV", m1.getPointsDeVie(), m1.getPointsDeVieMax()));

            if (hpBarPlayerFill != null) {
                hpBarPlayerFill.setWidth(160 * ratio);
                if (ratio > 0.5) hpBarPlayerFill.setFill(Color.web("#48d048"));
                else if (ratio > 0.2) hpBarPlayerFill.setFill(Color.web("#f8c800"));
                else hpBarPlayerFill.setFill(Color.web("#f85858"));
            }

            updateSpriteBox(playerSpriteBox, m1, true);
        }

        // Ennemi
        if (joueur2.getMonstreActuel() != null) {
            Monstre m2 = joueur2.getMonstreActuel();
            double ratio = Math.max(0, m2.getPointsDeVie() / m2.getPointsDeVieMax());

            lblEnemyName.setText(m2.getNomMonstre());
            lblEnemyHpText.setText(String.format("%.0f / %.0f PV", m2.getPointsDeVie(), m2.getPointsDeVieMax()));

            if (hpBarEnemyFill != null) {
                hpBarEnemyFill.setWidth(160 * ratio);
                if (ratio > 0.5) hpBarEnemyFill.setFill(Color.web("#48d048"));
                else if (ratio > 0.2) hpBarEnemyFill.setFill(Color.web("#f8c800"));
                else hpBarEnemyFill.setFill(Color.web("#f85858"));
            }

            updateSpriteBox(enemySpriteBox, m2, false);
        }
    }

    private void updateSpriteBox(VBox spriteBox, Monstre m, boolean isPlayer) {
        if (spriteBox == null || m == null) return;

        String typeColor = TYPE_COLORS.getOrDefault(m.getTypeMonstre().getLabelType(), "#A8A878");
        double size = isPlayer ? 100 : 90;

        spriteBox.getChildren().clear();

        Label sprite = new Label(m.getNomMonstre().substring(0, Math.min(2, m.getNomMonstre().length())).toUpperCase());
        sprite.setFont(Font.font("System", FontWeight.BOLD, isPlayer ? 38 : 34));
        sprite.setTextFill(Color.WHITE);
        sprite.setPrefSize(size, size);
        sprite.setMinSize(size, size);
        sprite.setMaxSize(size, size);
        sprite.setAlignment(Pos.CENTER);
        sprite.setStyle(String.format(
            "-fx-background-color: radial-gradient(center 50%% 35%%, radius 55%%, %s, derive(%s, -30%%)); " +
            "-fx-background-radius: 50%%; " +
            "-fx-border-color: derive(%s, -45%%); " +
            "-fx-border-width: 4; " +
            "-fx-border-radius: 50%%; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 8);",
            typeColor, typeColor, typeColor
        ));

        Rectangle shadow = new Rectangle(size * 1.2, 15);
        shadow.setFill(Color.rgb(0, 0, 0, 0.25));
        shadow.setArcWidth(size);
        shadow.setArcHeight(15);

        spriteBox.getChildren().addAll(sprite, shadow);
    }

    /**
     * Affiche les attaques disponibles.
     */
    public void displayAttackChoices(List<Attaque> attaques, Consumer<Attaque> onSelect) {
        attackGrid.getChildren().clear();

        battleLogBlocks.add("Choisissez une attaque :");
        refreshLogDisplay();

        int col = 0, row = 0;
        
        // Afficher les attaques normales
        if (attaques != null) {
            for (Attaque a : attaques) {
                String typeColor = TYPE_COLORS.getOrDefault(a.getTypeAttaque().getLabelType(), "#A8A878");
                boolean hasNoPP = a.getNbUtilisations() <= 0;

                Button btn = new Button();
                btn.setPrefWidth(165);
                btn.setPrefHeight(48);

                VBox content = new VBox(2);
                content.setAlignment(Pos.CENTER_LEFT);
                content.setPadding(new Insets(0, 8, 0, 8));

                Label nameLabel = new Label(a.getNomAttaque().replace("_", " "));
                nameLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                nameLabel.setTextFill(hasNoPP ? Color.GRAY : Color.WHITE);

                String ppText = hasNoPP ? "PP VIDE" : ("PP " + a.getNbUtilisations());
                Label infoLabel = new Label(a.getTypeAttaque().getLabelType() + " | " + ppText + " | PWR " + a.getPuissanceAttaque());
                infoLabel.setFont(Font.font("System", 9));
                infoLabel.setTextFill(hasNoPP ? Color.DARKGRAY : Color.web("#ddd"));

                content.getChildren().addAll(nameLabel, infoLabel);
                btn.setGraphic(content);

                String normalStyle;
                if (hasNoPP) {
                    // Style grisé pour les attaques sans PP
                    normalStyle = "-fx-background-color: #444; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #333; " +
                        "-fx-border-width: 0 0 3 0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-opacity: 0.6;";
                    btn.setDisable(true);
                } else {
                    normalStyle = String.format(
                        "-fx-background-color: %s; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: derive(%s, -30%%); " +
                        "-fx-border-width: 0 0 3 0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;",
                        typeColor, typeColor
                    );
                }
                btn.setStyle(normalStyle);

                if (!hasNoPP) {
                    btn.setOnMouseEntered(e -> btn.setStyle(String.format(
                        "-fx-background-color: derive(%s, 15%%); " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: derive(%s, -30%%); " +
                        "-fx-border-width: 0 0 3 0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-cursor: hand;",
                        typeColor, typeColor
                    )));
                    final String savedStyle = normalStyle;
                    btn.setOnMouseExited(e -> btn.setStyle(savedStyle));

                    btn.setOnAction(e -> {
                        onSelect.accept(a);
                        hideAllChoices();
                    });
                }

                attackGrid.add(btn, col, row);
                col++;
                if (col >= 2) { col = 0; row++; }
            }
        }
        
        // Bouton "Mains nues" toujours disponible
        Button btnBareHands = new Button();
        btnBareHands.setPrefWidth(165);
        btnBareHands.setPrefHeight(48);
        
        VBox bareHandsContent = new VBox(2);
        bareHandsContent.setAlignment(Pos.CENTER_LEFT);
        bareHandsContent.setPadding(new Insets(0, 8, 0, 8));
        
        Label bareHandsName = new Label("MAINS NUES");
        bareHandsName.setFont(Font.font("System", FontWeight.BOLD, 12));
        bareHandsName.setTextFill(Color.WHITE);
        
        Label bareHandsInfo = new Label("Normal | PP infini | PWR faible");
        bareHandsInfo.setFont(Font.font("System", 9));
        bareHandsInfo.setTextFill(Color.web("#ddd"));
        
        bareHandsContent.getChildren().addAll(bareHandsName, bareHandsInfo);
        btnBareHands.setGraphic(bareHandsContent);
        
        String bareHandsStyle = "-fx-background-color: #8B4513; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #654321; " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;";
        btnBareHands.setStyle(bareHandsStyle);
        
        btnBareHands.setOnMouseEntered(e -> btnBareHands.setStyle(
            "-fx-background-color: #A0522D; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #654321; " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        btnBareHands.setOnMouseExited(e -> btnBareHands.setStyle(bareHandsStyle));
        
        btnBareHands.setOnAction(e -> {
            onSelect.accept(null); // null représente l'attaque à mains nues
            hideAllChoices();
        });
        
        attackGrid.add(btnBareHands, col, row);
        col++;
        if (col >= 2) { col = 0; row++; }

        // Bouton retour
        Button btnBack = createBackButton();
        btnBack.setOnAction(e -> hideAllChoices());
        attackGrid.add(btnBack, col, row);

        attackGrid.setVisible(true);
        attackGrid.setManaged(true);
        actionButtonsContainer.setVisible(false);
        actionButtonsContainer.setManaged(false);
    }

    /**
     * Affiche les monstres disponibles.
     */
    public void displayMonsterChoices(List<Monstre> monstres, Consumer<Monstre> onSelect) {
        monsterChoicePanel.getChildren().clear();

        if (monstres == null || monstres.isEmpty()) {
            battleLogBlocks.add("Aucun monstre disponible !");
            refreshLogDisplay();
            return;
        }

        battleLogBlocks.add("Choisissez un monstre :");
        refreshLogDisplay();

        for (Monstre m : monstres) {
            String typeColor = TYPE_COLORS.getOrDefault(m.getTypeMonstre().getLabelType(), "#A8A878");
            double hpRatio = Math.max(0, m.getPointsDeVie() / m.getPointsDeVieMax());

            VBox card = new VBox(4);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(8, 12, 8, 12));
            card.setPrefWidth(110);

            String normalStyle = String.format(
                "-fx-background-color: %s; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: derive(%s, -30%%); " +
                "-fx-border-width: 0 0 3 0; " +
                "-fx-border-radius: 10; " +
                "-fx-cursor: hand;",
                typeColor, typeColor
            );
            card.setStyle(normalStyle);

            Label nameLabel = new Label(m.getNomMonstre());
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
            nameLabel.setTextFill(Color.WHITE);

            String hpColor = hpRatio > 0.5 ? "#48d048" : hpRatio > 0.2 ? "#f8c800" : "#f85858";
            Label hpLabel = new Label(String.format("%.0f/%.0f PV", m.getPointsDeVie(), m.getPointsDeVieMax()));
            hpLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
            hpLabel.setTextFill(Color.web(hpColor));

            card.getChildren().addAll(nameLabel, hpLabel);

            card.setOnMouseEntered(e -> card.setStyle(String.format(
                "-fx-background-color: derive(%s, 15%%); " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: derive(%s, -30%%); " +
                "-fx-border-width: 0 0 3 0; " +
                "-fx-border-radius: 10; " +
                "-fx-cursor: hand;",
                typeColor, typeColor
            )));
            card.setOnMouseExited(e -> card.setStyle(normalStyle));

            card.setOnMouseClicked(e -> {
                onSelect.accept(m);
                hideAllChoices();
            });

            monsterChoicePanel.getChildren().add(card);
        }

        Button btnBack = createBackButton();
        btnBack.setOnAction(e -> hideAllChoices());
        monsterChoicePanel.getChildren().add(btnBack);

        monsterChoicePanel.setVisible(true);
        monsterChoicePanel.setManaged(true);
        actionButtonsContainer.setVisible(false);
        actionButtonsContainer.setManaged(false);
    }

    /**
     * Affiche les objets disponibles.
     */
    public void displayItemChoices(List<Objet> objets, Consumer<Objet> onSelect) {
        itemChoicePanel.getChildren().clear();

        if (objets == null || objets.isEmpty()) {
            battleLogBlocks.add("Aucun objet disponible !");
            refreshLogDisplay();
            return;
        }

        battleLogBlocks.add("Choisissez un objet :");
        refreshLogDisplay();

        for (Objet obj : objets) {
            String objName = obj.getNomObjet();
            final String objColor;

            if (objName.toLowerCase().contains("medicament") || objName.toLowerCase().contains("anti")) {
                objColor = "#e88848";
            } else if (objName.toLowerCase().contains("degat")) {
                objColor = "#e85858";
            } else if (objName.toLowerCase().contains("vitesse")) {
                objColor = "#5898e8";
            } else {
                objColor = "#58b858";
            }

            Button btn = new Button(objName.replace("_", " "));
            btn.setPrefWidth(120);
            btn.setPrefHeight(40);
            btn.setFont(Font.font("System", FontWeight.BOLD, 10));

            String normalStyle = String.format(
                "-fx-background-color: %s; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: derive(%s, -30%%); " +
                "-fx-border-width: 0 0 3 0; " +
                "-fx-border-radius: 8; " +
                "-fx-cursor: hand;",
                objColor, objColor
            );
            btn.setStyle(normalStyle);

            btn.setOnMouseEntered(e -> btn.setStyle(String.format(
                "-fx-background-color: derive(%s, 15%%); " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: derive(%s, -30%%); " +
                "-fx-border-width: 0 0 3 0; " +
                "-fx-border-radius: 8; " +
                "-fx-cursor: hand;",
                objColor, objColor
            )));
            btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

            btn.setOnAction(e -> {
                onSelect.accept(obj);
                hideAllChoices();
            });

            itemChoicePanel.getChildren().add(btn);
        }

        Button btnBack = createBackButton();
        btnBack.setOnAction(e -> hideAllChoices());
        itemChoicePanel.getChildren().add(btnBack);

        itemChoicePanel.setVisible(true);
        itemChoicePanel.setManaged(true);
        actionButtonsContainer.setVisible(false);
        actionButtonsContainer.setManaged(false);
    }

    /**
     * Crée un bouton retour standard.
     */
    private Button createBackButton() {
        Button btn = new Button("RETOUR");
        btn.setPrefWidth(80);
        btn.setPrefHeight(32);
        btn.setFont(Font.font("System", FontWeight.BOLD, 10));
        btn.setStyle(
            "-fx-background-color: #606070; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 6; " +
            "-fx-border-color: #505060; " +
            "-fx-border-width: 0 0 2 0; " +
            "-fx-border-radius: 6; " +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    /**
     * Cache tous les panneaux de choix.
     */
    public void hideAllChoices() {
        attackGrid.setVisible(false);
        attackGrid.setManaged(false);
        attackGrid.getChildren().clear();

        monsterChoicePanel.setVisible(false);
        monsterChoicePanel.setManaged(false);
        monsterChoicePanel.getChildren().clear();

        itemChoicePanel.setVisible(false);
        itemChoicePanel.setManaged(false);
        itemChoicePanel.getChildren().clear();

        actionButtonsContainer.setVisible(true);
        actionButtonsContainer.setManaged(true);

        clearBattleLog();
    }

    // Alias pour compatibilité
    public void hideAttackChoices() {
        hideAllChoices();
    }

    public void setTurn(boolean player1Turn) {
        this.isPlayer1Turn = player1Turn;
        clearBattleLog();
    }

    // Getters
    public Button getBtnAttack() { return btnAttack; }
    public Button getBtnItem() { return btnItem; }
    public Button getBtnSwitch() { return btnSwitch; }
    public Button getBtnFlee() { return btnFlee; }
    public Joueur getJoueur1() { return joueur1; }
    public Joueur getJoueur2() { return joueur2; }
    public boolean isPlayer1Turn() { return isPlayer1Turn; }
    public void setIsPlayer1Turn(boolean isPlayer1Turn) { this.isPlayer1Turn = isPlayer1Turn; }
}
