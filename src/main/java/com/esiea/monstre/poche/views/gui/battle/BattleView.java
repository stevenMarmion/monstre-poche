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
import java.util.function.Consumer;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.items.Objet;
import com.esiea.monstre.poche.views.gui.components.UIComponentFactory;
import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

/**
 * Vue de combat style Pokémon - Design moderne inspiré des jeux GBA/DS.
 * Image de fond en plein écran avec interface overlay.
 */
public class BattleView extends StackPane {
    public static final int ATTACK_BUTTON_WIDTH = 220;
    public static final int ATTACK_BUTTON_HEIGHT = 65;
    public static final int MONSTER_CARD_WIDTH = 110;
    public static final double HP_BAR_MAX_WIDTH = 160.0;
    public static final int ATTACK_GRID_COLUMNS = 2;
    public static final double HP_RATIO_HIGH_THRESHOLD = 0.5;
    public static final double HP_RATIO_MEDIUM_THRESHOLD = 0.2;
    public static final int MAX_LOG_BLOCKS = 50;

    private VBox playerInfoBox;  // carte d'info contenant le joueur, pokemon et son sprite
    private VBox enemyInfoBox;

    // Éléments d'interface - Monstre Joueur
    private Label lblPlayerName;
    private Label lblPlayerHpText;
    private Label lblPlayerMonsterType;

    private Rectangle hpBarPlayerFill;
    private VBox playerSpriteBox;
    private VBox enemySpriteBox;

    // Éléments d'interface - Monstre Ennemi
    private Label lblEnemyName;
    private Label lblEnemyHpText;
    private Label lblEnemyMonsterType;
    private Rectangle hpBarEnemyFill;

    // Boutons d'action
    private Button btnAttack;
    private Button btnItem;
    private Button btnSwitch;

    // Zone de logs
    private VBox logBox;
    private ScrollPane logScrollPane;
    private List<String> battleLogBlocks = new ArrayList<>();

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
        VBox playerInfoBox = createPlayerInfoBox(joueur1, true);
        playerSpriteBox = createMonsterSpriteBox(joueur1.getMonstreActuel(), true);
        playerSide.getChildren().addAll(playerInfoBox, playerSpriteBox);

