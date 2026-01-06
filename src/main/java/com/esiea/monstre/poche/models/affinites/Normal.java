package com.esiea.monstre.poche.models.affinites;

import com.esiea.monstre.poche.models.entites.Monstre;

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
