package com.esiea.monstre.poche.models.items.medicaments;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Normal;

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