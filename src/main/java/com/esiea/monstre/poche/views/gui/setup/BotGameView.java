package com.esiea.monstre.poche.views.gui.setup;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Vue pour le mode de jeu contre le bot.
 * Hérite de AbstractGameSetupView pour réutiliser le code commun.
 */
public class BotGameView extends AbstractGameSetupView {

    private TextField txtPlayerName;

    public BotGameView() {
        super();
        initializeView();
    }

    @Override
    protected String getTitle() {
        return "Jouer contre le bot";
    }

    @Override
    protected VBox createCustomContent() {
        VBox inputBox = createInputBox();
        Label lblPlayer = createLabel("Nom du Joueur :");
        txtPlayerName = createTextField("Entrez votre nom");

        inputBox.getChildren().addAll(lblPlayer, txtPlayerName);
        createStartGameButton("Commencer le jeu");

        VBox container = new VBox(20);
        container.setAlignment(javafx.geometry.Pos.CENTER);
        container.getChildren().addAll(inputBox, btnStartGame);

        return container;
    }

    public TextField getTxtPlayerName() {
        return txtPlayerName;
    }
}
