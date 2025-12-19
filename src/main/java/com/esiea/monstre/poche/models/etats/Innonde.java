package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.entites.Monstre;

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
            System.out.println(cible.getNomMonstre() + " est soigné de son statut néfaste grâce à l'eau.");
        }

        if (Math.random() < this.probabiliteFaireChuter) {
            cible.setRateAttaque(true);
            cible.setPointsDeVie(cible.getPointsDeVie() - (degats * 0.25));
            System.out.println(cible.getNomMonstre() + " a chuté à cause de l'eau et rate sa prochaine attaque.");
        } else {
            cible.setRateAttaque(false);
        }
    }
}
