package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Innonde extends StatutTerrain {
    private double probabiliteFaireChuter;
    private boolean derniereAttaqueAChute = false;
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

    public void appliquerEffetsTerrain(Monstre cible, double degats) {
        this.decrementerNbToursAvecEffet();
        
        if (cible.getStatut().getLabelStatut().equals("Brule") || cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            cible.setStatut(new Normal());
            CombatLogger.log(cible.getNomMonstre() + " est soigné de son statut néfaste grâce à l'eau.");
        }

        if (Math.random() < this.probabiliteFaireChuter) {
            cible.setRateAttaque(true);
            double degatsChute = cible.getAttaque() * 0.25;
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsChute);
            this.derniereAttaqueAChute = true;
            CombatLogger.log(cible.getNomMonstre() + " a chuté à cause de l'eau ! Subit " + (int)degatsChute + " dégâts et rate son attaque.");
        } else {
            cible.setRateAttaque(false);
            this.derniereAttaqueAChute = false;
        }
    }
}
