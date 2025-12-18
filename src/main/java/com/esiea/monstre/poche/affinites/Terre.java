package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.SousTerre;

public class Terre extends Type {
    private static final double CHANCE_FUITE = 0.05;

    public Terre() {
        this.labelType = "Terre";
        this.fortContre = "Foudre";
        this.faibleContre = "Nature";
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        boolean fuite = Math.random() < CHANCE_FUITE;
        if (fuite) {
            cible.setStatut(new SousTerre());
            System.out.println(cible.getNomMonstre() + " s'enfuit sous terre !");
            ((SousTerre) cible.getStatut()).appliquerEffets(cible);
        }
    }
}
