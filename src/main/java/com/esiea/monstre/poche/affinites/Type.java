package com.esiea.monstre.poche.affinites;

public abstract class Type {
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
