package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Terrain;

public class Asseche extends StatutTerrain {

    public Asseche() {
        this.labelStatut = "Asseche";
    }

    public void appliquerEffets(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
    }
}
