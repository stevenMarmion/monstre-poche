package com.esiea.monstre.poche.models.inventaire.medicaments;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Normal;

public class MedicamentAntiPoison extends Medicament {

    public MedicamentAntiPoison(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
            CombatLogger.log("  -> " + cible.getNomMonstre() + " n'est plus empoisonné !");
        } else {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre() + " - Aucun effet (pas empoisonné)");
        }
    }

    @Override
    public MedicamentAntiPoison copyOf() {
        return new MedicamentAntiPoison(super.nomObjet);
    }
    
}
