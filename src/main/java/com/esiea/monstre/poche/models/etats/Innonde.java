package com.esiea.monstre.poche.models.etats;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Monstre;

public class Innonde extends StatutTerrain {
    private double probabiliteFaireChuter;
    private boolean derniereAttaqueAChute = false;
    private Monstre monstreSource; // Le monstre Eau qui a déclenché l'inondation
    
    /**
     * Constructeur avec probabilité de chute par défaut
     */
    public Innonde(int nbToursEffet) {
        this(nbToursEffet, 0.3, null);
    }
    
    /**
     * Constructeur complet avec probabilité configurable et monstre source
     */
    public Innonde(int nbToursEffet, double probabiliteFaireChuter, Monstre monstreSource) {
        this.labelStatut = "Innonde";
        this.nbToursEffet = nbToursEffet;
        this.nbToursAvecEffet = nbToursEffet;
        this.probabiliteFaireChuter = probabiliteFaireChuter;
        this.monstreSource = monstreSource;
    }
    
    public double getProbabiliteFaireChuter() {
        return this.probabiliteFaireChuter;
    }
    
    public void setProbabiliteFaireChuter(double probabiliteFaireChuter) {
        this.probabiliteFaireChuter = probabiliteFaireChuter;
    }
    
    /**
     * Retourne le monstre Eau qui a déclenché l'inondation
     */
    public Monstre getMonstreSource() {
        return this.monstreSource;
    }
    
    /**
     * Retourne true si la dernière attaque a fait chuter l'adversaire
     */
    public boolean aChute() {
        return this.derniereAttaqueAChute;
    }

    public void appliquerEffetsTerrain(Monstre cible, double degats) {
        // Décrémenter le nombre de tours restants
        this.nbToursAvecEffet--;
        
        // on soigne un monstre qui est brûlé ou empoisonné, dans le CDC
        if (cible.getStatut().getLabelStatut().equals("Brule") || cible.getStatut().getLabelStatut().equals("Empoisonne")) {
            cible.setStatut(new Normal());
            CombatLogger.logEffetTerrain(cible.getNomMonstre() + " est soigné de son statut néfaste grâce à l'eau.");
        }

        // CDC: l'adversaire a une chance de glisser, l'attaque est annulée et subit 1/4 de sa propre attaque
        if (Math.random() < this.probabiliteFaireChuter) {
            cible.setRateAttaque(true);
            double degatsChute = cible.getAttaque() * 0.25; // 1/4 de sa propre attaque
            cible.setPointsDeVie(cible.getPointsDeVie() - degatsChute);
            this.derniereAttaqueAChute = true;
            CombatLogger.logEffetTerrain(cible.getNomMonstre() + " a chuté à cause de l'eau ! Subit " + (int)degatsChute + " dégâts et rate son attaque.");
        } else {
            cible.setRateAttaque(false);
            this.derniereAttaqueAChute = false;
        }
    }
    
    /**
     * Vérifie si l'effet du terrain est terminé.
     */
    public boolean estTermine() {
        return this.nbToursAvecEffet <= 0;
    }
    
    /**
     * Décrémente le nombre de tours restants (pour les monstres Eau qui ne sont pas gênés).
     */
    public void decrementeTours() {
        this.nbToursAvecEffet--;
    }
}
