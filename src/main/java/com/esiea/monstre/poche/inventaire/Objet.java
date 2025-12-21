package com.esiea.monstre.poche.inventaire;

import com.esiea.monstre.poche.entites.Monstre;
import java.io.Serializable;

public abstract class Objet implements Serializable {
    protected static final long serialVersionUID = 1L;
    protected String nomObjet;

    public String getNomObjet() {
        return this.nomObjet;
    }

    public abstract void utiliserObjet(Monstre cible);

    public abstract Objet copyOf();
}
