package com.esiea.monstre.pochebis.models;

import java.util.Scanner;

import com.esiea.monstre.pochebis.models.combats.CombatBot;
import com.esiea.monstre.pochebis.models.combats.CombatLocalTerminal;
import com.esiea.monstre.pochebis.models.entites.Bot;
import com.esiea.monstre.pochebis.models.entites.Joueur;
import com.esiea.monstre.pochebis.models.online.OnlineClient;
import com.esiea.monstre.pochebis.models.online.OnlineServer;
import com.esiea.monstre.pochebis.models.utils.Loaders;
import com.esiea.monstre.pochebis.models.visual.GameVisual;
import com.esiea.monstre.pochebis.views.MonstrePocheUI;

public class App {
    private static final int LANCEMENT_JEU_BOT = 1;
    private static final int LANCEMENT_JEU_LOCAL = 2;
    private static final int LANCEMENT_JEU_ONLINE = 3;

    private static final int DEMARRAGE_SERVEUR = 1;
    private static final int CONNEXION_SERVEUR = 2;

    private static final Scanner scanner = new Scanner(System.in);

    public static void startAppTerminal() {
        Loaders.chargeLoaders();
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
        Loaders.chargeLoaders();
        MonstrePocheUI.main(null);
    }

    private static void lancementJeuLocal() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
        Joueur joueur2 = new Joueur(nomJoueur2);
        
        CombatLocalTerminal combat = new CombatLocalTerminal(joueur1, joueur2);
        combat.lancer(Loaders.monstreLoader, Loaders.attaqueLoader);
    }

    private static void lancementJeuBot() {
        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez votre nom de joueur >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
        Bot bot = new Bot("Kylian le Bot", difficulteBout);
        bot.chargerMonstresAutomatiquement(Loaders.monstreLoader);
        bot.chargerAttaquesAutomatiquement(Loaders.attaqueLoader);
        
        CombatBot combatBot = new CombatBot(joueur1, bot);
        combatBot.lancer(Loaders.monstreLoader, Loaders.attaqueLoader);
    }

    private static void lancementJeuOnline() {
        int choixEnLigne = GameVisual.afficherMenuJeuEnLigne(scanner);

        if (choixEnLigne == DEMARRAGE_SERVEUR) {
            int port = GameVisual.demanderPortServeur(scanner);
            OnlineServer server = new OnlineServer(port, scanner);
            server.lancer(Loaders.monstreLoader, Loaders.attaqueLoader);
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
