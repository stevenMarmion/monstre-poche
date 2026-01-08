package com.esiea.monstre.poche.views.gui.setup;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Vue pour le mode de jeu local à deux joueurs.
 * Hérite de AbstractGameSetupView pour réutiliser le code commun.
 */
public class LocalGameView extends AbstractGameSetupView {

    private TextField firstPlayerName;
    private TextField secondPlayerName;

    public LocalGameView() {
        super();
        initializeView();
    }

    @Override
    protected String getTitle() {
        return "Jouer en local à deux";
    }

    @Override
    protected VBox createCustomContent() {
        VBox inputBox = createInputBox();
        Label lblPlayer1 = createLabel("Nom du Joueur 1 :");
        firstPlayerName = createTextField("Entrez le nom du joueur 1");
        Label lblPlayer2 = createLabel("Nom du Joueur 2 :");
        secondPlayerName = createTextField("Entrez le nom du joueur 2");

        inputBox.getChildren().addAll(lblPlayer1, firstPlayerName, lblPlayer2, secondPlayerName);
        createStartGameButton("Commencer le jeu");

        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(inputBox, btnStartGame);

        return container;
    }

    public TextField getFirstPlayerName() {
        return firstPlayerName;
    }

    public TextField getSecondPlayerName() {
        return secondPlayerName;
    }
}
