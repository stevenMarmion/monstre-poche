package com.esiea.monstre.poche.models.status.monster;

import java.io.Serializable;

public abstract class StatutMonstre implements Serializable {
    protected static final long serialVersionUID = 1L;
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
