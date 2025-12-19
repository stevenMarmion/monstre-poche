package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.entites.Terrain;

public class Asseche extends StatutTerrain {

    public Asseche() {
        this.labelStatut = "Asseche";
    }

    public void appliquerEffets(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
        System.out.println(terrain.getNomTerrain() + " est maintenant asséché.");
    }
}
