package com.esiea.monstre.poche.models.types;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Empoisonne;

public class Insecte extends Nature {
    private static final int NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT = 3;
    
    private int nbTourAttaque;

    public Insecte() {
        this.labelType = "Insecte";
        this.fortContre = "Plante";
        this.faibleContre = "Feu";
        this.nbTourAttaque = 1;
    }

    @Override
    public void appliqueCapaciteSpeciale(Monstre cible) {
        if ((nbTourAttaque % NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT) == 0) {
            cible.setStatut(new Empoisonne());   
            CombatLogger.log(cible.getNomMonstre() + " est désormais empoisonné.");
        }
        nbTourAttaque++;
        super.appliqueCapaciteSpeciale(cible);
    }
    
}
