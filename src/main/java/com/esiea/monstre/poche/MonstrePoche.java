package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.bot.Bot;
import com.esiea.monstre.poche.combats.Combat;
import com.esiea.monstre.poche.combats.CombatBot;
import com.esiea.monstre.poche.entites.Joueur;
// import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.etats.Asseche;
// import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;
import com.esiea.monstre.poche.visual.GameVisual;

public class MonstrePoche {
    public static void main(String[] args) {
        AttaqueLoader attaqueLoader = new AttaqueLoader("attacks.txt");
        attaqueLoader.charger();
        MonstreLoader monstreLoader = new MonstreLoader("monsters.txt");
        monstreLoader.charger();

        Scanner scanner = new Scanner(System.in);
        int modeJeu = GameVisual.afficherMenuModeJeu(scanner);

        String nomJoueur1 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 1 >");
        Joueur joueur1 = new Joueur(nomJoueur1);
        Terrain terrain = new Terrain("Terrain de combat", new Asseche());

        if (modeJeu == 1) {
            int difficulteBout = GameVisual.afficherMenuDifficulteBot(scanner);
            Bot bot = new Bot("Monstre poche - Bot", difficulteBout);
            
            bot.chargerMonstresAutomatiquement(monstreLoader);
            bot.chargerAttaquesAutomatiquement(attaqueLoader);
            
            CombatBot combatBot = new CombatBot(joueur1, bot, terrain);
            combatBot.lancer(monstreLoader, attaqueLoader);
        } else {
            String nomJoueur2 = GameVisual.demanderSaisie(scanner, "Entrez le nom du Joueur 2 >");
            Joueur joueur2 = new Joueur(nomJoueur2);
            
            Combat combat = new Combat(joueur1, joueur2, terrain);
            combat.lancer(monstreLoader, attaqueLoader);
        }
        
        scanner.close();
    }
}
