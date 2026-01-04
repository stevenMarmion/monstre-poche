package com.esiea.monstre.pochebis.models.inventaire;

import java.io.Serializable;

import com.esiea.monstre.pochebis.models.entites.Monstre;

public abstract class Objet implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String nomObjet;

    public String getNomObjet() {
        return this.nomObjet;
    }

    public abstract void utiliserObjet(Monstre cible);
}
