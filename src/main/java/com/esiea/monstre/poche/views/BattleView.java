package com.esiea.monstre.poche.views;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;
import java.util.function.Consumer;

/**
 * Vue principale du système de combat.
 * Affiche les deux Pokémon en combat et les actions disponibles.
 */
public class BattleView extends BorderPane {
    
    // Containers pour les deux joueurs
    private VBox player1Container;
    private VBox player2Container;
    
    // Labels pour les informations des monstres
    private Label lblMonster1Name;
    private ProgressBar hpBar1;
    private Label lblHp1;
    private Label lblStats1;
    
    private Label lblMonster2Name;
    private ProgressBar hpBar2;
    private Label lblHp2;
    private Label lblStats2;
    
    // Boutons d'action
    private Button btnAttack;
    private Button btnItem;
    private Button btnSwitch;
    private Button btnFlee;
    private Label lblTurn;
    private VBox attackChoicesBox;
    
    // Zone de messages
    private Label lblBattleLog;
    
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
    
    /**
     * Initialise la vue du combat.
     */
    private void initializeView() {
        this.setStyle("-fx-background-color: black;");
        this.setPadding(new Insets(10));
        
        // Plateau de combat rétro avec fond image
        StackPane battlefield = createBattlefield();
        this.setCenter(battlefield);
        
        // Conteneur inférieur pour les actions + log
        VBox actionContainer = createActionContainer();
        this.setBottom(actionContainer);
    }
    
    /**
     * Crée l'affichage des deux Pokémon.
     */
    private StackPane createBattlefield() {
        StackPane stack = new StackPane();
        stack.setPadding(new Insets(10));
        stack.setStyle("-fx-background-color: #1b1b1b; -fx-border-color: #d2c29d; -fx-border-width: 6px; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        
        Image bgImage = new Image(getClass().getResource("/images/exemple_terrain.jpg").toExternalForm(), 900, 0, true, true);
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(true);
        bgView.setFitWidth(900);
        bgView.setViewOrder(10); // fond tout au fond
        stack.getChildren().add(bgView);
        
        // Sprites placeholders (positions inspirées d'un combat rétro)
        Label spritePlayer = new Label("◼");
        spritePlayer.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 72));
        spritePlayer.setStyle("-fx-text-fill: #4b4b4b;");

