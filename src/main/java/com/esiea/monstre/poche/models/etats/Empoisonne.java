package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Empoisonne extends StatutMonstre {
    
    public Empoisonne() {
        this.labelStatut = "Empoisonne";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        // CDC: subit un dixième de son attaque
        double degatsPoison = cible.getAttaque() / 10.0;
        if (cible.getPointsDeVie() - degatsPoison < 0) {
            degatsPoison = cible.getPointsDeVie(); // Ne pas aller en négatif
        }
        cible.setPointsDeVie(cible.getPointsDeVie() - degatsPoison);
        CombatLogger.log(cible.getNomMonstre() + " est empoisonné ! Subit " + (int)degatsPoison + " dégâts de poison.");
    }
}
