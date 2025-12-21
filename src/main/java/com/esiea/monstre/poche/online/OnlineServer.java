package com.esiea.monstre.poche.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.poche.combats.CombatEnLigne;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.etats.Asseche;
import com.esiea.monstre.poche.loader.GameResourcesFactory;
import com.esiea.monstre.poche.visual.GameVisual;

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

    public void lancer(GameResourcesFactory resourcesFactory) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur en attente d'un joueur sur le port " + port + " ...");
            Socket socket = serverSocket.accept();
            System.out.println("Joueur connecte depuis " + socket.getInetAddress());

            try (OnlineConnection connection = new OnlineConnection(socket)) {
                String nomJoueurLocal = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
                String nomJoueurDistant = connection.ask("Entrez votre nom de joueur >");

                Joueur joueurLocal = new Joueur(nomJoueurLocal);
                Joueur joueurDistant = new Joueur(nomJoueurDistant);
                Terrain terrain = new Terrain("Terrain en ligne", new Asseche());

                CombatEnLigne combat = new CombatEnLigne(joueurLocal, joueurDistant, terrain, connection);
                combat.lancer(resourcesFactory);
            }
        } catch (IOException e) {
            System.out.println("[ERREUR] Probleme de serveur : " + e.getMessage());
        }
    }
}
