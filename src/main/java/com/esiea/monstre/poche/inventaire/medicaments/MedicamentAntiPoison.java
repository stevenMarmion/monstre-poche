package com.esiea.monstre.poche.inventaire.medicaments;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Normal;

public class MedicamentAntiPoison extends Medicament {

    public MedicamentAntiPoison(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserMedicament(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }
    
}
