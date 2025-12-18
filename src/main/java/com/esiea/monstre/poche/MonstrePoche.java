package com.esiea.monstre.poche;

import com.esiea.monstre.poche.entites.Joueur;
// import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.etats.Asseche;
// import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;

public class MonstrePoche {
    public static void main(String[] args) {
        AttaqueLoader attaqueLoader = new AttaqueLoader("attacks.txt");
        attaqueLoader.charger();

        MonstreLoader monstreLoader = new MonstreLoader("monsters.txt");
        monstreLoader.charger();

        Joueur joueur1 = new Joueur("Steven");
        Joueur joueur2 = new Joueur("Kylian");

        Terrain terrain = new Terrain("Terrain de combat", new Asseche());

        Combat combat = new Combat(joueur1, joueur2, terrain);
        combat.lancer(monstreLoader, attaqueLoader);
    }
}
