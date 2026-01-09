package com.esiea.monstre.poche.models.status.terrain;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.status.monster.Normal;

public class Innonde extends StatutTerrain {
    private double probabiliteFaireChuter;
    private Monstre monstreSource;
    
    public Innonde(int nbToursEffet, double probabiliteFaireChuter, Monstre monstreSource) {
        this.labelStatut = "Innonde";
        this.nbToursEffet = nbToursEffet;
        this.nbToursAvecEffet = nbToursEffet;
        this.probabiliteFaireChuter = probabiliteFaireChuter;
        this.monstreSource = monstreSource;
    }
    
    public void setProbabiliteFaireChuter(double probabiliteFaireChuter) {
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }
    
    public Monstre getMonstreSource() {
        return this.monstreSource;
    }
    
    public boolean estTermine() {
        return this.nbToursAvecEffet <= 0;
    }

    public void appliquerEffetsTerrain(Monstre cible, Terrain terrain, double degats) {
        if (this.estTermine()) {
            terrain.setStatutTerrain(new Asseche());
            CombatLogger.info("Le terrain n'est plus inondé et revient dans un état normal");
            return;
        }

        if (!cible.getTypeMonstre().getLabelType().equals("Eau")) {
            if (cible.getStatut().getLabelStatut().equals("Brule") || cible.getStatut().getLabelStatut().equals("Empoisonne")) {
                cible.setStatut(new Normal());
                CombatLogger.info(cible.getNomMonstre() + " est soigné de son statut néfaste grâce au terrain innondé");
            }

            if (Math.random() < this.probabiliteFaireChuter) {
                cible.setRateAttaque(true);
                double degatsChute = cible.getAttaque() * 0.25;
                cible.setPointsDeVie(cible.getPointsDeVie() - degatsChute);
                CombatLogger.info(cible.getNomMonstre() + " a chuté à cause de l'eau ! Subit " + (int)degatsChute + " dégâts de sa propre attaque et rate son attaque.");
            } else {
                cible.setRateAttaque(false);
            }
        } else {
            CombatLogger.info(cible.getNomMonstre() + " est de type Eau et n'est pas gêné par l'inondation");
        }

        this.decrementerNbToursAvecEffet();
    }

    public void retraitInnondation(Terrain terrain) {
        terrain.setStatutTerrain(new Asseche());
        CombatLogger.info("Le terrain n'est plus inondé et revient dans un état normal");
    }
}
