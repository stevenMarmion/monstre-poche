package com.esiea.monstre.poche.models.inventaire.medicaments;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Normal;

public class MedicamentAntiBrulure extends Medicament {

    public MedicamentAntiBrulure(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Brule")) {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }

    @Override
    public MedicamentAntiBrulure copyOf() {
        return new MedicamentAntiBrulure(super.nomObjet);
    }
}
