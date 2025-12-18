package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Paralyse;

public class Foudre extends Type {
    private static final double CHANCE_PARALYSIE = 0; // à changer, jsp c'est quoi les chances

    public Foudre() {
        this.labelType = "Foudre";
        this.fortContre = "Eau";
        this.faibleContre = "Terre";
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < CHANCE_PARALYSIE) {
            cible.setStatut(new Paralyse());
            System.out.println(cible.getNomMonstre() + " est paralysé !");
        }
    }
}
