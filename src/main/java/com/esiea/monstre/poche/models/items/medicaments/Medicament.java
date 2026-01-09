package com.esiea.monstre.poche.models.items.medicaments;

import com.esiea.monstre.poche.models.items.Objet;

public abstract class Medicament extends Objet {

    public Medicament(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}
