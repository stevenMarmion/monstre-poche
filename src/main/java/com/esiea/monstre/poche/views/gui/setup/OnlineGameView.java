package com.esiea.monstre.poche.views.gui.setup;

import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
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
 * Hérite de AbstractGameSetupView mais avec une structure plus complexe (host/join).
 */
public class OnlineGameView extends AbstractGameSetupView {

    private TextField hostPlayerName;
    private TextField hostPort;
    private TextField joinPlayerName;
    private TextField joinHost;
    private TextField joinPort;

    private Button btnCreateServer;
    private Button btnJoinServer;

    private ToggleButton toggleHost;
    private ToggleButton toggleJoin;

    private VBox hostBox;
    private VBox joinBox;

    private HBox hostLoadingBox;
    private ProgressIndicator hostLoadingIndicator;
    private Label hostLoadingLabel;

    public OnlineGameView() {
        super();
        initializeView();
    }

    @Override
    protected String getTitle() {
        return "Jouer en ligne";
    }

    @Override
    protected VBox createCustomContent() {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        // Sous-titre
        Label subtitle = new Label("Choisissez votre role pour lancer une partie moderne et rapide");
        subtitle.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.NORMAL, 16));
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

        hostBox = createHostSection();
        joinBox = createJoinSection();

        // Conteneur horizontal pour juxtaposer les deux cartes
        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.getStyleClass().add("cards-wrapper");
        cardsContainer.getChildren().addAll(hostBox, joinBox);

        container.getChildren().addAll(subtitle, toggleContainer, cardsContainer);

        // Toggle behavior to show only the selected form
        toggleHost.setOnAction(e -> switchToHost());
        toggleJoin.setOnAction(e -> switchToJoin());
        switchToHost();

        return container;
    }

    private VBox createHostSection() {
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("card-container");

        Label hostTitle = new Label("Creer une partie");
        hostTitle.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 20));
        hostTitle.getStyleClass().add("label-text");

        Label hostBadge = new Label("Hote");
        hostBadge.getStyleClass().add("pill-badge");

        HBox hostHeader = new HBox(10);
        hostHeader.setAlignment(Pos.CENTER_LEFT);
        hostHeader.getChildren().addAll(hostBadge, hostTitle);

        hostPlayerName = createTextFieldWithCustomWidth("Nom du joueur", 280);
        hostPort = createTextFieldWithCustomWidth("Port (ex: 5000)", 280);

        btnCreateServer = new Button("Lancer le serveur");
        btnCreateServer.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        btnCreateServer.setPrefWidth(260);
        btnCreateServer.getStyleClass().add("menu-button");

        hostLoadingIndicator = new ProgressIndicator();
        hostLoadingIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        hostLoadingIndicator.setPrefSize(28, 28);

        hostLoadingLabel = new Label("En attente d'un joueur...");
        hostLoadingLabel.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.NORMAL, 13));
        hostLoadingLabel.getStyleClass().add("subtitle-text");

        hostLoadingBox = new HBox(10);
        hostLoadingBox.setAlignment(Pos.CENTER_LEFT);
        hostLoadingBox.getChildren().addAll(hostLoadingIndicator, hostLoadingLabel);
        hostLoadingBox.setVisible(false);
        hostLoadingBox.setManaged(false);

        box.getChildren().addAll(hostHeader, hostPlayerName, hostPort, btnCreateServer, hostLoadingBox);

        return box;
    }

    private VBox createJoinSection() {
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(20));
        box.getStyleClass().add("card-container");

        Label joinTitle = new Label("Rejoindre une partie");
        joinTitle.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 20));
        joinTitle.getStyleClass().add("label-text");

        Label joinBadge = new Label("Invité");
        joinBadge.getStyleClass().add("pill-badge-alt");

        HBox joinHeader = new HBox(10);
        joinHeader.setAlignment(Pos.CENTER_LEFT);
        joinHeader.getChildren().addAll(joinBadge, joinTitle);

        joinPlayerName = createTextFieldWithCustomWidth("Nom du joueur", 280);
        joinHost = createTextFieldWithCustomWidth("Adresse du serveur (ex: 127.0.0.1)", 280);
        joinPort = createTextFieldWithCustomWidth("Port (ex: 5000)", 280);

        btnJoinServer = new Button("Rejoindre");
        btnJoinServer.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 16));
        btnJoinServer.setPrefWidth(260);
        btnJoinServer.getStyleClass().add("menu-button");

        box.getChildren().addAll(joinHeader, joinPlayerName, joinHost, joinPort, btnJoinServer);

        return box;
    }

    private TextField createTextFieldWithCustomWidth(String promptText, int width) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(width);
        textField.setMaxWidth(width + 40);
        textField.setFont(Font.font(FontConfig.SYSTEM.getFontName(), 14));
        textField.getStyleClass().add("text-field");
        return textField;
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

    /**
     * Affiche un indicateur de chargement pour rejoindre un serveur.
     */
    public void showJoinLoading() {
        btnJoinServer.setDisable(true);
        joinPlayerName.setDisable(true);
        joinHost.setDisable(true);
        joinPort.setDisable(true);
    }

    /**
     * Reinitialise l'etat de chargement pour rejoindre un serveur.
     */
    public void resetJoinLoading() {
        btnJoinServer.setDisable(false);
        joinPlayerName.setDisable(false);
        joinHost.setDisable(false);
        joinPort.setDisable(false);
    }

    /**
     * Affiche un message d'erreur.
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
