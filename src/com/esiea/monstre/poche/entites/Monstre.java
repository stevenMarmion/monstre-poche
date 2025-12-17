package src.com.esiea.monstre.poche.entites;

import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.affinites.Eau;
import src.com.esiea.monstre.poche.affinites.Foudre;
import src.com.esiea.monstre.poche.affinites.Insecte;
import src.com.esiea.monstre.poche.affinites.Nature;
import src.com.esiea.monstre.poche.affinites.Feu;
import src.com.esiea.monstre.poche.affinites.Terre;
import src.com.esiea.monstre.poche.affinites.Type;
import src.com.esiea.monstre.poche.etats.Normal;
import src.com.esiea.monstre.poche.etats.StatutMonstre;
import java.util.ArrayList;

public class Monstre {
    private static final int COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES = 20;
    private static final int COEFF_MULTIPLICATEUR_DEGATS_ATTAQUE = 11;
    private static final int COEFF_MULTIPLICATEUR_DEFENSE_ADVERSAIRE = 25;
    private static final double BORNE_MIN_COEF_RANDOM = 0.85;
    private static final double BORNE_MAX_COEF_RANDOM = 1.0;
    private static final int POINTS_DE_VIE_MINIMAL = 0;
    private static final int COEF_DOUBLE_DEGATS = 2;
    private static final int COEF_DIVISE_DEGATS = 2;
    private static final double COEF_QUART_DEGATS = 0.25;
    private static final double COEF_DIXIEME_DEGATS = 0.1;

    private String nomMonstre;
    private double pointsDeVieMax;
    private double pointsDeVie;
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

    public void setNomMonstre(String nomMonstre) {
        this.nomMonstre = nomMonstre;
    }

    public void setPointsDeVie(double pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
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

    public void attaquer(Monstre cible, Terrain terrain, Attaque attaqueUtilisee) {
        if (attaqueUtilisee != null && this.attaques.contains(attaqueUtilisee)) {
            double degats = this.calculeDegatsAttaque(this, cible, attaqueUtilisee);

            if (terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
                if (!this.typeMonstre.getLabelType().equals("Eau")) {
                    if (((Eau) this.typeMonstre).faitChuter(this)) {
                        this.pointsDeVie -= degats * COEF_QUART_DEGATS;
                    }
                } else if (this.typeMonstre.getLabelType().equals("Insecte") || this.typeMonstre.getLabelType().equals("Plante")) {
                    ((Nature) this.typeMonstre).recupererSante(this);
                } else if (this.statut.getLabelStatut().equals("Empoisonne")) {
                    this.statut = new Normal();
                }
            }

             else if (this.statut.getLabelStatut().equals("Brule")) {
                this.pointsDeVie -= degats * COEF_DIXIEME_DEGATS;
            } else if (this.statut.getLabelStatut().equals("Empoisonne")) {
                this.pointsDeVie -= degats * COEF_DIXIEME_DEGATS;
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

                this.gestionEtats(cible, attaqueUtilisee, terrain);
            }
        } else { // ici, ça veut dire que le monstre attaque avec ses poings
            cible.pointsDeVie -= this.calculeDegat(this, cible);
            if (cible.pointsDeVie < POINTS_DE_VIE_MINIMAL) {
                cible.pointsDeVie = POINTS_DE_VIE_MINIMAL;
            }
        }
    }

    public void gestionEtats(Monstre cible, Attaque attaque, Terrain terrain) {
            // là on gère nos effets de bord des états
            if (attaque.getTypeAttaque().getLabelType().equals("Eau")) {
                ((Eau) this.typeMonstre).innondeTerrain(terrain);
            } else if (attaque.getTypeAttaque().getLabelType().equals("Foudre")) {
                ((Foudre) this.typeMonstre).paralyser(cible);
            } else if (attaque.getTypeAttaque().getLabelType().equals("Terre")) {
                ((Terre) this.typeMonstre).fuit(this);
            } else if (attaque.getTypeAttaque().getLabelType().equals("Feu")) {
                ((Feu) this.typeMonstre).bruler(cible);
            } else if (attaque.getTypeAttaque().getLabelType().equals("Insecte")) {
                ((Insecte) this.typeMonstre).empoisonner(cible);
            }
    }

    public double calculeDegat(Monstre monstreAttaquant, Monstre cible) {
        double coef_aleatoire = BORNE_MIN_COEF_RANDOM + (BORNE_MAX_COEF_RANDOM - BORNE_MIN_COEF_RANDOM) * Math.random();
        double degats = (COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES * (monstreAttaquant.getAttaque() / cible.getDefense()) * coef_aleatoire);
        return degats;
    }

    public double calculeDegatsAttaque(Monstre monstreAttaquant, Monstre cible, Attaque attaque) {
        int numerateur = COEFF_MULTIPLICATEUR_DEGATS_ATTAQUE * monstreAttaquant.getAttaque() * attaque.getPuissanceAttaque();
        int denominateur = COEFF_MULTIPLICATEUR_DEFENSE_ADVERSAIRE * cible.getDefense();
        double coeff = BORNE_MIN_COEF_RANDOM + (BORNE_MAX_COEF_RANDOM - BORNE_MIN_COEF_RANDOM) * Math.random();
        double avantage = 1;
        if (attaque.getTypeAttaque().estFaibleContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
            avantage = 0.5;
        } else if (attaque.getTypeAttaque().estFortContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
            avantage = 2;
        }
        return ((numerateur/denominateur) + 2) * avantage * coeff;
    }
}