        Label spriteEnemy = new Label("◆");
        spriteEnemy.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 64));
        spriteEnemy.setStyle("-fx-text-fill: #d9d9d9;");

        // Ligne dédiée aux sprites : joueur à gauche, ennemi à droite, sans chevauchement
        HBox spriteRow = new HBox();
        spriteRow.setAlignment(Pos.CENTER);
        spriteRow.setPadding(new Insets(60, 80, 80, 80));
        spriteRow.setSpacing(260);
        spriteRow.setViewOrder(5); // au-dessus du fond, sous les HUD

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        spriteRow.getChildren().addAll(spritePlayer, spacer, spriteEnemy);
        
        // HUDs repositionnés façon GBA/retro
        player2Container = createPlayerPokemonBox(joueur2, true);
        StackPane.setAlignment(player2Container, Pos.TOP_LEFT);
        StackPane.setMargin(player2Container, new Insets(30, 0, 0, 30));
        player2Container.setViewOrder(0); // HUDs au-dessus des sprites

        player1Container = createPlayerPokemonBox(joueur1, false);
        StackPane.setAlignment(player1Container, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(player1Container, new Insets(0, 30, 30, 0));
        player1Container.setViewOrder(0);

        stack.getChildren().addAll(spriteRow, player2Container, player1Container);
        return stack;
    }
    
    /**
     * Crée une boîte d'affichage pour un Pokémon.
     */
    private VBox createPlayerPokemonBox(Joueur joueur, boolean isRightSide) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(12));
        box.setStyle("-fx-background-color: rgba(24,24,24,0.9); " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: #d2c29d; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.45), 6, 0, 0, 2);");
        box.setPrefWidth(320);
        
        // Nom du joueur
        Label playerNameLabel = new Label(joueur.getNomJoueur());
        playerNameLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        playerNameLabel.setStyle("-fx-text-fill: #f8eec7;");
        
        // Nom et type du monstre
        Label monsterNameLabel = new Label(joueur.getMonstreActuel().getNomMonstre() + " (" + joueur.getMonstreActuel().getTypeMonstre().getLabelType() + ")");
        monsterNameLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        monsterNameLabel.setStyle("-fx-text-fill: #ffffff;");
        
        // Placeholder pour l'image (texte pour le moment)
        Label imageLabel = new Label("■");
        imageLabel.setFont(Font.font("Monospaced", FontWeight.EXTRA_BOLD, 36));
        imageLabel.setStyle("-fx-text-fill: #bfbfbf;");
        
        // Barre de PV
        HBox hpContainer = new HBox(10);
        hpContainer.setAlignment(Pos.CENTER_LEFT);
        hpContainer.setPrefWidth(300);
        
        Label hpLabel = new Label("PV :");
        hpLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 13));
        hpLabel.setStyle("-fx-text-fill: #f8eec7;");
        
        ProgressBar hpBar = new ProgressBar();
        hpBar.setPrefWidth(200);
        hpBar.setPrefHeight(25);
        double hpRatio = joueur.getMonstreActuel().getPointsDeVie() / joueur.getMonstreActuel().getPointsDeVieMax();
        hpBar.setProgress(Math.max(0, Math.min(1, hpRatio)));
        hpBar.setStyle("-fx-accent: #4cff4c;");
        
        Label hpValue = new Label(String.format("%.0f / %.0f", joueur.getMonstreActuel().getPointsDeVie(), joueur.getMonstreActuel().getPointsDeVieMax()));
        hpValue.setFont(Font.font("Monospaced", 12));
        hpValue.setStyle("-fx-text-fill: #ffffff;");
        
        hpContainer.getChildren().addAll(hpLabel, hpBar, hpValue);
        
        // Statistiques
        Label statsLabel = new Label(String.format(
            "ATK: %d | DEF: %d | VIT: %d",
            joueur.getMonstreActuel().getAttaque(),
            joueur.getMonstreActuel().getDefense(),
            joueur.getMonstreActuel().getVitesse()
        ));
        statsLabel.setFont(Font.font("Monospaced", 11));
        statsLabel.setStyle("-fx-text-fill: #c9c9c9;");
        
        box.getChildren().addAll(playerNameLabel, monsterNameLabel, imageLabel, hpContainer, statsLabel);
        
        // Sauvegarder les références pour mises à jour ultérieures
        if (!isRightSide) {
            lblMonster1Name = monsterNameLabel;
            hpBar1 = hpBar;
            lblHp1 = hpValue;
            lblStats1 = statsLabel;
        } else {
            lblMonster2Name = monsterNameLabel;
            hpBar2 = hpBar;
            lblHp2 = hpValue;
            lblStats2 = statsLabel;
        }
        
        return box;
    }
    
    /**
     * Crée le conteneur des actions disponibles.
     */
    private VBox createActionContainer() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        
        // Affichage du tour actuel
        lblTurn = new Label(joueur1.getNomJoueur() + " - À votre tour !");
        lblTurn.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        lblTurn.setStyle("-fx-text-fill: #f8eec7; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 6, 0, 1, 1);");
        
        // Conteneur pour les boutons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        
        // Boutons d'action (visibles seulement pour le joueur actuel)
        btnAttack = createActionButton("Attaquer", "#c0392b");
        btnItem = createActionButton("Objet", "#d68910");
        btnSwitch = createActionButton("Changer", "#2471a3");
        
        buttonBox.getChildren().addAll(btnAttack, btnItem, btnSwitch);
        
        // Conteneur pour afficher les choix d'attaques en ligne dans l'interface
        attackChoicesBox = new VBox(10);
        attackChoicesBox.setAlignment(Pos.CENTER);
        attackChoicesBox.setPadding(new Insets(10));
        attackChoicesBox.setStyle("-fx-background-color: rgba(20, 20, 20, 0.92); -fx-background-radius: 8; -fx-border-color: #d2c29d; -fx-border-width: 3px; -fx-border-radius: 8;");
        attackChoicesBox.setVisible(false);
        attackChoicesBox.setManaged(false);

        // Zone de messages (log) sous les boutons
        lblBattleLog = new Label("Le combat commence !");
        lblBattleLog.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        lblBattleLog.setStyle("-fx-text-fill: #f8eec7; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 5, 0, 1, 1);");
        lblBattleLog.setWrapText(true);
        lblBattleLog.setMaxWidth(720);
        
        container.getChildren().addAll(lblTurn, buttonBox, attackChoicesBox, lblBattleLog);
        return container;
    }
    
    /**
     * Crée un bouton d'action stylisé.
     */
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.BOLD, 14));
        btn.setPrefWidth(150);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: " + color + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 10; " +
                    "-fx-border-radius: 10; " +
                    "-fx-border-color: #2c3e50; " +
                    "-fx-border-width: 2px; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 8, 0, 0, 3);");
        
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + 
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replaceAll(
                    "-fx-scale-x: 1.05; -fx-scale-y: 1.05;", "")));
        
        return btn;
    }
    
    /**
     * Met à jour l'affichage des Pokémon.
     */
    public void updatePokemonDisplay() {
        if (joueur1.getMonstreActuel() != null && hpBar1 != null) {
            Monstre m1 = joueur1.getMonstreActuel();
            double hpRatio = m1.getPointsDeVie() / m1.getPointsDeVieMax();
            hpBar1.setProgress(Math.max(0, Math.min(1, hpRatio)));
            lblHp1.setText(String.format("%.0f / %.0f", m1.getPointsDeVie(), m1.getPointsDeVieMax()));
            lblMonster1Name.setText(m1.getNomMonstre() + " (" + m1.getTypeMonstre().getLabelType() + ")");
            lblStats1.setText(String.format("ATK: %d | DEF: %d | VIT: %d", m1.getAttaque(), m1.getDefense(), m1.getVitesse()));
        }
        
        if (joueur2.getMonstreActuel() != null && hpBar2 != null) {
            Monstre m2 = joueur2.getMonstreActuel();
            double hpRatio = m2.getPointsDeVie() / m2.getPointsDeVieMax();
            hpBar2.setProgress(Math.max(0, Math.min(1, hpRatio)));
            lblHp2.setText(String.format("%.0f / %.0f", m2.getPointsDeVie(), m2.getPointsDeVieMax()));
            lblMonster2Name.setText(m2.getNomMonstre() + " (" + m2.getTypeMonstre().getLabelType() + ")");
            lblStats2.setText(String.format("ATK: %d | DEF: %d | VIT: %d", m2.getAttaque(), m2.getDefense(), m2.getVitesse()));
        }
    }
    
    /**
     * Met à jour le message du combat.
     */
    public void updateBattleLog(String message) {
        lblBattleLog.setText(message);
    }
    
    /**
     * Affiche les attaques disponibles dans l'interface pour le joueur courant.
     */
    public void displayAttackChoices(List<Attaque> attaques, Consumer<Attaque> onSelect) {
        attackChoicesBox.getChildren().clear();
        if (attaques == null || attaques.isEmpty()) {
            Label none = new Label("Aucune attaque disponible");
            none.setFont(Font.font("System", FontWeight.BOLD, 14));
            none.setStyle("-fx-text-fill: #2c3e50;");
            attackChoicesBox.getChildren().add(none);
        } else {
            for (Attaque a : attaques) {
                Button btn = new Button(a.getNomAttaque() + " (Puissance: " + a.getPuissanceAttaque() + ", Uses: " + a.getNbUtilisations() + ")");
                btn.setFont(Font.font("System", FontWeight.BOLD, 13));
                btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #27ae60; -fx-border-width: 2px;");
                btn.setOnAction(e -> {
                    // Remonter la sélection au contrôleur
                    onSelect.accept(a);
                    hideAttackChoices();
                });
                attackChoicesBox.getChildren().add(btn);
            }
        }
        attackChoicesBox.setVisible(true);
        attackChoicesBox.setManaged(true);
    }
    
    /**
     * Cache la liste des attaques.
     */
    public void hideAttackChoices() {
        attackChoicesBox.setVisible(false);
        attackChoicesBox.setManaged(false);
        attackChoicesBox.getChildren().clear();
    }
    
    /**
     * Affiche les monstres disponibles pour le joueur courant.
     */
    public void displayMonsterChoices(List<Monstre> monstres, Consumer<Monstre> onSelect) {
        attackChoicesBox.getChildren().clear();
        if (monstres == null || monstres.isEmpty()) {
            Label none = new Label("Aucun monstre disponible");
            none.setFont(Font.font("System", FontWeight.BOLD, 14));
            none.setStyle("-fx-text-fill: #2c3e50;");
            attackChoicesBox.getChildren().add(none);
        } else {
            for (Monstre m : monstres) {
                Button btn = new Button(m.getNomMonstre() + " (PV: " + (int)m.getPointsDeVie() + "/" + (int)m.getPointsDeVieMax() + ", " + m.getTypeMonstre().getLabelType() + ")");
                btn.setFont(Font.font("System", FontWeight.BOLD, 13));
                btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #2980b9; -fx-border-width: 2px;");
                btn.setOnAction(e -> {
                    onSelect.accept(m);
                    hideAttackChoices();
                });
                attackChoicesBox.getChildren().add(btn);
            }
        }
        attackChoicesBox.setVisible(true);
        attackChoicesBox.setManaged(true);
    }
    
    /**
     * Met à jour l'affichage du tour.
     */
    public void setTurn(boolean player1Turn) {
        this.isPlayer1Turn = player1Turn;
        String name = player1Turn ? joueur1.getNomJoueur() : joueur2.getNomJoueur();
        lblTurn.setText(name + " - À votre tour !");
    }
    
    // Getters pour les boutons
    public Button getBtnAttack() {
        return btnAttack;
    }

    public Joueur getJoueur1() {
        return this.joueur1;
    }

    public Joueur getJoueur2() {
        return this.joueur2;
    }
    
    public Button getBtnItem() {
        return btnItem;
    }
    
    public Button getBtnSwitch() {
        return btnSwitch;
    }
    
    public Button getBtnFlee() {
        return btnFlee;
    }
    
    public void setIsPlayer1Turn(boolean isPlayer1Turn) {
        this.isPlayer1Turn = isPlayer1Turn;
    }
    
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }
}
