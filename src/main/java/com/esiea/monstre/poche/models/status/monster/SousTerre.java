package com.esiea.monstre.poche.models.status.monster;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

public class SousTerre extends StatutMonstre {
    private static final int NB_TOURS_MAX_FUITE = 3;
    private static final int NB_TOURS_MIN_FUITE = 1;

    public SousTerre() {
        this.labelStatut = "SousTerre";
        this.nbToursEffet = (int) (Math.random() * NB_TOURS_MAX_FUITE) + NB_TOURS_MIN_FUITE;;
        this.nbToursAvecEffet = nbToursEffet;
    }

    public void appliquerEffets(Monstre cible) {
        if (this.nbToursEffet > 0) {
            if (this.nbToursAvecEffet == nbToursEffet) {
                cible.setDefense(cible.getDefense() * 2);
                CombatLogger.log(cible.getNomMonstre() + " est maintenant sous terre, sa défense double !");
            }
            this.decrementerNbToursAvecEffet();
            sortirSousTerre(cible);
        }
    }

    public void sortirSousTerre(Monstre cible) {
        if (this.nbToursAvecEffet == 0) {
            cible.setStatut(new Normal());
            cible.setDefense(cible.getDefense() / 2);
            CombatLogger.log(cible.getNomMonstre() + " sort de sous terre, sa défense revient à la normale !");
        }
    }
}
