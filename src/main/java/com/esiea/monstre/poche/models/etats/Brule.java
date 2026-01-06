package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Brule extends StatutMonstre {
    
    public Brule() {
        this.labelStatut = "Brule";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        // CDC: subit un dixième de son attaque
        double degatsBrule = cible.getAttaque() / 10.0;
        if (cible.getPointsDeVie() - degatsBrule < 0) {
            degatsBrule = cible.getPointsDeVie(); // Ne pas aller en négatif
        }
        cible.setPointsDeVie(cible.getPointsDeVie() - degatsBrule);
        CombatLogger.log(cible.getNomMonstre() + " est brûlé ! Subit " + (int)degatsBrule + " dégâts de brûlure.");
    }
}
