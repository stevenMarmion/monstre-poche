package com.esiea.monstre.pochebis.models.etats;

import java.io.Serializable;

public abstract class StatutTerrain implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String labelStatut;
    protected int nbToursEffet;
    protected int nbToursAvecEffet;

    public void decrementerNbToursAvecEffet() {
        if (nbToursAvecEffet > 0) {
            nbToursAvecEffet--;
        }
    }

    public int getNbToursEffet() {
        return this.nbToursEffet;
    }

    public String getLabelStatut() {
        return this.labelStatut;
    }
}
