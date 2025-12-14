package src.com.esiea.monstre.poche.entites;

import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.affinites.Eau;
import src.com.esiea.monstre.poche.affinites.Foudre;
import src.com.esiea.monstre.poche.affinites.Terre;
import src.com.esiea.monstre.poche.affinites.Type;
import src.com.esiea.monstre.poche.etats.Normal;
import src.com.esiea.monstre.poche.etats.StatutMonstre;
import java.util.ArrayList;

public class Monstre {
    private static final int COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES = 20;
    private static final double BORNE_MIN_COEF_RANDOM = 0.85;
    private static final double BORNE_MAX_COEF_RANDOM = 1.0;
    private static final int POINTS_DE_VIE_MINIMAL = 0;
    private static final int COEF_DOUBLE_DEGATS = 2;
    private static final int COEF_DIVISE_DEGATS = 2;
    private static final double COEF_QUART_DEGATS = 0.25;

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
        this.statut = new Normal();
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

    public void attaquer(Monstre cible, Attaque attaqueUtilisee) {
        if (attaqueUtilisee != null && this.attaques.contains(attaqueUtilisee)) {
            int degats = this.calculeDegat(cible, attaqueUtilisee.getPuissanceAttaque());

            if (this.statut.getLabelStatut().equals("Mouille") && !this.typeMonstre.getLabelType().equals("Eau")) {
                if (((Eau) this.typeMonstre).faitChuter(this)) {
                    this.pointsDeVie -= degats * COEF_QUART_DEGATS;
                }
            } else {
                if (attaqueUtilisee.getTypeAttaque().estFortContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
                cible.pointsDeVie -= COEF_DOUBLE_DEGATS*degats;
                } else if (attaqueUtilisee.getTypeAttaque().estFaibleContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
                    cible.pointsDeVie -= (int) degats/COEF_DIVISE_DEGATS;
                } else {
                    cible.pointsDeVie -= degats;
                }

                if (cible.pointsDeVie < POINTS_DE_VIE_MINIMAL) {
                    cible.pointsDeVie = POINTS_DE_VIE_MINIMAL;
                }

                if (attaqueUtilisee.getTypeAttaque().getLabelType().equals("Eau")) {
                    ((Eau) this.typeMonstre).innondeTerrain(cible);
                } else if (attaqueUtilisee.getTypeAttaque().getLabelType().equals("Foudre")) {
                    ((Foudre) this.typeMonstre).paralyser(cible);
                } else if (attaqueUtilisee.getTypeAttaque().getLabelType().equals("Terre")) {
                    ((Terre) this.typeMonstre).fuit(this);
                }
            }
        } else {
            cible.pointsDeVie -= this.calculeDegat(cible, this.attaque);
            if (cible.pointsDeVie < POINTS_DE_VIE_MINIMAL) {
                cible.pointsDeVie = POINTS_DE_VIE_MINIMAL;
            }
        }
    }

    public int calculeDegat(Monstre cible, int puissanceAttaque) {
        double coef_aleatoire = BORNE_MIN_COEF_RANDOM + (BORNE_MAX_COEF_RANDOM - BORNE_MIN_COEF_RANDOM) * Math.random();
        int degats = (int) (COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES * (puissanceAttaque / cible.defense) * coef_aleatoire);
        return degats;
    }
}
