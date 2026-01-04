package com.esiea.monstre.pochebis.models.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.pochebis.models.combats.CombatEnLigne;
import com.esiea.monstre.pochebis.models.combats.CombatLogger;
import com.esiea.monstre.pochebis.models.entites.Joueur;
import com.esiea.monstre.pochebis.models.loader.AttaqueLoader;
import com.esiea.monstre.pochebis.models.loader.MonstreLoader;
import com.esiea.monstre.pochebis.models.visual.GameVisual;

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

    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            CombatLogger.logReseau("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            CombatLogger.logReseau("Joueur connecté depuis " + socket.getInetAddress());

            try (OnlineConnection connection = new OnlineConnection(socket, null)) {
                String nomJoueurLocal = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
                String nomJoueurDistant = connection.ask("Entrez votre nom de joueur >");

                Joueur joueurLocal = new Joueur(nomJoueurLocal);
                Joueur joueurDistant = new Joueur(nomJoueurDistant);

                CombatEnLigne combat = new CombatEnLigne(joueurLocal, joueurDistant, connection);
                combat.lancer(monstreLoader, attaqueLoader);
            }
        } catch (IOException e) {
            CombatLogger.error("Problème de serveur : " + e.getMessage());
        }
    }

    /** Fonctions pour le mode interface */

    public OnlineConnection demarrerServeur(Joueur hostPlayer) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            CombatLogger.logReseau("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            CombatLogger.logReseau("Joueur connecté depuis " + socket.getInetAddress());

            OnlineConnection connection = new OnlineConnection(socket, hostPlayer);
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
