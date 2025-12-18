package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Brule extends StatutMonstre {
    
    public Brule() {
        this.labelStatut = "Brule";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        double degatsBrule = degats / 10;
        if (cible.getPointsDeVie() - degatsBrule < 0) {
            degatsBrule = 0;
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsBrule);
        }
    }
}
