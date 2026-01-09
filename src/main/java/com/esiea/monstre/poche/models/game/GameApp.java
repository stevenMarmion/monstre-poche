package com.esiea.monstre.poche.models.game;

import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.ai.Bot;
import com.esiea.monstre.poche.models.battle.modes.CombatBot;
import com.esiea.monstre.poche.models.battle.modes.CombatLocalTerminal;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.network.OnlineClient;
import com.esiea.monstre.poche.models.network.OnlineServer;
import com.esiea.monstre.poche.views.MonstrePocheUI;

import javafx.application.Application;

public class GameApp {
    private static final int LANCEMENT_JEU_BOT = 1;
    private static final int LANCEMENT_JEU_LOCAL = 2;
    private static final int LANCEMENT_JEU_ONLINE = 3;

    private static final int DEMARRAGE_SERVEUR = 1;
    private static final int CONNEXION_SERVEUR = 2;

    private static final Scanner scanner = new Scanner(System.in);

    public static void startAppTerminal() {
        GameResourcesFactory.getInstance();

        int modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        switch (modeJeu) {
            case LANCEMENT_JEU_BOT:
                lancementJeuBot();
                break;

            case LANCEMENT_JEU_LOCAL:
                lancementJeuLocal();
                break;

            case LANCEMENT_JEU_ONLINE:
                lancementJeuOnline();
                break;
            default:
                break;
        }
    }

    public static void startAppInterface() {
        Application.launch(MonstrePocheUI.class);
    }

    private static void lancementJeuLocal() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
        Joueur joueur2 = new Joueur(nomJoueur2);

        CombatLocalTerminal combat = new CombatLocalTerminal(joueur1, joueur2);
        combat.lancer();
    }

    private static void lancementJeuBot() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
        Joueur joueur1 = new Joueur(nomJoueur1);

        int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
        Bot bot = new Bot("Kylian le Bot", difficulteBout);

        CombatBot combatBot = new CombatBot(joueur1, bot);
        combatBot.lancer();
    }

    private static void lancementJeuOnline() {
        int choixEnLigne = GameVisual.afficherMenuJeuEnLigne(scanner);

        if (choixEnLigne == DEMARRAGE_SERVEUR) {
            int port = GameVisual.demanderPortServeur(scanner);
            OnlineServer server = new OnlineServer(port, scanner);
            server.lancer();
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
