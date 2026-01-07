package com.esiea.monstre.poche.models.status.terrain;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Normal;
import com.esiea.monstre.poche.models.status.monster.StatutMonstre;

public class Paralyse extends StatutMonstre {
    private static final double CHANCE_RATER_ATTAQUE = 0.75;
    private static final int NB_TOURS_MAX_PARALYSIE = 6;

    public Paralyse() {
        this.labelStatut = "Paralyse";
        this.nbToursEffet = NB_TOURS_MAX_PARALYSIE;
        this.nbToursAvecEffet = 0;
    }

    public void rateAttaque(Monstre cible) {
        if (Math.random() < CHANCE_RATER_ATTAQUE) {
            cible.setRateAttaque(true);
            CombatLogger.log(cible.getNomMonstre() + " est paralysé et ne peut pas attaquer !");
        } else {
            cible.setRateAttaque(false);
            CombatLogger.log(cible.getNomMonstre() + " surmonte la paralysie et peut attaquer !");
        }
    }

    public void sortParalysie(Monstre cible) {
        double chanceSortie = (double) this.nbToursAvecEffet / (double) this.nbToursEffet;
        if (Math.random() < chanceSortie) {
            cible.setStatut(new Normal());
            CombatLogger.log(cible.getNomMonstre() + " n'est plus paralysé après " + this.nbToursAvecEffet + " tours !");
        }
    }

    public void appliquerEffets(Monstre cible, double degats) {
        this.nbToursAvecEffet++;
        sortParalysie(cible);
        if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
            rateAttaque(cible);
        }
    }
}
