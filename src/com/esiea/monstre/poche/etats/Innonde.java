package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Innonde extends StatutTerrain {
    private double probabiliteFaireChuter;
    
    public Innonde(int nbToursEffet) {
        this.labelStatut = "Innonde";
        this.nbToursEffet = nbToursEffet;
        this.nbToursAvecEffet = nbToursEffet;
    }

    public void appliquerEffetsTerrain(Monstre cible, double degats) {
        // on soigne un monstre qui est brûlé, dans le CDC
        if (cible.getStatut().getLabelStatut().equals("Brule") || cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            cible.setStatut(new Normal());
        }

        if (Math.random() < this.probabiliteFaireChuter) {
            cible.setRateAttaque(true);
            cible.setPointsDeVie(cible.getPointsDeVie() - (degats * 0.25));
        } else {
            cible.setRateAttaque(false);
        }
    }
}
