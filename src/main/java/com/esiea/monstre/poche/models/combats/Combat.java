package com.esiea.monstre.poche.models.combats;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
import com.esiea.monstre.poche.models.entites.Joueur;

public abstract class Combat {
    public static Joueur joueur1;
    public static Joueur joueur2;
    public static Terrain terrain;

    public Combat(Joueur joueur1, Joueur joueur2) {
        Combat.joueur1 = joueur1;
        Combat.joueur2 = joueur2;
        Combat.terrain = new Terrain("Arène de jeu", new Asseche());
    }
    
    public abstract void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader /*, PotionLoader potionLoader, MedicamentLoader medicamentLoader*/) ;
    public abstract void executerTour();
    public abstract void selectionnerMonstre(MonstreLoader monstreLoader, Joueur joueur);
    public abstract void selectionnerAttaque(AttaqueLoader attaqueLoader, Joueur joueur);
    public abstract Object gereChoixAction(Joueur joueur);
    public abstract Attaque choixAttaque(Joueur joueur);
    public abstract Objet utiliseObjet(Joueur joueur);
    public abstract Monstre changeMonstre(Joueur joueur);

    /**
    public void selectionnerObjet(PotionLoader potionLoader, MedicamentLoader medicamentLoader, Joueur joueur) {
        System.out.println("Merci de choisir 5 objets :");
        for (Potion potion : potionLoader.getRessources()) {
            System.out.println("Nom de la potion : " + potion.getNomObjet());
        }
        for (Medicament medicament : medicamentLoader.getRessources()) {
            System.out.println("Nom du médicament : " + medicament.getNomObjet());
        }

        while (joueur.getObjets().size() <= 5) {
            Scanner scanner = new Scanner(System.in);
            String objetChoisi = scanner.next();
            scanner.close();
            // on va faire ça en attendant de trouver une meilleure solution
            Potion potionTrouve = potionLoader.getRessourceParNom(objetChoisi);
            if (potionTrouve != null) {
                joueur.ajouterObjet(potionTrouve);
            }
            Medicament medicamentTrouve = medicamentLoader.getRessourceParNom(objetChoisi);
            if (medicamentTrouve != null) {
                joueur.ajouterObjet(medicamentTrouve);
            }
        }
    }*/

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
