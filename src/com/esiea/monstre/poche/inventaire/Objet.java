package src.com.esiea.monstre.poche.inventaire;

import src.com.esiea.monstre.poche.entites.Monstre;

public abstract class Objet {
    protected String nomObjet;

    public String getNomObjet() {
        return this.nomObjet;
    }

    public abstract void utiliserObjet(Monstre cible);
}
