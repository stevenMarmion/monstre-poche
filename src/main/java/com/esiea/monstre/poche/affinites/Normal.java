package com.esiea.monstre.poche.affinites;

import com.esiea.monstre.poche.entites.Monstre;

public class Normal extends Type {
    
    public Normal() {
        this.labelType = "Normal";
        this.fortContre = "Terre";
        this.faibleContre = "Nature";
    }

    public void appliqueCapaciteSpeciale(Monstre cible) {
        // Pas de capacité spéciale pour le type Normal
    }
}
