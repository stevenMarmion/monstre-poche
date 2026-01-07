package com.esiea.monstre.poche.models.items.potions;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;

public class PotionSante extends Potion {
    /**
     * Value en pourcentage de restoration des points de vie d'un monstre
     */
    private final int pointsDeSoin;

    public PotionSante(String nomObjet, int pointsDeSoin) {
        super(nomObjet);
        this.pointsDeSoin = pointsDeSoin;
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        double pvAvant = cible.getPointsDeVie();
        CombatLogger.log("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
        if (cible.getPointsDeVie() + this.pointsDeSoin > cible.getPointsDeVieMax()) {
            cible.setPointsDeVie(cible.getPointsDeVieMax());
        } else {
            cible.setPointsDeVie(cible.getPointsDeVie() + this.pointsDeSoin);
        }
        int pvRecuperes = (int)(cible.getPointsDeVie() - pvAvant);
        CombatLogger.log("  -> " + cible.getNomMonstre() + " récupère " + pvRecuperes + " PV (" + (int)cible.getPointsDeVie() + "/" + (int)cible.getPointsDeVieMax() + ")");
    }

    @Override
    public PotionSante copyOf(){
        return new PotionSante(super.nomObjet, this.pointsDeSoin);
    }
}
