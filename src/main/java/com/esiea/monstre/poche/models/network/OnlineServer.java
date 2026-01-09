package com.esiea.monstre.poche.models.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.battle.modes.CombatEnLigne;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.game.GameVisual;

/**
 * Serveur minimal pour orchestrer un combat en ligne.
 */
public class OnlineServer {
    private final int port;
    private final Scanner scanner;

    public OnlineServer(int port, Scanner scanner) {
        this.port = port;
        this.scanner = scanner;
    }

    public void lancer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            CombatLogger.network("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            CombatLogger.network("Joueur connecté depuis " + socket.getInetAddress());

            try (OnlineConnection connection = new OnlineConnection(socket)) {
                String nomJoueurLocal = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
                String nomJoueurDistant = connection.ask("Entrez votre nom de joueur >");

                Joueur joueurLocal = new Joueur(nomJoueurLocal);
                Joueur joueurDistant = new Joueur(nomJoueurDistant);

                CombatEnLigne combat = new CombatEnLigne(joueurLocal, joueurDistant, connection);
                combat.lancer();
            }
        } catch (IOException e) {
            CombatLogger.error("Problème de serveur : " + e.getMessage());
        }
    }

    /** Fonctions pour le mode interface */

    public OnlineConnection demarrerServeur() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            CombatLogger.network("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            CombatLogger.network("Joueur connecté depuis " + socket.getInetAddress());

            OnlineConnection connection = new OnlineConnection(socket);
            return connection;
        } catch (Exception e) {
            CombatLogger.error("Problème de serveur : " + e.getMessage());
        }
        return null;
    }

    public int getPort() {
        return port;
    }
}
