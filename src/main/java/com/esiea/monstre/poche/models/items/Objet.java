package com.esiea.monstre.poche.models.items;

import java.io.Serializable;

import com.esiea.monstre.poche.models.core.Monstre;

public abstract class Objet implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String nomObjet;

    public String getNomObjet() {
        return this.nomObjet;
    }

    public abstract void utiliserObjet(Monstre cible);
    public abstract Objet copyOf();
}
