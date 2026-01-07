package com.esiea.monstre.poche.models.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.status.monster.Normal;
import com.esiea.monstre.poche.models.status.monster.StatutMonstre;
import com.esiea.monstre.poche.models.status.utils.StatutMonstreUtils;
import com.esiea.monstre.poche.models.status.utils.StatutTerrainUtils;
import com.esiea.monstre.poche.models.types.Type;
import com.esiea.monstre.poche.models.types.utils.TypeUtils;

public class Monstre implements Serializable {
    protected static final long serialVersionUID = 1L;

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

    public Monstre(String nomMonstre, double pointsDeVie, int attaque, int defense, int vitesse, ArrayList<Attaque> attaques, Type typeMonstre) {
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
        } else {
            CombatLogger.log("[INFO] Attaque non ajoutee : " + attaque.getNomAttaque() + " deja presente ou limite atteinte pour " + this.nomMonstre + ".");
        }
    }

    public void attaquer(Monstre cible, Terrain terrain, Attaque attaqueUtilisee) {
        if (attaqueUtilisee != null) {
            if (attaqueUtilisee.getNbUtilisations() <= 0) {
                CombatLogger.log(this.nomMonstre + " ne peut pas utiliser " + attaqueUtilisee.getNomAttaque() + " : plus de PP !");
                return;
            } else {
                attaqueUtilisee.setNbUtilisations(attaqueUtilisee.getNbUtilisations() - 1);
            }
        }
        
        // Vérifier si l'attaque échoue (probabilité d'échec)
        if (attaqueUtilisee != null && Math.random() < attaqueUtilisee.getProbabiliteEchec()) {
            CombatLogger.log("=======================================");
            CombatLogger.log(this.nomMonstre + " utilise " + attaqueUtilisee.getNomAttaque() + "...");
            CombatLogger.log("  --> L'attaque échoue ! (probabilité d'échec: " + (int)(attaqueUtilisee.getProbabiliteEchec() * 100) + "%)");
            CombatLogger.log("=======================================");
            return;
        }
        
        // on commence par calculer les dégâts, c'est la base de tout
        double degatsAffliges;
        if (attaqueUtilisee == null || !this.attaques.contains(attaqueUtilisee)) {
            degatsAffliges = calculeDegat(this, cible);
        } else {
            degatsAffliges = attaqueUtilisee.calculeDegatsAttaque(this, cible);
        }

        // Log avant attaque avec PP restants
        if (attaqueUtilisee == null) {
            CombatLogger.log("=======================================");
            CombatLogger.log(this.nomMonstre + " attaque avec ses propres forces (sans attaque spécifique) :");
        } else {
            CombatLogger.log("=======================================");
            CombatLogger.log(this.nomMonstre + " utilise " + attaqueUtilisee.getNomAttaque() + " (PP restants: " + attaqueUtilisee.getNbUtilisations() + ") :");
            CombatLogger.log("  [STATUT] " + this.nomMonstre + " est " + this.statut.getLabelStatut());
        }
        
        // ensuite on applique nos effets avant attaque si le pokémon est paralysé ou autre
        StatutMonstreUtils.appliquerStatutMonstre(statut, this, (int) degatsAffliges);
        StatutTerrainUtils.appliquerStatutTerrain(terrain, this, (int) degatsAffliges);

        TypeUtils.appliqueCapaciteSpecialeNature(typeMonstre, this, terrain);

        // notre attaque principal, le process principal
        if (!this.isRateAttaque()) {
            double pvAvant = cible.getPointsDeVie();
            cible.setPointsDeVie(cible.getPointsDeVie() - (int) degatsAffliges);
            
            CombatLogger.log(cible.getNomMonstre() + " subit " + (int) degatsAffliges + " points de dégâts.");
            CombatLogger.log("PV: " + (int)pvAvant + " --> " + (int)cible.getPointsDeVie() + " / " + (int)cible.getPointsDeVieMax());

            if (cible.getPointsDeVie() == 0) {
                CombatLogger.log(cible.getNomMonstre() + " est K.O.");
            }

            // les effets de l'attaque spéciale du monstre si elle est pas ratée
            // ET si c'est une vraie attaque (pas mains nues)
            if (attaqueUtilisee != null) {
                TypeUtils.appliqueCapaciteSpeciale(typeMonstre, this, cible, terrain);
            }
        } else {
            CombatLogger.log(this.nomMonstre + " a raté son attaque.");
        }
        CombatLogger.log("=======================================");
    }

    public double calculeDegat(Monstre monstreAttaquant, Monstre cible) {
        double coef_aleatoire = 0.85 + (1.0 - 0.85) * Math.random();
        double degats = (20 * (monstreAttaquant.getAttaque() / cible.getDefense()) * coef_aleatoire);
        return degats;
    }

    public Monstre copyOf() {
        ArrayList<Attaque> copiesAttaques = this.attaques.stream()
            .map(Attaque::copyOf)
            .collect(Collectors.toCollection(ArrayList::new)
        );

        return new Monstre(
            this.getNomMonstre(),
            this.getPointsDeVieMax(),
            this.getAttaque(),
            this.getDefense(),
            this.getVitesse(),
            copiesAttaques,
            this.getTypeMonstre()
        );
    }

    @Override
    public String toString() {
        return "Monstre (" +
            "nomMonstre='" + nomMonstre + '\'' +
            ", pointsDeVie=" + pointsDeVie +
            ", attaque=" + attaque +
            ", defense=" + defense +
            ", vitesse=" + vitesse +
            ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Monstre monstre = (Monstre) obj;
        return nomMonstre.equals(monstre.nomMonstre) && pointsDeVie == monstre.pointsDeVie && attaque == monstre.attaque && defense == monstre.defense && vitesse == monstre.vitesse;
    }
}
