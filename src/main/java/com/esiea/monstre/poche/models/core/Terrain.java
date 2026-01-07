package com.esiea.monstre.poche.models.core;

import java.io.Serializable;

import com.esiea.monstre.poche.models.status.terrain.StatutTerrain;

public class Terrain implements Serializable {
    protected static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return String.format("Terrain: %s | Statut: %s", this.nomTerrain, this.statutTerrain.getLabelStatut());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Terrain terrain = (Terrain) obj;
        return nomTerrain.equals(terrain.nomTerrain) && statutTerrain == terrain.statutTerrain;
    }
}
