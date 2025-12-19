package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.entites.Monstre;

public class Paralyse extends StatutMonstre {
    private static final double CHANCE_RATER_ATTAQUE = 0.25;

    public Paralyse() {
        this.labelStatut = "Paralyse";
    }

    public void rateAttaque(Monstre cible) {
        if (Math.random() < CHANCE_RATER_ATTAQUE) {
            cible.setRateAttaque(true);
            System.out.println(cible.getNomMonstre() + " est paralysé et rate son attaque !");
        } else {
            cible.setRateAttaque(false);
        }
    }

    public void sortParalysie(Monstre cible) {
        boolean chanceSortie = Math.random() < (this.nbToursAvecEffet / this.nbToursEffet);
        if (chanceSortie) {
            cible.setStatut(new Normal());
            System.out.println(cible.getNomMonstre() + " n'est plus paralysé !");
        }
    }

    public void appliquerEffets(Monstre cible, double degats) {
        if (this.nbToursEffet > 0) {
            sortParalysie(cible);
            decrementerNbToursAvecEffet();
            if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
                rateAttaque(cible);
            }
        }
    }
}
