package src.com.esiea.monstre.poche.inventaire.potions;

import src.com.esiea.monstre.poche.inventaire.Objet;

public abstract class Potion extends Objet {

    public Potion(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}