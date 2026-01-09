package com.esiea.monstre.poche.models.items.medicaments;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.Normal;

public class MedicamentAntiPoison extends Medicament {

    public MedicamentAntiPoison(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            CombatLogger.info("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
            CombatLogger.info("-> " + cible.getNomMonstre() + " n'est plus empoisonné !");
        } else {
            CombatLogger.info("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre() + " - Aucun effet (pas empoisonné)");
        }
    }

    @Override
    public MedicamentAntiPoison copyOf() {
        return new MedicamentAntiPoison(super.nomObjet);
    }
    
}
