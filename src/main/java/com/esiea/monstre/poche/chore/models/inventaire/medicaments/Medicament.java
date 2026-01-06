package com.esiea.monstre.poche.chore.models.inventaire.medicaments;

import com.esiea.monstre.poche.chore.models.inventaire.Objet;

public abstract class Medicament extends Objet {

    public Medicament(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}
