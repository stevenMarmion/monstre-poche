package com.esiea.monstre.poche.online;

import java.io.*;
import java.net.*;

import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;

/**
 * Classe serveur TCP qui attend les connexions et gère les sessions de jeu en ligne.
 * Le serveur peut accueillir plusieurs paires de joueurs.
 */
public class Server {
    private ServerSocket serverSocket;
    private int port;
    private boolean isRunning;
    private AttaqueLoader attaqueLoader;
    private MonstreLoader monstreLoader;
    private static final int PORT_DEFAULT = 5555;
    
    public Server() {
        this(PORT_DEFAULT);
    }
    
    public Server(int port) {
        this.port = port;
        this.isRunning = false;
        this.attaqueLoader = new AttaqueLoader("attacks.txt");
        this.monstreLoader = new MonstreLoader("monsters.txt");
    }
    
    public Server(int port, AttaqueLoader attaqueLoader, MonstreLoader monstreLoader) {
        this.port = port;
        this.isRunning = false;
        this.attaqueLoader = attaqueLoader;
        this.monstreLoader = monstreLoader;
    }
    
    /**
     * Démarre le serveur et attend les connexions
     */
    public void demarrer() {
        try {
            // Charger les ressources
            if (attaqueLoader != null) {
                attaqueLoader.charger();
            }
            if (monstreLoader != null) {
                monstreLoader.charger();
            }
            
            serverSocket = new ServerSocket(port);
            isRunning = true;
            System.out.println("[SERVEUR] Serveur démarré sur le port " + port);
            
            // Boucle d'acceptation des connexions
            accepterConnexions();
        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible de démarrer le serveur : " + e.getMessage());
        }
    }
    
    /**
     * Accepte les connexions entrantes et crée des sessions de jeu
     */
    private void accepterConnexions() {
        ClientHandler client1 = null;
        
        while (isRunning) {
            try {
                System.out.println("[SERVEUR] En attente de connexion...");
                Socket socket1 = serverSocket.accept();
                System.out.println("[SERVEUR] Premier joueur connecté : " + socket1.getInetAddress());
                
                client1 = new ClientHandler(socket1, "Joueur1");
                
                // Attendre la deuxième connexion
                System.out.println("[SERVEUR] En attente du deuxième joueur...");
                Socket socket2 = serverSocket.accept();
                System.out.println("[SERVEUR] Deuxième joueur connecté : " + socket2.getInetAddress());
                
                ClientHandler client2 = new ClientHandler(socket2, "Joueur2");
                
                // Créer des joueurs avec les monstres sélectionnés
                Joueur joueur1 = new Joueur(client1.getIdJoueur());
                Joueur joueur2 = new Joueur(client2.getIdJoueur());
                
                // Créer une session de jeu avec les deux clients
                ServerSession session = new ServerSession(client1, client2, joueur1, joueur2, 
                                                         attaqueLoader, monstreLoader);
                Thread sessionThread = new Thread(session);
                sessionThread.start();
                
            } catch (IOException e) {
                System.err.println("[ERREUR] Erreur lors de l'acceptation de connexion : " + e.getMessage());
            }
        }
    }
    
    /**
     * Arrête le serveur
     */
    public void arreter() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("[SERVEUR] Serveur arrêté");
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }
    
    /**
     * Point d'entrée pour démarrer le serveur
     */
    public static void main(String[] args) {
        int port = PORT_DEFAULT;
        
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Port invalide. Utilisation du port par défaut : " + PORT_DEFAULT);
            }
        }
        
        Server server = new Server(port);
        server.demarrer();
    }
}
