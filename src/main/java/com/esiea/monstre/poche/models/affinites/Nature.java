package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Nature extends Type {
    private static final double COEF_VINGTIEME = 0.05; // 1/20

    public Nature() {
        // Nature est la classe parente, pas de fort/faible défini ici
        // Les sous-types (Plante, Insecte) définissent leurs propres valeurs
        this.labelType = null;
        this.fortContre = null;
        this.faibleContre = null;
    }

    /**
     * CDC: Les monstres de type nature récupèrent un vingtième de leurs points de vie max
     * au début de chaque tour s'ils se trouvent sur un terrain inondé.
     */
    public void appliqueCapaciteSpeciale(Monstre cible) {
        double recuperation = cible.getPointsDeVieMax() * COEF_VINGTIEME;
        double nouveauxPV = cible.getPointsDeVie() + recuperation;
        
        if (nouveauxPV > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
            CombatLogger.log(cible.getNomMonstre() + " récupère ses points de vie au maximum grâce au terrain inondé !");
        } else {
            cible.setPointsDeVie(nouveauxPV);
            CombatLogger.log(cible.getNomMonstre() + " récupère " + (int)recuperation + " PV grâce au terrain inondé.");
        }
    }
}
