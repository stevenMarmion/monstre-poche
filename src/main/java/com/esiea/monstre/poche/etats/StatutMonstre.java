package com.esiea.monstre.poche.etats;

import java.io.Serializable;

public abstract class StatutMonstre implements Serializable {
    private static final long serialVersionUID = 1L;
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
}
