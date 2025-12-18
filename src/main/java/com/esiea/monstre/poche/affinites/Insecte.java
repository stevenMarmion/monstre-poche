package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Empoisonne;

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
        if ((nbTourAttaque % NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT) == 0) {
            nbTourAttaque++;
            cible.setStatut(new Empoisonne());   
            System.out.println(cible.getNomMonstre() + " est empoisonné !");
        }
        super.appliqueCapaciteSpeciale(cible);
    }
    
}
