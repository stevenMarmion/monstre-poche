package com.esiea.monstre.poche.models.combats;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.loader.GameResourcesFactory;

public abstract class Combat {
    public static Joueur joueur1;
    public static Joueur joueur2;
    public static Terrain terrain;

    public Combat(Joueur joueur1, Joueur joueur2) {
        Combat.joueur1 = joueur1;
        Combat.joueur2 = joueur2;
        Combat.terrain = new Terrain("Arène de jeu", new Asseche());
    }
    
    public abstract void lancer(GameResourcesFactory resourcesFactory) ;
    public abstract void executerTour();
    public abstract void selectionnerMonstre(GameResourcesFactory resourcesFactory, Joueur joueur);
    public abstract void selectionnerAttaque(GameResourcesFactory resourcesFactory, Joueur joueur);
    public abstract void selectionnerObjet(GameResourcesFactory resourcesFactory, Joueur joueur);
    public abstract Object gereChoixAction(Joueur joueur);
    public abstract Attaque choixAttaque(Joueur joueur);
    public abstract Objet utiliseObjet(Joueur joueur);
    public abstract Monstre changeMonstre(Joueur joueur);

    public static void gereOrdreExecutionActions(Object actionJoueur1, Object actionJoueur2) {
        if (actionJoueur1 instanceof Monstre) {
            joueur1.setMonstreActuel((Monstre) actionJoueur1);
        }
        if (actionJoueur2 instanceof Monstre) {
            joueur2.setMonstreActuel((Monstre) actionJoueur2);
        }

        if (actionJoueur1 instanceof Objet) {
            ((Objet) actionJoueur1).utiliserObjet(joueur1.getMonstreActuel());
        }
        if (actionJoueur2 instanceof Objet) {
            ((Objet) actionJoueur2).utiliserObjet(joueur2.getMonstreActuel());
        }

        if (actionJoueur1 instanceof Attaque && actionJoueur2 instanceof Attaque) {
            if (joueur1.getMonstreActuel().getVitesse() > joueur2.getMonstreActuel().getVitesse()) {
                joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);
                joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            } else {
                joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);
                joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            }
        } else if (actionJoueur1 instanceof Attaque) {
            joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);
        } else if (actionJoueur2 instanceof Attaque) {
            joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);
        }
    }

    public void finDePartie() {
        if (joueur1.sontMonstresMorts()) {
            CombatLogger.log("Le joueur " + joueur2.getNomJoueur() + " a gagné la partie !");
        } else if (joueur2.sontMonstresMorts()) {
            CombatLogger.log("Le joueur " + joueur1.getNomJoueur() + " a gagné la partie !");
        }
    }

    public static Joueur getAWinner() {
        if (joueur1.sontMonstresMorts()) {
            return joueur2;
        } else if (joueur2.sontMonstresMorts()) {
            return joueur1;
        } else {
            return null;
        }
    }
}
