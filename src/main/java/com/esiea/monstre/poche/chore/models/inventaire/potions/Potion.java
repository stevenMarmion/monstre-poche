package com.esiea.monstre.poche.chore.models.inventaire.potions;

import com.esiea.monstre.poche.chore.models.inventaire.Objet;

public abstract class Potion extends Objet {

    public Potion(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}