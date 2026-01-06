package com.esiea.monstre.poche.models.inventaire.medicaments;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Normal;
import com.esiea.monstre.poche.models.inventaire.Objet;

public class MedicamentAntiParalysie extends Medicament {

    public MedicamentAntiParalysie(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Paralyse")) {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
            CombatLogger.log("  -> " + cible.getNomMonstre() + " n'est plus paralysé !");
        } else {
            CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre() + " - Aucun effet (pas paralysé)");
        }
    }

    @Override
    public MedicamentAntiParalysie copyOf() {
        return new MedicamentAntiParalysie(super.nomObjet);
    }
}