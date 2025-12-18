package com.esiea.monstre.poche.entites;

import java.util.ArrayList;
import com.esiea.monstre.poche.inventaire.Objet;

public class Joueur {
    private String nomJoueur;
    private ArrayList<Monstre> monstres;
    private Monstre monstreActuel;
    private ArrayList<Objet> objets;

    public Joueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.monstres = new ArrayList<Monstre>();
        this.objets = new ArrayList<Objet>();
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public ArrayList<Monstre> getMonstres() {
        return monstres;
    }

    public Monstre getMonstreActuel() {
        return monstreActuel;
    }

    public void setMonstreActuel(Monstre monstreActuel) {
        System.out.println("[CHANGEMENT] " + this.nomJoueur + " envoie " + monstreActuel.getNomMonstre() + " au combat.");
        this.monstreActuel = monstreActuel;
    }

    public ArrayList<Objet> getObjets() {
        return objets;
    }

    public void ajouterMonstre(Monstre monstre) {
        if (monstres.size() >= 3) {
            System.out.println("[INFO] La liste de monstres est pleine.");
            return;
        }

        boolean estMonstrePresentDansListe = false;
        for (Monstre m : monstres) {
            if (m == monstre) {
                estMonstrePresentDansListe = true;
                break;
            }
        }
        if (!estMonstrePresentDansListe) {
            monstres.add(monstre);
        } else {
            System.out.println("[INFO] Monstre deja present dans la liste.");
        }
    }

    public void ajouterObjet(Objet objet) {
        if (this.objets.size() <= 5) {
            if (!this.objets.contains(objet)) {
                this.objets.add(objet);
            }
        }
    }

    public boolean sontMonstresMorts() {
        for (Monstre m : monstres) {
            if (m.getPointsDeVie() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Joueur [nomJoueur=" + nomJoueur + ", monstres=" + monstres + ", monstreActuel=" + monstreActuel + ", objets=" + objets + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        Joueur other = (Joueur) obj;
        return (this.nomJoueur.equals(other.getNomJoueur()) && this.monstres.equals(other.getMonstres()) && this.objets.equals(other.getObjets()));
    }
}
