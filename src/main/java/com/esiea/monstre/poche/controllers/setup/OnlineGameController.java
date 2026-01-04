package com.esiea.monstre.poche.controllers.setup;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.online.OnlineClient;
import com.esiea.monstre.poche.models.online.OnlineConnection;
import com.esiea.monstre.poche.models.online.OnlineServer;
import com.esiea.monstre.poche.views.gui.setup.OnlineGameView;

import javafx.application.Platform;

/**
 * Controller pour le mode de jeu en ligne.
 * Version simplifiée - affiche simplement la vue.
 */
public class OnlineGameController {
    
    private OnlineGameView view;
    private INavigationCallback INavigationCallback;
    private OnlineConnection currentConnection;
    
    public OnlineGameController(OnlineGameView view, INavigationCallback INavigationCallback) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnBackToMenu().setOnAction(e -> INavigationCallback.showMainMenu());
        view.getBtnJoinServer().setOnAction(e -> handleJoinServer());
        view.getBtnCreateServer().setOnAction(e -> handleCreateServer());
    }

    private void handleJoinServer() {
        String joinPlayerName = view.getJoinPlayerName().getText().trim();
        String joinPort = view.getJoinPort().getText().trim();
        String address = view.getJoinHost().getText().trim();

        int port;
        try {
            port = Integer.parseInt(joinPort);
        } catch (NumberFormatException ex) {
            CombatLogger.error("Port invalide pour rejoindre le serveur");
            return;
        }

        Joueur joiningPlayer = new Joueur(joinPlayerName);
        OnlineClient client = new OnlineClient(address, port);

        new Thread(() -> {
            OnlineConnection connectionClient = client.connecteToServer(joiningPlayer, address, port);
            Platform.runLater(() -> {
                if (connectionClient != null && connectionClient.getSocket().isConnected()) {
                    currentConnection = connectionClient;
                    startSelectionFlow(joiningPlayer, connectionClient);
                }
            });
        }, "online-client-connect").start();
    }

    private void handleCreateServer() {
        String hostPlayerName = view.getHostPlayerName().getText().trim();
        String hostPort = view.getHostPort().getText().trim();

        int port;
        try {
            port = Integer.parseInt(hostPort);
        } catch (NumberFormatException ex) {
            CombatLogger.error("Port invalide pour créer le serveur");
            return;
        }

        Joueur hostPlayer = new Joueur(hostPlayerName);
        view.showHostLoading();

        new Thread(() -> {
            OnlineServer server = new OnlineServer(port, null);
            OnlineConnection connectionServer = server.demarrerServeur(hostPlayer);
            Platform.runLater(() -> {
                if (connectionServer != null && connectionServer.getSocket().isConnected()) {
                    currentConnection = connectionServer;
                    startSelectionFlow(hostPlayer, connectionServer);
                } else {
                    view.resetHostLoading();
                }
            });
        }, "online-server-accept").start();
    }

    private void startSelectionFlow(Joueur joueur, OnlineConnection connection) {
        INavigationCallback.showMonsterSelectionPlayer(joueur, () -> {
            waitForOpponentReadyAndStartBattle(joueur, connection);
        });
    }

    private void waitForOpponentReadyAndStartBattle(Joueur joueur, OnlineConnection connection) {
        new Thread(() -> {
            try {
                java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(connection.getSocket().getInputStream())
                );
                
                // Envoyer que ce joueur est prêt
                connection.sendInfo("READY_SELECTION");
                CombatLogger.logReseau("[" + joueur.getNomJoueur() + "] En attente de l'adversaire...");
                
                // Attendre que l'adversaire soit aussi prêt
                String line;
                boolean opponentReady = false;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("INFO|READY_SELECTION")) {
                        opponentReady = true;
                        CombatLogger.logReseau("[" + joueur.getNomJoueur() + "] Adversaire prêt!");
                        break;
                    }
                }
                
                if (opponentReady) {
                    Platform.runLater(() -> {
                        INavigationCallback.showBattleOnline(joueur, connection);
                    });
                } else {
                    throw new java.io.IOException("Adversaire non prêt");
                }
            } catch (Exception e) {
                CombatLogger.error("Synchronisation du combat en ligne : " + e.getMessage());
                Platform.runLater(INavigationCallback::showMainMenu);
            }
        }, "online-battle-sync").start();
    }
}
