package com.esiea.monstre.poche.entites;

import com.esiea.monstre.poche.etats.StatutTerrain;

public class Terrain {
    private String nomTerrain;
    private StatutTerrain statutTerrain;

    public Terrain(String nomTerrain, StatutTerrain statut) {
        this.nomTerrain = nomTerrain;
        this.statutTerrain = statut;
    }

    public String getNomTerrain() {
        return this.nomTerrain;
    }

    public void setStatutTerrain(StatutTerrain statutTerrain) {
        this.statutTerrain = statutTerrain;
    }

    public StatutTerrain getStatutTerrain() {
        return this.statutTerrain;
    }

    public boolean estInnonde() {
        return this.statutTerrain.getLabelStatut().equals("Innonde");
    }
}
