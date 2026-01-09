package com.esiea.monstre.poche.models.status.monster;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

public class Brule extends StatutMonstre {
    
    public Brule() {
        this.labelStatut = "Brule";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        double degatsBrule = cible.getAttaque() / 10.0;
        if (cible.getPointsDeVie() - degatsBrule < 0) {
            degatsBrule = cible.getPointsDeVie();
        }
        cible.setPointsDeVie(cible.getPointsDeVie() - degatsBrule);
        CombatLogger.info(cible.getNomMonstre() + " est brûlé ! Subit " + (int)degatsBrule + " dégâts de brûlure");
    }
}
