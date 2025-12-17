package com.esiea.monstre.poche.entites;

import java.util.ArrayList;

import com.esiea.monstre.poche.inventaire.medicaments.Medicament;
import com.esiea.monstre.poche.inventaire.potions.Potion;

public class Joueur {
    private String nomJoueur;
    private boolean enAttenteAtion;
    private ArrayList<Monstre> monstres;
    private Monstre monstreActuel;
    private ArrayList<Potion> potions;
    private ArrayList<Medicament> medicaments;

    public Joueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.enAttenteAtion = false;
        this.monstres = new ArrayList<Monstre>();
        this.potions = new ArrayList<Potion>();
        this.medicaments = new ArrayList<Medicament>();
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public boolean isEnAttenteAtion() {
        return enAttenteAtion;
    }

    public ArrayList<Monstre> getMonstres() {
        return monstres;
    }

    public Monstre getMonstreActuel() {
        return monstreActuel;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setEnAttenteAtion(boolean enAttenteAtion) {
        this.enAttenteAtion = enAttenteAtion;
    }

    public void setMonstres(ArrayList<Monstre> monstres) {
        this.monstres = monstres;
    }

    public void setMonstreActuel(Monstre monstreActuel) {
        this.monstreActuel = monstreActuel;
    }

    public void setPotions(ArrayList<Potion> potions) {
        this.potions = potions;
    }

    public void setMedicaments(ArrayList<Medicament> medicaments) {
        this.medicaments = medicaments;
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

    public void ajouterPotion(Potion potion) {
        this.potions.add(potion);
    }

    public void ajouterMedicament(Medicament medicament) {
        this.medicaments.add(medicament);
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
