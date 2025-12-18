package src.com.esiea.monstre.poche.entites;

import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.affinites.Type;
import src.com.esiea.monstre.poche.affinites.utils.AffinitesUtils;
import src.com.esiea.monstre.poche.etats.Normal;
import src.com.esiea.monstre.poche.etats.StatutMonstre;
import src.com.esiea.monstre.poche.etats.utils.StatutMonstreUtils;
import src.com.esiea.monstre.poche.etats.utils.StatutTerrainUtils;

import java.util.ArrayList;

public class Monstre {
    // private static final int COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES = 20;
    // private static final int POINTS_DE_VIE_MINIMAL = 0;
    // private static final int COEF_DOUBLE_DEGATS = 2;
    // private static final int COEF_DIVISE_DEGATS = 2;
    // private static final double COEF_QUART_DEGATS = 0.25;
    // private static final double COEF_DIXIEME_DEGATS = 0.1;

    private String nomMonstre;
    private double pointsDeVieMax;
    private double pointsDeVie;
    private int attaque;
    private int defense;
    private int vitesse;
    private ArrayList<Attaque> attaques;
    private Type typeMonstre;
    private StatutMonstre statut;
    private boolean rateAttaque;

    public Monstre(String nomMonstre, int pointsDeVie, int attaque, int defense, int vitesse, ArrayList<Attaque> attaques, Type typeMonstre) {
        this.nomMonstre = nomMonstre;
        this.pointsDeVie = pointsDeVie;
        this.pointsDeVieMax = pointsDeVie;
        this.attaque = attaque;
        this.defense = defense;
        this.vitesse = vitesse;
        this.attaques = attaques;
        this.statut = new Normal();
        this.typeMonstre = typeMonstre;
        this.rateAttaque = false;
    }

    public String getNomMonstre() {
        return nomMonstre;
    }

    public double getPointsDeVie() {
        return pointsDeVie;
    }

    public double getPointsDeVieMax() {
        return pointsDeVieMax;
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

    public boolean isRateAttaque() {
        return rateAttaque;
    }

    public void setNomMonstre(String nomMonstre) {
        this.nomMonstre = nomMonstre;
    }

    public void setPointsDeVie(double pointsDeVie) {
        if (pointsDeVie < 0) {
            this.pointsDeVie = 0;
        } else if (pointsDeVie > this.pointsDeVieMax) {
            this.pointsDeVie = this.pointsDeVieMax;
        } else {
            this.pointsDeVie = pointsDeVie;
        }
    }

    public void setPointsDeVieMax(double pointsDeVieMax) {
        this.pointsDeVieMax = pointsDeVieMax;
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

    public void setRateAttaque(boolean rateAttaque) {
        this.rateAttaque = rateAttaque;
    }

    public void ajouterAttaque(Attaque attaque) {
        if (!this.attaques.contains(attaque) && this.attaques.size() < 4) {
            this.attaques.add(attaque);
        }
    }

    public void attaquer(Monstre cible, Terrain terrain, Attaque attaqueUtilisee) {
        // on commence par calculer les dégâts, c'est la base de tout
        double degatsAffliges;
        if (attaqueUtilisee == null || !this.attaques.contains(attaqueUtilisee)) {
            degatsAffliges = calculeDegat(cible, cible);
        } else {
            degatsAffliges = attaqueUtilisee.calculeDegatsAttaque(this, cible);
        }

        // ensuite on applique nos effest avant attaque si le pokémon est paralysé ou autre
        StatutMonstreUtils.appliquerStatutMonstre(statut, this, degatsAffliges);
        StatutTerrainUtils.appliquerStatutTerrain(terrain, this, degatsAffliges);

        AffinitesUtils.appliqueCapaciteSpecialeNature(typeMonstre, cible, terrain);

        // notre attaque principal, le process principal
        if (!this.isRateAttaque()) {
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsAffliges);

            // les effets de l'attaque sépciale du monstre si elle est pas ratée
            AffinitesUtils.appliqueCapaciteSpeciale(typeMonstre, cible, terrain);
        }
    }

    public double calculeDegat(Monstre monstreAttaquant, Monstre cible) {
        double coef_aleatoire = 0.85 + (1.0 - 0.85) * Math.random();
        double degats = (20 * (monstreAttaquant.getAttaque() / cible.getDefense()) * coef_aleatoire);
        return degats;
    }
}
