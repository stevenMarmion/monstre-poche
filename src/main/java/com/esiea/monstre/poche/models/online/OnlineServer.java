package com.esiea.monstre.poche.models.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.CombatEnLigne;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.visual.GameVisual;

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
            System.out.println("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            System.out.println("Joueur connecte depuis " + socket.getInetAddress());

            try (OnlineConnection connection = new OnlineConnection(socket)) {
                String nomJoueurLocal = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
                String nomJoueurDistant = connection.ask("Entrez votre nom de joueur >");

                Joueur joueurLocal = new Joueur(nomJoueurLocal);
                Joueur joueurDistant = new Joueur(nomJoueurDistant);

                CombatEnLigne combat = new CombatEnLigne(joueurLocal, joueurDistant, connection);
                combat.lancer(monstreLoader, attaqueLoader);
            }
        } catch (IOException e) {
            System.out.println("[ERREUR] Probleme de serveur : " + e.getMessage());
        }
    }
}
