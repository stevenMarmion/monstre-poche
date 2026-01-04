package com.esiea.monstre.poche.views.gui.setup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Vue pour le mode de jeu en ligne.
 */
public class OnlineGameView extends VBox {

    private TextField hostPlayerName;
    private TextField hostPort;
    private TextField joinPlayerName;
    private TextField joinHost;
    private TextField joinPort;

    private Button btnCreateServer;
    private Button btnJoinServer;
    private Button btnBackToMenu;

    private ToggleButton toggleHost;
    private ToggleButton toggleJoin;

    private VBox hostBox;
    private VBox joinBox;

    private HBox hostLoadingBox;
    private ProgressIndicator hostLoadingIndicator;
    private Label hostLoadingLabel;

    public OnlineGameView() {
        initializeView();
    }

    /**
     * Initialise la vue du jeu en ligne : choix entre creer ou rejoindre une partie.
     */
    private void initializeView() {
        // Configuration du conteneur principal
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.setPadding(new Insets(20));
        this.getStyleClass().add("main-container");

        // Barre superieure avec retour au menu
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(10));

        btnBackToMenu = new Button("Revenir au menu");
        btnBackToMenu.setFont(Font.font("System", 14));
        btnBackToMenu.getStyleClass().add("back-button");
        topBar.getChildren().add(btnBackToMenu);

        // Titre et sous-titre
        Label title = new Label("Jouer en ligne");
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Choisissez votre role pour lancer une partie moderne et rapide");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 16));
        subtitle.getStyleClass().add("subtitle-text");

        // Segmented controls to toggle host / join
        ToggleGroup modeGroup = new ToggleGroup();
        toggleHost = new ToggleButton("Créer");
        toggleJoin = new ToggleButton("Rejoindre");
        toggleHost.setToggleGroup(modeGroup);
        toggleJoin.setToggleGroup(modeGroup);
        toggleHost.getStyleClass().add("segment-toggle");
        toggleJoin.getStyleClass().add("segment-toggle");
        toggleHost.setSelected(true);

        HBox toggleContainer = new HBox(8);
        toggleContainer.setAlignment(Pos.CENTER);
        toggleContainer.getChildren().addAll(toggleHost, toggleJoin);

        // Section creation de serveur
        hostBox = new VBox(14);
        hostBox.setAlignment(Pos.CENTER_LEFT);
        hostBox.setPadding(new Insets(20));
        hostBox.getStyleClass().add("card-container");

        Label hostTitle = new Label("Creer une partie");
        hostTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        hostTitle.getStyleClass().add("label-text");

        Label hostBadge = new Label("Hote");
        hostBadge.getStyleClass().add("pill-badge");

        HBox hostHeader = new HBox(10);
        hostHeader.setAlignment(Pos.CENTER_LEFT);
        hostHeader.getChildren().addAll(hostBadge, hostTitle);

        hostPlayerName = createTextField("Nom du joueur");
        hostPort = createTextField("Port (ex: 5000)");

        btnCreateServer = new Button("Lancer le serveur");
        btnCreateServer.setFont(Font.font("System", FontWeight.BOLD, 16));
        btnCreateServer.setPrefWidth(260);
        btnCreateServer.getStyleClass().add("menu-button");

        hostLoadingIndicator = new ProgressIndicator();
        hostLoadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        hostLoadingIndicator.setPrefSize(28, 28);

        hostLoadingLabel = new Label("En attente d'un joueur...");
        hostLoadingLabel.setFont(Font.font("System", FontWeight.NORMAL, 13));
        hostLoadingLabel.getStyleClass().add("subtitle-text");

        hostLoadingBox = new HBox(10);
        hostLoadingBox.setAlignment(Pos.CENTER_LEFT);
        hostLoadingBox.getChildren().addAll(hostLoadingIndicator, hostLoadingLabel);
        hostLoadingBox.setVisible(false);
        hostLoadingBox.setManaged(false);

        hostBox.getChildren().addAll(hostHeader, hostPlayerName, hostPort, btnCreateServer, hostLoadingBox);

