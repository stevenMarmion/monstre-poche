package com.esiea.monstre.poche.models;

import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.CombatBot;
import com.esiea.monstre.poche.models.combats.CombatLocalTerminal;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.loader.GameResourcesFactory;
import com.esiea.monstre.poche.models.loader.GameResourcesLoader;
import com.esiea.monstre.poche.models.online.OnlineClient;
import com.esiea.monstre.poche.models.online.OnlineServer;
import com.esiea.monstre.poche.models.visual.GameVisual;
import com.esiea.monstre.poche.views.MonstrePocheUI;

public class App {
    private static final int LANCEMENT_JEU_BOT = 1;
    private static final int LANCEMENT_JEU_LOCAL = 2;
    private static final int LANCEMENT_JEU_ONLINE = 3;

    private static final int DEMARRAGE_SERVEUR = 1;
    private static final int CONNEXION_SERVEUR = 2;

    private static final Scanner scanner = new Scanner(System.in);

    public static void startAppTerminal() {
        GameResourcesLoader resourcesLoader = new GameResourcesLoader();
        GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);
        int modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        switch (modeJeu) {
            case LANCEMENT_JEU_BOT:
                lancementJeuBot(resourcesFactory);
                break;
            
            case LANCEMENT_JEU_LOCAL:
                lancementJeuLocal(resourcesFactory);
                break;

            case LANCEMENT_JEU_ONLINE:
                lancementJeuOnline(resourcesFactory);
                break;
            default:
                break;
        }
    }

    public static void startAppInterface() {
//        Loaders.chargeLoaders();
        //TODO A voir
        MonstrePocheUI.main(null);
    }

    private static void lancementJeuLocal(GameResourcesFactory resourcesFactory) {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
        Joueur joueur2 = new Joueur(nomJoueur2);
        
        CombatLocalTerminal combat = new CombatLocalTerminal(joueur1, joueur2);
        combat.lancer(resourcesFactory);
    }

    private static void lancementJeuBot(GameResourcesFactory resourcesFactory) {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        
        int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
        Bot bot = new Bot("Kylian le Bot", difficulteBout);

        bot.chargerMonstresAutomatiquement(resourcesFactory);
        bot.chargerAttaquesAutomatiquement(resourcesFactory);
        bot.chargerObjetsAutomatiquement(resourcesFactory);

        CombatBot combatBot = new CombatBot(joueur1, bot);
        combatBot.lancer(resourcesFactory);
    }

    private static void lancementJeuOnline(GameResourcesFactory resourcesFactory) {
        int choixEnLigne = GameVisual.afficherMenuJeuEnLigne(scanner);

        if (choixEnLigne == DEMARRAGE_SERVEUR) {
            int port = GameVisual.demanderPortServeur(scanner);
            OnlineServer server = new OnlineServer(port, scanner);
            server.lancer(resourcesFactory);
        } 
        if (choixEnLigne == CONNEXION_SERVEUR) {
            String[] config = GameVisual.demanderConfigurationServeur(scanner);
            String adresse = config[0];
            int port = Integer.parseInt(config[1]);
            OnlineClient client = new OnlineClient(adresse, port);
            client.lancer();
        }
    }
}
