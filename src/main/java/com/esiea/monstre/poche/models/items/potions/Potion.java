package com.esiea.monstre.poche.models.items.potions;

import com.esiea.monstre.poche.models.items.Objet;

public abstract class Potion extends Objet {

    public Potion(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}