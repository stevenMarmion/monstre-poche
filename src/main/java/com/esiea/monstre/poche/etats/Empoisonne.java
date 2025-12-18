package com.esiea.monstre.poche.etats;

import com.esiea.monstre.poche.entites.Monstre;

public class Empoisonne extends StatutMonstre {
    
    public Empoisonne() {
        this.labelStatut = "Empoisonne";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        double degatsPoison = degats / 10;
        if (cible.getPointsDeVie() - degatsPoison < 0) {
            degatsPoison = 0;
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsPoison);
            System.out.println(cible.getNomMonstre() + " subit " + degatsPoison + " points de dégâts de poison.");
        }
    }
}
