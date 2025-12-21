package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.bot.Bot;
import com.esiea.monstre.poche.combats.Combat;
import com.esiea.monstre.poche.combats.CombatBot;
import com.esiea.monstre.poche.configuration.enums.ModeJeu;
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
        } else {
            String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
            Joueur joueur2 = new Joueur(nomJoueur2);
            
            Combat combat = new Combat(joueur1, joueur2, terrain);
            combat.lancer(resourcesFactory);
        }
        
        scanner.close();
    }
}
