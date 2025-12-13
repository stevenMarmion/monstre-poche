package src.com.esiea.monstre.poche.entites;

import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.affinites.Type;
import src.com.esiea.monstre.poche.etats.StatutMonstre;
import java.util.ArrayList;

public class Monstre {
    private String nomMonstre;
    private int pointsDeVie;
    private int attaque;
    private int defense;
    private int vitesse;
    private ArrayList<Attaque> attaques;
    private Type typeMonstre;
    private StatutMonstre statut;

    public Monstre(String nomMonstre, int pointsDeVie, int attaque, int defense, int vitesse, ArrayList<Attaque> attaques, Type typeMonstre) {
        this.nomMonstre = nomMonstre;
        this.pointsDeVie = pointsDeVie;
        this.attaque = attaque;
        this.defense = defense;
        this.vitesse = vitesse;
        this.attaques = attaques;
        this.statut = null;
        this.typeMonstre = typeMonstre;
    }

    public String getNomMonstre() {
        return nomMonstre;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }

    public int getVitesse() {
        return vitesse;
    }

    public ArrayList<Attaque> getAttaques() {
        return attaques;
    }

    public Type getTypeMonstre() {
        return typeMonstre;
    }

    public StatutMonstre getStatut() {
        return statut;
    }

    public void setNomMonstre(String nomMonstre) {
        this.nomMonstre = nomMonstre;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    public void setAttaques(ArrayList<Attaque> attaques) {
        this.attaques = attaques;
    }

    public void setStatut(StatutMonstre statut) {
        this.statut = statut;
    }

    public void setTypeMonstre(Type typeMonstre) {
        this.typeMonstre = typeMonstre;
    }

    public void attaquer(Monstre cible) {
        // Logique d'attaque à implémenter
    }
}
