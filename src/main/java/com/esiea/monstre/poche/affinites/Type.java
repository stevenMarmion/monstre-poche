package com.esiea.monstre.poche.affinites;

import java.io.Serializable;

public abstract class Type implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String labelType;
    protected String faibleContre;
    protected String fortContre;

    public String getLabelType() {
        return this.labelType;
    }
    
    public String estFortContre() {
        return this.fortContre;
    }

    public String estFaibleContre() {
        return this.faibleContre;
    }
}
