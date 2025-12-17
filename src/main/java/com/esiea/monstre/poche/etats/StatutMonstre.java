package com.esiea.monstre.poche.etats;

import com.esiea.monstre.poche.entites.Monstre;

public abstract class StatutMonstre {
    protected String labelStatut;
    protected int nbToursEffet;
    protected int nbToursAvecEffet;

    public void decrementerNbToursAvecEffet() {
        if (nbToursAvecEffet > 0) {
            nbToursAvecEffet--;
        }
    }

    public String getLabelStatut() {
        return this.labelStatut;
    }

    public abstract void appliquerEffets(Monstre cible);
}
