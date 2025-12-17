package com.esiea.monstre.poche;

// import com.esiea.monstre.poche.entites.Joueur;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.loader.AttaqueLoader;

import java.util.List;

public class Combat {

    public static void main(String[] args) {
        System.out.println("Application lancée !!");

        AttaqueLoader attaqueLoader = new AttaqueLoader("attacks.txt");

        if (attaqueLoader.charger()){
            List<Attaque> attaquesADisposition = attaqueLoader.getRessources();
            for (Attaque atq: attaquesADisposition){
                System.out.println(atq);
            }
        }


    }

    public void demarrerCombat() {
        // toute notre logique de démarrage pour un combat (choix nom joueur, choix pokemons et attaques)
    }

    public void selectionnerMonstre() {
        // ici on fera ce qui faut pour que les joueurs choisissent leurs monstres
    }
}
