package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;

public abstract class Type {
    protected String labelType;
    protected Type faibleContre;
    protected Type fortContre;

    public String getLabelType() {
        return this.labelType;
    }
    
    public Type estFortContre() {
        return this.fortContre;
    }

    public Type estFaibleContre() {
        return this.faibleContre;
    }

    public abstract void appliqueCapaciteSpeciale(Monstre cible);
}
