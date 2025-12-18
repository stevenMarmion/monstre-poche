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
        this.monstreActuel = monstreActuel;
    }

    public ArrayList<Objet> getObjets() {
        return objets;
    }

    public void ajouterMonstre(Monstre monstre) {
        if (monstres.size() >= 3) {
            return;
        }

        boolean estMonstrePresentDansListe = false;
        int cptMonstres = 0;
        for (Monstre m : monstres) {
            if (m == monstre) {
                estMonstrePresentDansListe = true;
                break;
            }
            cptMonstres++;
        }
        if (!estMonstrePresentDansListe) {
            monstres.add(monstre);
        } else {
            System.out.println("Le monstre est déjà dans la liste");
        }
        
        if (cptMonstres >= 3) {
            System.out.println("La liste de monstres est pleine");
        }
    }

    public void ajouterObjet(Objet objet) {
        if (this.objets.size() <= 5) {
            if (!this.objets.contains(objet)) {
                this.objets.add(objet);
            }
        }
    }

    public void choisirMonstre(Monstre nouveauMonstre) {
        this.monstreActuel = nouveauMonstre;
    }

    public boolean sontMonstresMorts() {
        for (Monstre m : monstres) {
            if (m.getPointsDeVie() > 0) {
                return false;
            }
        }
        return true;
    }
}
