package com.esiea.monstre.poche.models.items.medicaments;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Normal;

public class MedicamentAntiBrulure extends Medicament {

    public MedicamentAntiBrulure(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Brule")) {
            CombatLogger.info("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
            CombatLogger.info("-> " + cible.getNomMonstre() + " n'est plus brûlé !");
        } else {
            CombatLogger.info("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre() + " - Aucun effet (pas brûlé)");
        }
    }

    @Override
    public MedicamentAntiBrulure copyOf() {
        return new MedicamentAntiBrulure(super.nomObjet);
    }
}
