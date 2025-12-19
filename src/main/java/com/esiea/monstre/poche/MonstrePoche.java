package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.Combat;
import com.esiea.monstre.poche.models.combats.CombatBot;
import com.esiea.monstre.poche.models.online.OnlineClient;
import com.esiea.monstre.poche.models.online.OnlineServer;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.visual.GameVisual;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;

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
            String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
            Joueur joueur2 = new Joueur(nomJoueur2);
            Terrain terrain = new Terrain("Terrain par défaut", new Asseche());
            
            Combat combat = new Combat(joueur1, joueur2, terrain);
            combat.lancer(monstreLoader, attaqueLoader);
        } else if (modeJeu == 3) {
            int choixEnLigne = GameVisual.afficherMenuJeuEnLigne(scanner);

            if (choixEnLigne == 1) {
                int port = GameVisual.demanderPortServeur(scanner);
                OnlineServer server = new OnlineServer(port, scanner);
                server.lancer(monstreLoader, attaqueLoader);
            } else {
                String[] config = GameVisual.demanderConfigurationServeur(scanner);
                String adresse = config[0];
                int port = Integer.parseInt(config[1]);
                OnlineClient client = new OnlineClient(adresse, port);
                client.lancer();
            }
        }
        
        scanner.close();
    }
}

