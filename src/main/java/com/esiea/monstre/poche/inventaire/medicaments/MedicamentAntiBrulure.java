package com.esiea.monstre.poche.inventaire.medicaments;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Normal;

public class MedicamentAntiBrulure extends Medicament {

    public MedicamentAntiBrulure(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Brulure")) {
            System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }

    @Override
    public MedicamentAntiBrulure copyOf() {
        return new MedicamentAntiBrulure(super.nomObjet);
    }
}
