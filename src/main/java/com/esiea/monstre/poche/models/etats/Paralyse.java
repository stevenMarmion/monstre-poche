package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Paralyse extends StatutMonstre {
    private static final double CHANCE_RATER_ATTAQUE = 0.75; // 75% de rater = 1 chance sur 4 de réussir
    private static final int NB_TOURS_MAX_PARALYSIE = 6;

    public Paralyse() {
        this.labelStatut = "Paralyse";
        this.nbToursEffet = NB_TOURS_MAX_PARALYSIE;
        this.nbToursAvecEffet = 0; // Commence à 0, incrémenté chaque tour
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
        // Probabilité de sortie = nombre de tours passés / 6
        // Au tour 6, chance = 6/6 = 100%
        double chanceSortie = (double) this.nbToursAvecEffet / (double) this.nbToursEffet;
        if (Math.random() < chanceSortie) {
            cible.setStatut(new Normal());
            CombatLogger.log(cible.getNomMonstre() + " n'est plus paralysé après " + this.nbToursAvecEffet + " tours !");
        }
    }

    public void appliquerEffets(Monstre cible, double degats) {
        // Incrémenter le nombre de tours passés avec cet effet
        this.nbToursAvecEffet++;
        
        // Vérifier si le monstre sort de la paralysie au début du tour
        sortParalysie(cible);
        
        // Si toujours paralysé, vérifier s'il rate son attaque
        if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
            rateAttaque(cible);
        }
    }
}
