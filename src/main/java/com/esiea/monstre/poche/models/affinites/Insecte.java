package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Empoisonne;

public class Insecte extends Nature {
    private static final int NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT = 3;
    private int nbTourAttaque = 1;

    public Insecte() {
        this.labelType = "Insecte";
        this.fortContre = "Plante";
        this.faibleContre = "Feu";
    }

    @Override
    public void appliqueCapaciteSpeciale(Monstre cible) {
        // CDC: empoisonne une attaque sur trois
        if ((nbTourAttaque % NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT) == 0) {
            cible.setStatut(new Empoisonne());   
            CombatLogger.log(cible.getNomMonstre() + " est désormais empoisonné.");
        }
        nbTourAttaque++; // Incrémenter après chaque attaque
        super.appliqueCapaciteSpeciale(cible);
    }
    
}