        // === ZONE CENTRALE (VS) ===
        VBox centerZone = new VBox();
        centerZone.setAlignment(Pos.CENTER);
        centerZone.setPrefWidth(80);

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 28));
        vsLabel.setTextFill(Color.WHITE);
        vsLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 8, 0, 2, 2);");
        centerZone.getChildren().add(vsLabel);

        // === CÔTÉ ENNEMI (droite) ===
        VBox enemySide = new VBox(10);
        enemySide.setAlignment(Pos.BOTTOM_CENTER);
        enemySide.setPrefWidth(280);
        HBox.setHgrow(enemySide, Priority.ALWAYS);

        VBox enemyInfoBox = createPlayerInfoBox(joueur2, false);
        enemySpriteBox = createMonsterSpriteBox(joueur2.getMonstreActuel(), false);
        enemySide.getChildren().addAll(enemyInfoBox, enemySpriteBox);

        area.getChildren().addAll(playerSide, centerZone, enemySide);
        return area;
    }

    /**
     * Crée le panneau d'info du joueur.
     * @param joueur Le joueur dont on veut afficher les informations
     * @param isPlayer true si c'est le joueur 1 (à gauche), false si c'est l'ennemi (à droite)
     */
    private VBox createPlayerInfoBox(Joueur joueur, boolean isPlayer) {
        Monstre m = joueur.getMonstreActuel();
        String typeColor = ColorConfig.fromString(m.getTypeMonstre().getLabelType()).getColorCode();

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
        Label playerLabel = new Label(joueur.getNomJoueur());
        playerLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 11));
        playerLabel.setTextFill(Color.web("#666"));

        // Nom du monstre + type
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER);

        Label monsterNameLabel = new Label(m.getNomMonstre());
        monsterNameLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        monsterNameLabel.setTextFill(Color.web("#303030"));

        // Stocker la référence selon si c'est le joueur ou l'ennemi
        if (isPlayer) {
            lblPlayerName = monsterNameLabel;
        } else {
            lblEnemyName = monsterNameLabel;
        }

        Label typeLabel = new Label(m.getTypeMonstre().getLabelType());
        typeLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 10));
        typeLabel.setTextFill(Color.WHITE);
        typeLabel.setPadding(new Insets(2, 8, 2, 8));
        typeLabel.setStyle("-fx-background-color: " + ColorConfig.fromString(m.getTypeMonstre().getLabelType()).getColorCode() + "; -fx-background-radius: 10;");

        // Stocker la référence selon si c'est le joueur ou l'ennemi
        if (isPlayer) {
            lblPlayerMonsterType = typeLabel;
        } else {
            lblEnemyMonsterType = typeLabel;
        }

        nameRow.getChildren().addAll(monsterNameLabel, typeLabel);

        // Barre de PV
        HBox hpRow = createHpBar(m, isPlayer);

        // Valeur PV
        Label hpTextLabel = new Label(UIComponentFactory.formatHpText(m.getPointsDeVie(), m.getPointsDeVieMax()));
        hpTextLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));
        hpTextLabel.setTextFill(Color.web("#404040"));

        // Stocker la référence selon si c'est le joueur ou l'ennemi
        if (isPlayer) {
            lblPlayerHpText = hpTextLabel;
        } else {
            lblEnemyHpText = hpTextLabel;
        }

        box.getChildren().addAll(playerLabel, nameRow, hpRow, hpTextLabel);

        if (isPlayer) {
            playerInfoBox = box;
        } else {
            enemyInfoBox = box;
        }
        return box;
    }

    /**
     * Crée une barre de PV.
     */
    private HBox createHpBar(Monstre m, boolean isPlayer) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER);

        Label hpLabel = new Label("PV");
        hpLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 11));
        hpLabel.setTextFill(Color.web("#f8a800"));

        StackPane barContainer = new StackPane();
        barContainer.setAlignment(Pos.CENTER_LEFT);

        double maxWidth = HP_BAR_MAX_WIDTH;
        double ratio = Math.max(0, m.getPointsDeVie() / m.getPointsDeVieMax());

        Rectangle bg = new Rectangle(maxWidth, 12);
        bg.setFill(Color.web("#404040"));
        bg.setArcWidth(8);
        bg.setArcHeight(8);

        Rectangle fill = new Rectangle(maxWidth * ratio, 12);
        fill.setArcWidth(8);
        fill.setArcHeight(8);
        fill.setFill(Color.web(UIComponentFactory.getHpColor(ratio)));

        barContainer.getChildren().addAll(bg, fill);

        // Stocker la référence selon si c'est le joueur ou l'ennemi
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
        String typeColor = ColorConfig.fromString(m.getTypeMonstre().getLabelType()).getColorCode();
        double size = isPlayer ? 100 : 90;

        VBox spriteBox = new VBox(5);
        spriteBox.setAlignment(Pos.CENTER);

        // Cercle avec initiales
        Label sprite = new Label(m.getNomMonstre().substring(0, Math.min(2, m.getNomMonstre().length())).toUpperCase());
        sprite.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, isPlayer ? 38 : 34));
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
        title.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 14));
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

        btnAttack = UIComponentFactory.createActionButton("ATTAQUE", ColorConfig.ACTION_ATTACK_COLOR.getColorCode(), ColorConfig.ACTION_ATTACK_DARK.getColorCode());
        btnItem = UIComponentFactory.createActionButton("SAC", ColorConfig.ACTION_ITEM_COLOR.getColorCode(), ColorConfig.ACTION_ITEM_DARK.getColorCode());
        btnSwitch = UIComponentFactory.createActionButton("POKEMON", ColorConfig.ACTION_SWITCH_COLOR.getColorCode(), ColorConfig.ACTION_SWITCH_DARK.getColorCode());

        grid.add(btnAttack, 0, 0);
        grid.add(btnItem, 1, 0);
        grid.add(btnSwitch, 0, 1);

        panel.getChildren().add(grid);
        return panel;
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
                    lbl.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));
                    lbl.setTextFill(Color.web("#f0f8ff"));
                } else {
                    lbl.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.NORMAL, 11));
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
    public void updatePokemonDisplay(Joueur joueur) {
        if (joueur == null || joueur.getMonstreActuel() == null) {
            return;
        }

        Monstre monstre = joueur.getMonstreActuel();
        double ratio = Math.max(0, monstre.getPointsDeVie() / monstre.getPointsDeVieMax());

        // Déterminer si c'est le joueur1 (à gauche) ou le joueur2 (à droite)
        boolean isPlayer1 = joueur == joueur1;

        if (isPlayer1) {
            // Mise à jour du joueur 1 (à gauche)
            if (lblPlayerName != null) {
                lblPlayerName.setText(monstre.getNomMonstre());
            }
            if (lblPlayerHpText != null) {
                lblPlayerHpText.setText(UIComponentFactory.formatHpText(monstre.getPointsDeVie(), monstre.getPointsDeVieMax()));
            }
            if (hpBarPlayerFill != null) {
                hpBarPlayerFill.setWidth(HP_BAR_MAX_WIDTH * ratio);
                hpBarPlayerFill.setFill(Color.web(UIComponentFactory.getHpColor(ratio)));
            }

            String typeColorCode = ColorConfig.fromString(joueur.getMonstreActuel().getTypeMonstre().getLabelType()).getColorCode();

            // Bordure de la carte d'info (couleur selon le type)
            if (playerInfoBox != null) {
                playerInfoBox.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.92); " +
                                "-fx-background-radius: 15; " +
                                "-fx-border-color: " + typeColorCode + "; " +
                                "-fx-border-width: 3; " +
                                "-fx-border-radius: 15; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);"
                );
            }
            if (lblPlayerMonsterType != null) {
                lblPlayerMonsterType.setText(monstre.getTypeMonstre().getLabelType());
                lblPlayerMonsterType.setStyle("-fx-background-color: " + typeColorCode + "; -fx-background-radius: 10;");
            }
            updateSpriteBox(playerSpriteBox, monstre, true);
        } else {
            // Mise à jour du joueur 2 (ennemi à droite)
            if (lblEnemyName != null) {
                lblEnemyName.setText(monstre.getNomMonstre());
            }
            if (lblEnemyHpText != null) {
                lblEnemyHpText.setText(UIComponentFactory.formatHpText(monstre.getPointsDeVie(), monstre.getPointsDeVieMax()));
            }
            if (hpBarEnemyFill != null) {
                hpBarEnemyFill.setWidth(HP_BAR_MAX_WIDTH * ratio);
                hpBarEnemyFill.setFill(Color.web(UIComponentFactory.getHpColor(ratio)));
            }

            String typeColorCode = ColorConfig.fromString(monstre.getTypeMonstre().getLabelType()).getColorCode();
            // Bordure de la carte d'info (couleur selon le type)
            if (enemyInfoBox != null) {
                enemyInfoBox.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.92); " +
                                "-fx-background-radius: 15; " +
                                "-fx-border-color: " + typeColorCode + "; " +
                                "-fx-border-width: 3; " +
                                "-fx-border-radius: 15; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 4);"
                );
            }
            if (lblEnemyMonsterType != null) {
                lblEnemyMonsterType.setText(monstre.getTypeMonstre().getLabelType());
                lblEnemyMonsterType.setStyle("-fx-background-color: " + typeColorCode + "; -fx-background-radius: 10;");
            }
            updateSpriteBox(enemySpriteBox, monstre, false);
        }
    }

    private void updateSpriteBox(VBox spriteBox, Monstre m, boolean isPlayer) {
        if (spriteBox == null || m == null) return;

        String typeColor = ColorConfig.fromString(m.getTypeMonstre().getLabelType()).getColorCode();
        double size = isPlayer ? 100 : 90;

        spriteBox.getChildren().clear();

        Label sprite = new Label(m.getNomMonstre().substring(0, Math.min(2, m.getNomMonstre().length())).toUpperCase());
        sprite.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, isPlayer ? 38 : 34));
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
                String typeColor = ColorConfig.fromString(a.getTypeAttaque().getLabelType()).getColorCode();
                boolean hasNoPP = a.getNbUtilisations() <= 0;

                Button btn = new Button();
                btn.setPrefWidth(ATTACK_BUTTON_WIDTH);
                btn.setPrefHeight(ATTACK_BUTTON_HEIGHT);

                String ppText = hasNoPP ? "PP VIDE" : ("PP " + a.getNbUtilisations());
                String infoText = String.format(
                        "%s | %s | PWR %d | ACC %.0f%%",
                        a.getTypeAttaque().getLabelType(),
                        ppText,
                        a.getPuissanceAttaque(),
                        100 - (a.getProbabiliteEchec() * 100)
                );
                VBox content = UIComponentFactory.createButtonContent(a.getNomAttaque().replace("_", " "), infoText, hasNoPP);
                btn.setGraphic(content);

                if (hasNoPP) {
                    // Style grisé pour les attaques sans PP
                    btn.setStyle(UIComponentFactory.createButtonStyle(
                            ColorConfig.NO_PP_BG_COLOR.getColorCode(), ColorConfig.NO_PP_BORDER_COLOR.getColorCode(), "0 0 3 0", 8) + "-fx-opacity: 0.6;");
                    btn.setDisable(true);
                } else {
                    // Utiliser UIComponentFactory pour les effets hover
                    String normalStyle = UIComponentFactory.createCardStyle(
                            typeColor,
                            UIComponentFactory.deriveColor(typeColor, -30),
                            "0 0 3 0",
                            8
                    );
                    String hoverStyle = UIComponentFactory.createCardStyle(
                            UIComponentFactory.deriveColor(typeColor, 15),
                            UIComponentFactory.deriveColor(typeColor, -30),
                            "0 0 3 0",
                            8
                    );
                    UIComponentFactory.addHoverEffect(btn, normalStyle, hoverStyle);

                    btn.setOnAction(e -> {
                        onSelect.accept(a);
                        hideAllChoices();
                    });
                }

                attackGrid.add(btn, col, row);
                col++;
                if (col >= ATTACK_GRID_COLUMNS) { col = 0; row++; }
            }
        }

       
        boolean attaquesDispo = false;
        for (Attaque a : attaques) {
            if (a.getNbUtilisations() > 0) {
                attaquesDispo = true;
                break;
            }
        }

        if (!attaquesDispo) {
            // Bouton "Mains nues" toujours disponible
            Button btnBareHands = new Button();
            btnBareHands.setPrefWidth(ATTACK_BUTTON_WIDTH);
            btnBareHands.setPrefHeight(ATTACK_BUTTON_HEIGHT);

            VBox bareHandsContent = UIComponentFactory.createButtonContent("MAINS NUES", "Normal | PP infini | PWR faible", false);
            btnBareHands.setGraphic(bareHandsContent);

            // Utiliser UIComponentFactory pour les effets hover
            String bareHandsNormalStyle = UIComponentFactory.createCardStyle(ColorConfig.BARE_HANDS_COLOR.getColorCode(), ColorConfig.BARE_HANDS_BORDER.getColorCode(), "0 0 3 0", 8);
            String bareHandsHoverStyle = UIComponentFactory.createCardStyle(ColorConfig.BARE_HANDS_COLOR_HOVER.getColorCode(), ColorConfig.BARE_HANDS_BORDER.getColorCode(), "0 0 3 0", 8);
            UIComponentFactory.addHoverEffect(btnBareHands, bareHandsNormalStyle, bareHandsHoverStyle);

            btnBareHands.setOnAction(e -> {
                onSelect.accept(null); // null représente l'attaque à mains nues
                hideAllChoices();
            });
            
            attackGrid.add(btnBareHands, col, row);
            col++;
            if (col >= ATTACK_GRID_COLUMNS) { col = 0; row++; }
        }
        // Bouton retour
        Button btnBack = UIComponentFactory.createBackButton();
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
            String typeColor = ColorConfig.fromString(m.getTypeMonstre().getLabelType()).getColorCode();
            double hpRatio   = Math.max(0, m.getPointsDeVie() / m.getPointsDeVieMax());
            VBox card        = UIComponentFactory.createTypeCard(typeColor, MONSTER_CARD_WIDTH);
            Label nameLabel  = UIComponentFactory.createLabel(m.getNomMonstre(), 11, FontWeight.BOLD, Color.WHITE);
            String hpColor   = UIComponentFactory.getHpColor(hpRatio);
            Label hpLabel    = UIComponentFactory.createLabel(
                    UIComponentFactory.formatHpText(m.getPointsDeVie(), m.getPointsDeVieMax()),
                    10,
                    FontWeight.BOLD,
                    Color.web(hpColor)
            );

            card.getChildren().addAll(nameLabel, hpLabel);

            card.setOnMouseClicked(e -> {
                onSelect.accept(m);
                hideAllChoices();
            });

            monsterChoicePanel.getChildren().add(card);
        }

        Button btnBack = UIComponentFactory.createBackButton();
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
            String objName  = obj.getNomObjet();
            String objColor = UIComponentFactory.getItemColor(objName);
            Button btn      = UIComponentFactory.createTypeButton(objName.replace("_", " "), objColor, 120, 40, 10);

            btn.setOnAction(e -> {
                onSelect.accept(obj);
                hideAllChoices();
            });

            itemChoicePanel.getChildren().add(btn);
        }

        Button btnBack = UIComponentFactory.createBackButton();
        btnBack.setOnAction(e -> hideAllChoices());
        itemChoicePanel.getChildren().add(btnBack);

        itemChoicePanel.setVisible(true);
        itemChoicePanel.setManaged(true);
        actionButtonsContainer.setVisible(false);
        actionButtonsContainer.setManaged(false);
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

    public void setTurn(boolean player1Turn) {
        this.isPlayer1Turn = player1Turn;
        clearBattleLog();
    }

    // Getters
    public Button getBtnAttack() { return btnAttack; }
    public Button getBtnItem() { return btnItem; }
    public Button getBtnSwitch() { return btnSwitch; }
    public Joueur getJoueur1() { return joueur1; }
    public Joueur getJoueur2() { return joueur2; }
    public boolean isPlayer1Turn() { return isPlayer1Turn; }
    public void setIsPlayer1Turn(boolean isPlayer1Turn) { this.isPlayer1Turn = isPlayer1Turn; }
}
