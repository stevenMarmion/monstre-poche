package com.esiea.monstre.poche.models.status.monster;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

public class Empoisonne extends StatutMonstre {
    
    public Empoisonne() {
        this.labelStatut = "Empoisonne";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        double degatsPoison = cible.getAttaque() / 10.0;
        if (cible.getPointsDeVie() - degatsPoison < 0) {
            degatsPoison = cible.getPointsDeVie();
        }
        cible.setPointsDeVie(cible.getPointsDeVie() - degatsPoison);
        CombatLogger.log(cible.getNomMonstre() + " est empoisonné ! Subit " + (int)degatsPoison + " dégâts de poison.");
    }
}
