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
import src.com.esiea.monstre.poche.etats.Paralyse;
import src.com.esiea.monstre.poche.etats.StatutMonstre;
import java.util.ArrayList;

public class Monstre {
    // private static final int COEFF_MULTIPLICATEUR_DEGATS_MAINS_NUES = 20;
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

    public void ajouterAttaque(Attaque attaque) {
        if (!this.attaques.contains(attaque) && this.attaques.size() < 4) {
            this.attaques.add(attaque);
        }
    }

    public void attaquer(Monstre cible, Terrain terrain, Attaque attaqueUtilisee) {
        // // Phase 1: effets de statut en début de tour (DoT, sorties progressives, etc.)
        // if (this.statut != null) {
        //     this.statut.appliquerEffets(this);
        // }

        // // Phase 2: gestion des empêchements d'action (paralysie qui fait rater l'attaque)
        // if (this.statut != null && this.statut.getLabelStatut().equals("Paralyse")) {
        //     Paralyse paralysie = (Paralyse) this.statut;
        //     if (paralysie.rateAttaque(this)) {
        //         // Tour perdu, on applique tout de même les effets de fin de tour
        //         if (cible.getStatut() != null) {
        //             cible.getStatut().appliquerEffets(cible);
        //         }
        //         return;
        //     }
        // }

        // // Détermination des dégâts de base (attaque choisie ou poings)
        // boolean attaqueAvecPoings = (attaqueUtilisee == null || !this.attaques.contains(attaqueUtilisee));
        // double degats = attaqueAvecPoings ? this.calculeDegat(this, cible)
        //                                   : this.calculeDegatsAttaque(this, cible, attaqueUtilisee);

        // // Phase 3: modificateurs liés au statut de l'attaquant (ex: brûlure réduit les dégâts infligés)
        // if (this.statut != null && this.statut.getLabelStatut().equals("Brule")) {
        //     degats *= (1 - COEF_DIXIEME_DEGATS); // -10% sur les dégâts infligés
        // }

        // // Phase 4: effets liés au terrain (Innonde)
        // if (terrain != null && terrain.getStatutTerrain() != null && terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
        //     // L'eau peut faire chuter les non-Eau (probabilité de Eau.faitChuter)
        //     if (!this.typeMonstre.getLabelType().equals("Eau")) {
        //         Eau effetEau = new Eau();
        //         if (effetEau.faitChuter(this)) {
        //             this.pointsDeVie -= degats * COEF_QUART_DEGATS; // auto-dégâts sur chute
        //             if (this.pointsDeVie < POINTS_DE_VIE_MINIMAL) {
        //                 this.pointsDeVie = POINTS_DE_VIE_MINIMAL;
        //             }
        //         }
        //     }

        //     // Les types Nature (Insecte/Plante) récupèrent via leur capacité spéciale
        //     if (this.typeMonstre.getLabelType().equals("Insecte") || this.typeMonstre.getLabelType().equals("Plante")) {
        //         ((Nature) this.typeMonstre).appliqueCapaciteSpeciale(this);
        //     }

        //     // L'eau nettoie le poison
        //     if (this.statut != null && this.statut.getLabelStatut().equals("Empoisonne")) {
        //         this.statut = new Normal();
        //     }
        // }

        // // Phase 5: application des avantages/faiblesses de type (uniquement pour une attaque)
        // if (!attaqueAvecPoings && attaqueUtilisee != null) {
        //     if (attaqueUtilisee.getTypeAttaque().estFortContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
        //         cible.pointsDeVie -= COEF_DOUBLE_DEGATS * degats;
        //     } else if (attaqueUtilisee.getTypeAttaque().estFaibleContre().getLabelType().equals(cible.getTypeMonstre().getLabelType())) {
        //         cible.pointsDeVie -= (int) degats / COEF_DIVISE_DEGATS;
        //     } else {
        //         cible.pointsDeVie -= degats;
        //     }
        // } else {
        //     // Attaque à mains nues
        //     cible.pointsDeVie -= degats;
        // }

        // // Clamp des PV de la cible
        // if (cible.pointsDeVie < POINTS_DE_VIE_MINIMAL) {
        //     cible.pointsDeVie = POINTS_DE_VIE_MINIMAL;
        // }

        // // Phase 6: effets secondaires de l'attaque (peuvent appliquer des statuts)
        // if (!attaqueAvecPoings && attaqueUtilisee != null) {
        //     this.gestionEtats(cible, attaqueUtilisee, terrain);
        // }

        // // Phase 7: effets de statut en fin de tour pour la cible
        // if (cible.getStatut() != null) {
        //     cible.getStatut().appliquerEffets(cible);
        // }
    }

    // public void gestionEtats(Monstre cible, Attaque attaque, Terrain terrain) {
    //         // là on gère nos effets de bord des états
    //         if (attaque.getTypeAttaque().getLabelType().equals("Eau")) {
    //             ((Eau) this.typeMonstre).innondeTerrain(terrain);
    //         } else if (attaque.getTypeAttaque().getLabelType().equals("Foudre")) {
    //             ((Foudre) this.typeMonstre).paralyser(cible);
    //         } else if (attaque.getTypeAttaque().getLabelType().equals("Terre")) {
    //             ((Terre) this.typeMonstre).fuit(this);
    //         } else if (attaque.getTypeAttaque().getLabelType().equals("Feu")) {
    //             ((Feu) this.typeMonstre).bruler(cible);
    //         } else if (attaque.getTypeAttaque().getLabelType().equals("Insecte")) {
    //             ((Insecte) this.typeMonstre).empoisonner(cible);
    //         }
    // }

    public double calculeDegat(Monstre monstreAttaquant, Monstre cible) {
        double coef_aleatoire = 0.85 + (1.0 - 0.85) * Math.random();
        double degats = (20 * (monstreAttaquant.getAttaque() / cible.getDefense()) * coef_aleatoire);
        return degats;
    }
}
