package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.bot.Bot;
import com.esiea.monstre.poche.combats.Combat;
import com.esiea.monstre.poche.combats.CombatBot;
import com.esiea.monstre.poche.combats.CombatOnline;
import com.esiea.monstre.poche.entites.Joueur;
// import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.etats.Asseche;
// import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;
import com.esiea.monstre.poche.online.Client;
import com.esiea.monstre.poche.online.Connexion;
import com.esiea.monstre.poche.online.Server;
import com.esiea.monstre.poche.visual.GameVisual;

public class MonstrePoche {
    public static void main(String[] args) {
        AttaqueLoader attaqueLoader = new AttaqueLoader("attacks.txt");
        attaqueLoader.charger();
        MonstreLoader monstreLoader = new MonstreLoader("monsters.txt");
        monstreLoader.charger();

        Scanner scanner = new Scanner(System.in);
        int modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        if (modeJeu == 1) {
            String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
            Joueur joueur1 = new Joueur(nomJoueur1);
            Terrain terrain = new Terrain("Terrain par défaut", new Asseche());

            int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
            Bot bot = new Bot("Monstre poche - Bot", difficulteBout);
            
            bot.chargerMonstresAutomatiquement(monstreLoader);
            bot.chargerAttaquesAutomatiquement(attaqueLoader);
            
            CombatBot combatBot = new CombatBot(joueur1, bot, terrain);
            combatBot.lancer(monstreLoader, attaqueLoader);
        } else if (modeJeu == 2) {
            String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
            Joueur joueur1 = new Joueur(nomJoueur1);
            Terrain terrain = new Terrain("Terrain par défaut", new Asseche());

            String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
            Joueur joueur2 = new Joueur(nomJoueur2);
            
            Combat combat = new Combat(joueur1, joueur2, terrain);
            combat.lancer(monstreLoader, attaqueLoader);
        } else if (modeJeu == 3) {
            gererJeuEnLigne(scanner, monstreLoader, attaqueLoader);
        }
        
        scanner.close();
    }

    /**
     * Gère le jeu en ligne
     */
    private static void gererJeuEnLigne(Scanner scanner, MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        int modeConnexion = GameVisual.afficherMenuJeuEnLigne(scanner);
        
        if (modeConnexion == 1) {
            demarrerServeur(scanner);
        } else if (modeConnexion == 2) {
            connecterAuServeur(scanner, monstreLoader, attaqueLoader);
        }
    }

    /**
     * Démarre le serveur et attend la connexion de deux joueurs
     */
    private static void demarrerServeur(Scanner scanner) {
        int port = GameVisual.demanderPortServeur(scanner);
        
        System.out.println("[INFO] Démarrage du serveur sur le port " + port);
        System.out.println("[INFO] En attente de la connexion de deux joueurs...");
        
        Server server = new Server(port);
        
        // Démarrer le serveur dans un thread séparé
        Thread serverThread = new Thread(() -> {
            server.demarrer();
        });
        serverThread.setDaemon(false);
        serverThread.start();
        
        System.out.println("[SERVEUR] Le serveur est en attente de connexions...");
        System.out.println("[INFO] Les joueurs peuvent se connecter sur : localhost:" + port);
        System.out.println("[INFO] Appuyez sur Ctrl+C pour arrêter le serveur");
        
        // Garder le serveur actif
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            System.out.println("[INFO] Serveur interrompu");
            server.arreter();
        }
    }

    /**
     * Se connecte au serveur pour jouer en ligne
     */
    private static void connecterAuServeur(Scanner scanner, MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        String[] config = GameVisual.demanderConfigurationServeur(scanner);
        String adresse = config[0];
        int port = Integer.parseInt(config[1]);
        
        System.out.println("[INFO] Tentative de connexion à " + adresse + ":" + port);
        
        Client client = Connexion.connecterAuServeur(adresse, port);
        
        if (client == null) {
            GameVisual.afficherErreur("Impossible de se connecter au serveur.");
            return;
        }
        
        String nomJoueur = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
        Joueur joueur = new Joueur(nomJoueur);
        client.setIdJoueur(nomJoueur);
        
        System.out.println("[SUCCESS] Connecté au serveur !");
        Connexion.afficherInfoConnexion(client);
        
        // Créer un terrain pour le combat en ligne
        Terrain terrain = new Terrain("Terrain de combat en ligne", new Asseche());
        
        // Lancer le combat en ligne
        CombatOnline combatOnline = new CombatOnline(joueur, client, terrain);
        combatOnline.lancer(monstreLoader, attaqueLoader);
    }
}

