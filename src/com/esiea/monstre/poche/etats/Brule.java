package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

// import src.com.esiea.monstre.poche.entites.Monstre;

public class Brule extends StatutMonstre {
    
    public Brule() {
        this.labelStatut = "Brule";
    }

    public void appliquerEffets(Monstre cible, double degats) {
        double degatsPoison = degats / 10;
        if (cible.getPointsDeVie() - degatsPoison < 0) {
            degatsPoison = 0;
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsPoison);
        }
    }
}
