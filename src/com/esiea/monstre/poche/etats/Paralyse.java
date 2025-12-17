package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Paralyse extends StatutMonstre {
    private static final double CHANCE_RATER_ATTAQUE = 0.25;

    public Paralyse() {
        this.labelStatut = "Paralyse";
    }

    public boolean rateAttaque(Monstre cible) {
        if (Math.random() < CHANCE_RATER_ATTAQUE) {
            System.out.println(cible.getNomMonstre() + " est paralysé et rate son attaque !");
            return true;
        } else {
            return false;
        }
    }

    public void sortParalysie(Monstre cible) {
        boolean chanceSortie = Math.random() < (this.nbToursAvecEffet / this.nbToursEffet);
        if (chanceSortie) {
            cible.setStatut(new Normal());
        }
    }

    @Override
    public void appliquerEffets(Monstre cible) {
        if (this.nbToursEffet > 0) {
            sortParalysie(cible);
            if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
                rateAttaque(cible); //  A GERER PARCE QUE DU COUP COMMENT ON REMONTE L'INFO QUE L'ATTAQUE A RATE ???
            }
            this.decrementerNbToursAvecEffet();
        }
    }
}
