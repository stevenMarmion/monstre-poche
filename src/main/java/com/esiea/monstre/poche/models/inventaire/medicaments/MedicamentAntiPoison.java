package com.esiea.monstre.poche.models.inventaire.medicaments;

import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Normal;
import com.esiea.monstre.poche.models.combats.CombatLogger;

public class MedicamentAntiPoison extends Medicament {

    public MedicamentAntiPoison(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }
    
}
