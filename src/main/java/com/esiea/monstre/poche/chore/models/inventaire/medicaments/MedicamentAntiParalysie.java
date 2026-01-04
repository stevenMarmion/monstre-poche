package com.esiea.monstre.poche.chore.models.inventaire.medicaments;

import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.entites.Monstre;
import com.esiea.monstre.poche.chore.models.etats.Normal;

public class MedicamentAntiParalysie extends Medicament {

    public MedicamentAntiParalysie(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }
}