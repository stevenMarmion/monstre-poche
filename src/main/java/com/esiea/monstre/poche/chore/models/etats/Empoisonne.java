package com.esiea.monstre.poche.chore.models.etats;

import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Monstre;

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
            CombatLogger.log(cible.getNomMonstre() + " est empoisonné ! Subit " + (int)degatsPoison + " dégâts de poison.");
        }
    }
}
