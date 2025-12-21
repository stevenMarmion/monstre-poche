package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.bot.Bot;
import com.esiea.monstre.poche.combats.Combat;
import com.esiea.monstre.poche.combats.CombatBot;
import com.esiea.monstre.poche.configuration.enums.ModeJeu;
import com.esiea.monstre.poche.online.OnlineClient;
import com.esiea.monstre.poche.online.OnlineServer;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.etats.Asseche;
import com.esiea.monstre.poche.loader.*;
import com.esiea.monstre.poche.visual.GameVisual;

public class MonstrePoche {
    public static void main(String[] args) {

        // charger les données du jeu : monstres, attaques, objets, ...
        GameResourcesLoader resourcesLoader = new GameResourcesLoader();

        GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);
        Scanner scanner = new Scanner(System.in);
        ModeJeu modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        Terrain terrain = new Terrain("Terrain de combat", new Asseche());

        if (ModeJeu.JOUEUR_CONTRE_IA.equals(modeJeu)) {
            int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
            Bot bot = new Bot("Monstre poche - Bot", difficulteBout);

            bot.chargerMonstresAutomatiquement(resourcesFactory);
            bot.chargerAttaquesAutomatiquement(resourcesFactory);
            bot.chargerObjetsAutomatiquement(resourcesFactory);

            CombatBot combatBot = new CombatBot(joueur1, bot, terrain);
            combatBot.lancer(resourcesFactory);
        } else if (ModeJeu.JOUEUR_CONTRE_JOUEUR.equals(modeJeu)) {
            String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
            Joueur joueur2 = new Joueur(nomJoueur2);

            Combat combat = new Combat(joueur1, joueur2, terrain);
            combat.lancer(resourcesFactory);
        }
        else if (ModeJeu.JCJ_EN_LIGNE.equals(modeJeu)) {
            int choixEnLigne = GameVisual.afficherMenuJeuEnLigne(scanner);
            if (choixEnLigne == 1) {
                int port = GameVisual.demanderPortServeur(scanner);
                OnlineServer server = new OnlineServer(port, scanner);
                server.lancer(resourcesFactory);
            } else {
                String[] config = GameVisual.demanderConfigurationServeur(scanner);
                String adresse = config[0];
                int port = Integer.parseInt(config[1]);
                OnlineClient client = new OnlineClient(adresse, port);
                client.lancer();

            }
        scanner.close();
        }
    }
}