package com.esiea.monstre.poche.models.battle;

import java.util.ArrayList;
import java.util.List;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.core.Terrain;
import com.esiea.monstre.poche.models.game.GameVisual;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.items.Objet;
import com.esiea.monstre.poche.models.status.monster.Asseche;
import com.esiea.monstre.poche.models.status.terrain.Innonde;

public abstract class Combat {
    public Joueur joueur1;
    public Joueur joueur2;
    public Terrain terrain;

    public Combat(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.terrain = new Terrain("Arène de jeu", new Asseche());
    }

    // ========================================
    // Méthodes abstraites à implémenter par les sous-classes
    // ========================================
    protected abstract void lancer();
    protected abstract void executerTour();
    protected abstract void afficherMessage(String message);
    protected abstract void afficherTitre(String titre);
    protected abstract void afficherSousTitre(String sousTitre);
    protected abstract void afficherErreur(String erreur);
    protected abstract String demanderSaisie(String prompt);
    protected abstract Object gereChoixAction(Joueur joueur);

    // ========================================
    // Méthodes concrètes communes (Template Methods)
    // ========================================

    /**
     * Sélection des monstres pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public void selectionnerMonstre(Joueur joueur) {
        afficherTitre("Selection des monstres - " + joueur.getNomJoueur());
        afficherSousTitre("Monstres disponibles");

        int index = 1;
        List<Monstre> monstresDisponibles = GameResourcesFactory.getInstance().getTousLesMonstres();
        for (Monstre monstre : monstresDisponibles) {
            afficherMessage(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < Joueur.TAILLE_EQUIPE_MAX) {
            String choixInput = demanderSaisie("Choix " + (joueur.getMonstres().size() + 1) + "/" + Joueur.TAILLE_EQUIPE_MAX + "> ");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > monstresDisponibles.size()) {
                    afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstresDisponibles.size());
                    continue;
                }
                Monstre monstreCharge = monstresDisponibles.get(indexChoisi - 1);
                if (joueur.getMonstres().contains(monstreCharge)) {
                    afficherErreur("Ce monstre a deja ete selectionne.");
                    continue;
                }
                joueur.ajouterMonstre(monstreCharge);
                afficherMessage("  [OK] Monstre ajoute : " + monstreCharge.getNomMonstre());
            } catch (NumberFormatException e) {
                afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        joueur.setMonstreActuel(joueur.getMonstres().get(0));
        afficherMessage("Monstre actif initial : " + joueur.getMonstreActuel().getNomMonstre());
    }

    /**
     * Sélection des attaques pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public void selectionnerAttaque(Joueur joueur) {
        afficherTitre("Selection des attaques - " + joueur.getNomJoueur());
        afficherSousTitre("Attaques disponibles");

        List<Attaque> attaquesADisposition = GameResourcesFactory.getInstance().getToutesLesAttaques();
        for (Monstre monstre : joueur.getMonstres()) {
            afficherSousTitre("Monstre : " + monstre.getNomMonstre());

            // Filtrer les attaques compatibles avec le type du monstre
            List<Attaque> attaquesCompatibles = new ArrayList<>();
            for (Attaque attaque : attaquesADisposition) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    attaquesCompatibles.add(attaque);
                }
            }

            int index = 1;
            for (Attaque attaque : attaquesCompatibles) {
                afficherMessage(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
            }

            while (monstre.getAttaques().size() < Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE) {
                String choixInput = demanderSaisie("Choix " + (monstre.getAttaques().size() + 1) + "/" + Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE + "> ");
                try {
                    int indexChoisi = Integer.parseInt(choixInput);
                    if (indexChoisi < 1 || indexChoisi > attaquesCompatibles.size()) {
                        afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + attaquesCompatibles.size());
                        continue;
                    }
                    Attaque attaqueChargee = attaquesCompatibles.get(indexChoisi - 1);
                    if (monstre.getAttaques().contains(attaqueChargee)) {
                        afficherErreur("Attaque deja selectionnee pour ce monstre.");
                        continue;
                    }
                    monstre.ajouterAttaque(attaqueChargee);
                    afficherMessage("  [OK] Attaque ajoutee : " + attaqueChargee.getNomAttaque());
                } catch (NumberFormatException e) {
                    afficherErreur("Saisie invalide. Veuillez entrer un numero.");
                }
            }
        }
    }

    /**
     * Sélection des objets pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public void selectionnerObjet(Joueur joueur) {
        afficherTitre("Selection des objets - " + joueur.getNomJoueur());
        afficherSousTitre("Objets disponibles");

        List<Objet> objetsDisponibles = GameResourcesFactory.getInstance().getTousLesObjets();
        int index = 1;
        for (Objet objet : objetsDisponibles) {
            afficherMessage(String.format("[%d] %s", index++, objet.getNomObjet()));
        }

        while (joueur.getObjets().size() < Joueur.TAILLE_INVENTAIRE_MAX) {
            String choixInput = demanderSaisie("Choix " + (joueur.getObjets().size() + 1) + "/" + Joueur.TAILLE_INVENTAIRE_MAX + "> ");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > objetsDisponibles.size()) {
                    afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + objetsDisponibles.size());
                    continue;
                }
                Objet objetChoisi = objetsDisponibles.get(indexChoisi - 1);
                joueur.ajouterObjet(objetChoisi.copyOf());
                afficherMessage("  [OK] Objet ajoute pour " + joueur.getNomJoueur() + " : " + objetChoisi.getNomObjet());
            } catch (NumberFormatException e) {
                afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        afficherMessage("Selection des objets terminee !");
    }

    /**
     * Choix d'une attaque pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public Attaque choixAttaque(Joueur joueur) {
        Monstre monstreActuel = joueur.getMonstreActuel();
        afficherTitre("Attaques de " + monstreActuel.getNomMonstre());

        int index = 1;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            String ppStatus = attaque.getNbUtilisations() <= 0 ? " [VIDE]" : "";
            afficherMessage(String.format("[%d] %s%s", index++, GameVisual.formatterAttaque(attaque), ppStatus));
        }
        afficherMessage(String.format("[%d] %s | PP: illimite | Puissance: faible", index, "MAINS NUES"));

        while (true) {
            String choixInput = demanderSaisie("Attaque choisie (0 pour mains nues) >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi == 0) {
                    afficherMessage("  [OK] Attaque a mains nues selectionnee.");
                    return null;
                }
                if (indexChoisi < 1 || indexChoisi > monstreActuel.getAttaques().size()) {
                    afficherErreur("Index invalide. Veuillez choisir 0 ou un nombre entre 1 et " + monstreActuel.getAttaques().size());
                    continue;
                }
                Attaque attaqueTemp = monstreActuel.getAttaques().get(indexChoisi - 1);
                if (attaqueTemp.getNbUtilisations() <= 0) {
                    afficherErreur("Cette attaque n'a plus de PP ! Choisissez une autre attaque ou 0 pour mains nues.");
                    continue;
                }
                return attaqueTemp;
            } catch (NumberFormatException e) {
                afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
    }

    /**
     * Utilisation d'un objet pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public Objet utiliseObjet(Joueur joueur) {
        afficherTitre("Objets de " + joueur.getNomJoueur());

        List<Objet> objets = joueur.getObjets();
        if (objets.isEmpty()) {
            afficherErreur("Aucun objet disponible.");
            return null;
        }

        int index = 1;
        for (Objet objet : objets) {
            afficherMessage(String.format("[%d] %s", index++, objet.getNomObjet()));
        }

        while (true) {
            String saisie = demanderSaisie("Objet choisi >");
            try {
                int indexChoisi = Integer.parseInt(saisie);
                if (indexChoisi < 1 || indexChoisi > objets.size()) {
                    afficherErreur("Index invalide. Choisissez un nombre entre 1 et " + objets.size());
                    continue;
                }
                return objets.get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez entrer un nombre valide.");
            }
        }
    }

    /**
     * Changement de monstre pour un joueur
     * Méthode template : la logique est commune, seul l'I/O change
     */
    public Monstre changeMonstre(Joueur joueur) {
        afficherTitre("Changement de monstre - " + joueur.getNomJoueur());

        int index = 1;
        for (Monstre monstre : joueur.getMonstres()) {
            afficherMessage(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (true) {
            String choixInput = demanderSaisie("Monstre envoye >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > joueur.getMonstres().size()) {
                    afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + joueur.getMonstres().size());
                    continue;
                }
                return joueur.getMonstres().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
    }

    // ========================================
    // Logique métier du combat (inchangée)
    // ========================================

    /**
     * Vérifie si l'inondation doit être retirée quand un monstre quitte le terrain.
     * CDC: L'inondation est automatiquement retirée lorsque le monstre l'ayant déclenché quitte le terrain.
     */
    protected void verifierRetraitInondation(Monstre ancienMonstre) {
        if (terrain.getStatutTerrain() instanceof Innonde) {
            Innonde innonde = (Innonde) terrain.getStatutTerrain();
            if (innonde.getMonstreSource() != null && innonde.getMonstreSource().equals(ancienMonstre)) {
                terrain.setStatutTerrain(new Asseche());
                CombatLogger.log("L'inondation se retire car " + ancienMonstre.getNomMonstre() + " quitte le terrain.");
            }
        }
    }

    public void gereOrdreExecutionActions(Object actionJoueur1, Object actionJoueur2) {
        if (actionJoueur1 instanceof Monstre) {
            Monstre ancienMonstre = joueur1.getMonstreActuel();
            if (ancienMonstre != null) {
                verifierRetraitInondation(ancienMonstre);
            }
            joueur1.setMonstreActuel((Monstre) actionJoueur1);
        }
        if (actionJoueur2 instanceof Monstre) {
            Monstre ancienMonstre = joueur2.getMonstreActuel();
            if (ancienMonstre != null) {
                verifierRetraitInondation(ancienMonstre);
            }
            joueur2.setMonstreActuel((Monstre) actionJoueur2);
        }

        if (actionJoueur1 instanceof Objet) {
            ((Objet) actionJoueur1).utiliserObjet(joueur1.getMonstreActuel());
        }
        if (actionJoueur2 instanceof Objet) {
            ((Objet) actionJoueur2).utiliserObjet(joueur2.getMonstreActuel());
        }

        // Vérifie si c'est une attaque (Attaque ou null pour mains nues)
        boolean j1Attaque = actionJoueur1 instanceof Attaque || actionJoueur1 == null;
        boolean j2Attaque = actionJoueur2 instanceof Attaque || actionJoueur2 == null;

        // Exclure le cas où l'action est un Monstre ou Objet (déjà traité)
        if (actionJoueur1 instanceof Monstre || actionJoueur1 instanceof Objet) j1Attaque = false;
        if (actionJoueur2 instanceof Monstre || actionJoueur2 instanceof Objet) j2Attaque = false;

        if (j1Attaque==true && j2Attaque==true) {
            Attaque attaque1 = actionJoueur1 instanceof Attaque ? (Attaque) actionJoueur1 : null;
            Attaque attaque2 = actionJoueur2 instanceof Attaque ? (Attaque) actionJoueur2 : null;

            if (joueur1.getMonstreActuel().getVitesse() > joueur2.getMonstreActuel().getVitesse()) {
                joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, attaque1);
                joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, attaque2);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            } else {
                joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, attaque2);
                joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, attaque1);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            }
        } else if (j1Attaque) {
            Attaque attaque1 = actionJoueur1 instanceof Attaque ? (Attaque) actionJoueur1 : null;
            joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, attaque1);
        } else if (j2Attaque) {
            Attaque attaque2 = actionJoueur2 instanceof Attaque ? (Attaque) actionJoueur2 : null;
            joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, attaque2);
        }
    }

    public void finDePartie() {
        if (joueur1.sontMonstresMorts()) {
            CombatLogger.log("Le joueur " + joueur2.getNomJoueur() + " a gagné la partie !");
        } else if (joueur2.sontMonstresMorts()) {
            CombatLogger.log("Le joueur " + joueur1.getNomJoueur() + " a gagné la partie !");
        }
    }

    public Joueur getAWinner() {
        if (joueur1.sontMonstresMorts()) {
            return joueur2;
        } else if (joueur2.sontMonstresMorts()) {
            return joueur1;
        } else {
            return null;
        }
    }
}
