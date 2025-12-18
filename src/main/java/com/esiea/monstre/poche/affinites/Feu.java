package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Brule;

public class Feu extends Type {
    private double chanceBrulure;

    public Feu() {
        this.labelType = "Feu";
        this.fortContre = "Nature";
        this.faibleContre = "Eau";
        this.chanceBrulure = Math.random(); // notre VAR, c'est juste un random entre 0 et 1
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        if (Math.random() < this.chanceBrulure) {
            cible.setStatut(new Brule());
            System.out.println(cible.getNomMonstre() + " est brûlé !");
        }
    }
}
