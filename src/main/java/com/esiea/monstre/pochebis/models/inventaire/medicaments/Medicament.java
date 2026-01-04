package com.esiea.monstre.pochebis.models.inventaire.medicaments;

import com.esiea.monstre.pochebis.models.inventaire.Objet;

public abstract class Medicament extends Objet {

    public Medicament(String nomObjet) {
        this.nomObjet = nomObjet;
    }
}
