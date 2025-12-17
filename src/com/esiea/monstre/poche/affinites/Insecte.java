package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.Empoisonne;

public class Insecte extends Nature {
    private static final int NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT = 3;
    private int nbTourAttaque = 1;

    public Insecte() {
        this.labelType = "Insecte";
    }

    public void empoisonner(Monstre cible) {
        if ((nbTourAttaque % NB_TOUR_MAX_ATTAQUE_EMPOISONNEMENT) == 0) {
            nbTourAttaque++;
            cible.setStatut(new Empoisonne());   
        }
    }
    
}
