package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Paralyse extends StatutMonstre {
    private static final double CHANCE_RATER_ATTAQUE = 0.25;

    public Paralyse() {
        this.labelStatut = "Paralyse";
    }

    public boolean rateAttaque(Monstre cible) {
        return Math.random() < CHANCE_RATER_ATTAQUE;
    }

    public void sortParalysie(Monstre cible) {
        boolean chanceSortie = Math.random() < (this.nbToursAvecEffet / this.nbToursEffet);
        if (chanceSortie) {
            cible.setStatut(new Normal());
        }
    }
}