        // Section rejoindre serveur
        joinBox = new VBox(14);
        joinBox.setAlignment(Pos.CENTER_LEFT);
        joinBox.setPadding(new Insets(20));
        joinBox.getStyleClass().add("card-container");

        Label joinTitle = new Label("Rejoindre une partie");
        joinTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        joinTitle.getStyleClass().add("label-text");

        Label joinBadge = new Label("Invité");
        joinBadge.getStyleClass().add("pill-badge-alt");

        HBox joinHeader = new HBox(10);
        joinHeader.setAlignment(Pos.CENTER_LEFT);
        joinHeader.getChildren().addAll(joinBadge, joinTitle);

        joinPlayerName = createTextField("Nom du joueur");
        joinHost = createTextField("Adresse du serveur (ex: 127.0.0.1)");
        joinPort = createTextField("Port (ex: 5000)");

        btnJoinServer = new Button("Rejoindre");
        btnJoinServer.setFont(Font.font("System", FontWeight.BOLD, 16));
        btnJoinServer.setPrefWidth(260);
        btnJoinServer.getStyleClass().add("menu-button");

        joinBox.getChildren().addAll(joinHeader, joinPlayerName, joinHost, joinPort, btnJoinServer);

        // Conteneur horizontal pour juxtaposer les deux cartes
        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.getStyleClass().add("cards-wrapper");
        cardsContainer.getChildren().addAll(hostBox, joinBox);

        // Ajout des elements au conteneur principal
        this.getChildren().addAll(topBar, title, subtitle, toggleContainer, cardsContainer);

        // Toggle behavior to show only the selected form
        toggleHost.setOnAction(e -> switchToHost());
        toggleJoin.setOnAction(e -> switchToJoin());
        switchToHost();
    }

    private void switchToHost() {
        toggleHost.setSelected(true);
        toggleJoin.setSelected(false);
        hostBox.setVisible(true);
        hostBox.setManaged(true);
        joinBox.setVisible(false);
        joinBox.setManaged(false);
        toggleHost.getStyleClass().add("segment-toggle-active");
        toggleJoin.getStyleClass().remove("segment-toggle-active");
    }

    private void switchToJoin() {
        toggleHost.setSelected(false);
        toggleJoin.setSelected(true);
        hostBox.setVisible(false);
        hostBox.setManaged(false);
        joinBox.setVisible(true);
        joinBox.setManaged(true);
        toggleJoin.getStyleClass().add("segment-toggle-active");
        toggleHost.getStyleClass().remove("segment-toggle-active");
    }

    /**
     * Crée un champ de texte stylise.
     */
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(280);
        textField.setMaxWidth(320);
        textField.setFont(Font.font("System", 14));
        textField.getStyleClass().add("text-field");
        return textField;
    }

    public TextField getHostPlayerName() {
        return hostPlayerName;
    }

    public TextField getHostPort() {
        return hostPort;
    }

    public TextField getJoinPlayerName() {
        return joinPlayerName;
    }

    public TextField getJoinHost() {
        return joinHost;
    }

    public TextField getJoinPort() {
        return joinPort;
    }

    public Button getBtnCreateServer() {
        return btnCreateServer;
    }

    public Button getBtnJoinServer() {
        return btnJoinServer;
    }

    public Button getBtnBackToMenu() {
        return btnBackToMenu;
    }

    /**
     * Affiche un indicateur de chargement lorsqu'un hote lance un serveur.
     */
    public void showHostLoading() {
        btnCreateServer.setDisable(true);
        hostPlayerName.setDisable(true);
        hostPort.setDisable(true);
        hostLoadingBox.setVisible(true);
        hostLoadingBox.setManaged(true);
    }

    /**
     * Reinitialise l'etat de chargement de la creation serveur.
     */
    public void resetHostLoading() {
        btnCreateServer.setDisable(false);
        hostPlayerName.setDisable(false);
        hostPort.setDisable(false);
        hostLoadingBox.setVisible(false);
        hostLoadingBox.setManaged(false);
    }
}
