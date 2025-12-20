package com.esiea.monstre.poche.models;

import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.Combat;
import com.esiea.monstre.poche.models.combats.CombatBot;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
import com.esiea.monstre.poche.models.online.OnlineClient;
import com.esiea.monstre.poche.models.online.OnlineServer;
import com.esiea.monstre.poche.models.visual.GameVisual;

public class AppTerminal {
    public static final int LANCEMENT_JEU_LOCAL = 1;
    public static final int LANCEMENT_JEU_BOT = 2;
    public static final int LANCEMENT_JEU_ONLINE = 3;

    public static Scanner scanner = new Scanner(System.in);

    public static MonstreLoader monstreLoader;
    public static AttaqueLoader attaqueLoader;

    public static void startAppTerminal() {
        chargeLoaders();
        int modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        switch (modeJeu) {
            case LANCEMENT_JEU_LOCAL:
                lancementJeuLocal();
                break;
        
            case LANCEMENT_JEU_BOT:
                lancementJeuBot();
                break;

            case LANCEMENT_JEU_ONLINE:
                lancementJeuOnline();
                break;
            default:
                break;
        }
    }

    public static void lancementJeuLocal() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
        Joueur joueur1 = new Joueur(nomJoueur1);

        int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
        Bot bot = new Bot("Monstre poche - Bot", difficulteBout);

        bot.chargerMonstresAutomatiquement(monstreLoader);
        bot.chargerAttaquesAutomatiquement(attaqueLoader);
        
        CombatBot combatBot = new CombatBot(joueur1, bot);
        combatBot.lancer(monstreLoader, attaqueLoader);
    }

    public static void lancementJeuBot() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
        Joueur joueur2 = new Joueur(nomJoueur2);
        
        Combat combat = new Combat(joueur1, joueur2);
        combat.lancer(monstreLoader, attaqueLoader);
    }

    public static void lancementJeuOnline() {
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

    public static void chargeLoaders() {
        attaqueLoader = new AttaqueLoader("attacks.txt");
        attaqueLoader.charger();
        monstreLoader = new MonstreLoader("monsters.txt");
        monstreLoader.charger();
    }
}
