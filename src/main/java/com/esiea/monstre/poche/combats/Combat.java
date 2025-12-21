package com.esiea.monstre.poche.combats;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.inventaire.Objet;


import com.esiea.monstre.poche.loader.GameResourcesFactory;
import com.esiea.monstre.poche.visual.GameVisual;


public class Combat {
    public static Joueur joueur1;
    public static Joueur joueur2;

    public static Terrain terrain;
    private final Scanner scanner = new Scanner(System.in);

    public Combat(Joueur joueur1, Joueur joueur2, Terrain terrain) {
        Combat.joueur1 = joueur1;
        Combat.joueur2 = joueur2;
        Combat.terrain = terrain;
    }
    
    public void lancer(GameResourcesFactory resourcesFactory) {
        this.selectionnerMonstre(resourcesFactory, joueur1);
        this.selectionnerMonstre(resourcesFactory, joueur2);

        this.selectionnerAttaque(resourcesFactory, joueur1);
        this.selectionnerAttaque(resourcesFactory, joueur2);

        this.selectionnerObjet(resourcesFactory, joueur1);
        this.selectionnerObjet(resourcesFactory, joueur2);

        this.executerTour();
    }

    public void executerTour() {
        while ((joueur1.sontMonstresMorts() || joueur2.sontMonstresMorts()) == false) {
            Object actionJoueur1 = this.gereChoixAction(joueur1);
            Object actionJoueur2 = this.gereChoixAction(joueur2);

            Combat.gereOrdreExecutionActions(actionJoueur1, actionJoueur2);
        }
        this.finDePartie();
    }

    public void selectionnerMonstre(GameResourcesFactory resourceFactory, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des monstres - " + joueur.getNomJoueur());
        GameVisual.afficherSousTitre("Monstres disponibles");

        int index = 1;
        List<Monstre> monstres = resourceFactory.getTousLesMonstres();
        int arraySize = monstres.size();
        for (Monstre monstre : monstres) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < Joueur.TAILLE_EQUIPE_MAX) {
            String choixInput = GameVisual.demanderSaisie(this.scanner, "Choix " + (joueur.getMonstres().size() + 1) + "/"+Joueur.TAILLE_EQUIPE_MAX+"> ");
            
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                
                if (indexChoisi < 1 || indexChoisi > resourceFactory.getTousLesMonstres().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + arraySize);
                    continue;
                }
                
                Monstre monstreCharge = resourceFactory.getTousLesMonstres().get(indexChoisi - 1);
                
                if (joueur.getMonstres().contains(monstreCharge)) {
                    GameVisual.afficherErreur("Ce monstre a deja ete selectionne.");
                    continue;
                }

                joueur.ajouterMonstre(monstreCharge);
                System.out.println("  [OK] Monstre ajoute : " + monstreCharge.getNomMonstre());
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        joueur.setMonstreActuel(joueur.getMonstres().get(0));
        System.out.println("Monstre actif initial : " + joueur.getMonstreActuel().getNomMonstre());
    }

    public void selectionnerAttaque(GameResourcesFactory resourceFactory, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des attaques - " + joueur.getNomJoueur());

        List<Attaque> attaquesADisposition = resourceFactory.getToutesLesAttaques();
        for (Monstre monstre : joueur.getMonstres()) {
            GameVisual.afficherSousTitre("Monstre : " + monstre.getNomMonstre());
            
            // Créer une liste des attaques compatibles
            List<Attaque> attaquesCompatibles = new ArrayList<>();
            for (Attaque attaque : attaquesADisposition) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    attaquesCompatibles.add(attaque);
                }
            }
            
            // Afficher avec index
            int index = 1;
            for (Attaque attaque : attaquesCompatibles) {
                System.out.printf("[%d] %s%n", index++, GameVisual.formatterAttaque(attaque));
            }

            while (monstre.getAttaques().size() < Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE) {
                String choixInput = GameVisual.demanderSaisie(this.scanner, "Choix " + (monstre.getAttaques().size() + 1) + "/"+Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE+"> ");
                
                try {
                    int indexChoisi = Integer.parseInt(choixInput);
                    
                    if (indexChoisi < 1 || indexChoisi > attaquesCompatibles.size()) {
                        GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + attaquesCompatibles.size());
                        continue;
                    }
                    
                    Attaque attaqueChargee = attaquesCompatibles.get(indexChoisi - 1);

                    if (monstre.getAttaques().contains(attaqueChargee)) {
                        GameVisual.afficherErreur("Attaque deja selectionnee pour ce monstre.");
                        continue;
                    }

                    monstre.ajouterAttaque(attaqueChargee);
                    System.out.println("  [OK] Attaque ajoutee pour " + joueur.getNomJoueur() + " : " + attaqueChargee.getNomAttaque() + " (" + monstre.getNomMonstre() + ")");
                } catch (NumberFormatException e) {
                    GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
                }
            }
        }
    }


    public void selectionnerObjet(GameResourcesFactory resourceFactory, Joueur joueur) {
        System.out.printf(
                "Merci de choisir %d objets parmi la liste suivante :%n",
                Joueur.TAILLE_INVENTAIRE_MAX
        );
        List<Objet> objetsDisponibles = resourceFactory.getTousLesObjets();
        int index = 1;
        for (Objet objet : objetsDisponibles) {
            System.out.printf("[%d] %s%n", index, objet.getNomObjet());
            index++;
        }
        System.out.printf("Choisissez un objet (%d/%d) : ", joueur.getObjets().size(), Joueur.TAILLE_INVENTAIRE_MAX);
        while (joueur.getObjets().size() < Joueur.TAILLE_INVENTAIRE_MAX) {
            try {
                String choixInput = GameVisual.demanderSaisie(this.scanner, "Choix " + (joueur.getObjets().size() + 1) + "/"+Joueur.TAILLE_INVENTAIRE_MAX+"> ");
                int indexChoisi = Integer.parseInt(choixInput);

                if (indexChoisi < 1 || indexChoisi > objetsDisponibles.size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + objetsDisponibles.size());
                    continue;
                }

                Objet objetChoisi = objetsDisponibles.get(indexChoisi - 1);

                // si l'objet est déjà présent dans la liste des objets choisis, on en crée une copie qu'on ajoute
                // car un joueur peut avoir 5x le même objet dans son inventaire ( mais en réalité c'est 5 instances différentes )
                //TODO Vérifier ca !
                /**if (joueur.getObjets().contains(objetChoisi)){
                    joueur.getObjets().add(new O)
                }
                else {
                 joueur.getObjets().add(objetChoisi);
                 }*/

                joueur.getObjets().add(objetChoisi);
                System.out.println("  [OK] Objet ajouté pour " + joueur.getNomJoueur() + " : " + objetChoisi.getNomObjet());
               } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        System.out.println("Sélection des objets terminée !");
    }



    public Object gereChoixAction(Joueur joueur) {
        GameVisual.afficherTitreSection("Tour de " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        System.out.println("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());
        System.out.println("Actions disponibles :");
        System.out.println("  1) Attaquer");
        System.out.println("  2) Utiliser un objet");
        System.out.println("  3) Changer de monstre");

        String choixAction = GameVisual.demanderSaisie(this.scanner, "Votre choix >");
        while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
            GameVisual.afficherErreur("Saisie invalide. Merci de choisir 1, 2 ou 3.");
            choixAction = GameVisual.demanderSaisie(this.scanner, "Votre choix >");
        }

        Object actionEffectuee = null;
        switch (choixAction) {
            case "1":
                Attaque attaqueChoisie = this.choixAttaque(joueur);
                actionEffectuee = attaqueChoisie;
                // joueur.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, attaqueChoisie);
                break;
            case "2":
                Objet objetChoisi = this.utiliseObjet(joueur);
                actionEffectuee = objetChoisi;
                // objetChoisi.utiliserObjet(joueur.getMonstreActuel());

                System.out.println("DEBUG");
                System.out.println(joueur.getObjets());
                System.out.println("tentative de suppression de 1 objet");
                System.out.println(joueur.getObjets().remove(1));
                System.out.println(joueur.getObjets());
                break;
            case "3":
                Monstre monstreChoisi = this.changeMonstre(joueur);
                actionEffectuee = monstreChoisi;
                // joueur.setMonstreActuel(monstreChoisi);
                break;
            default: // normalement c'est impossible qu'on arrive ici
                break;
        }

        return actionEffectuee;
    }

    public Attaque choixAttaque(Joueur joueur) {
        Monstre monstreActuel = joueur.getMonstreActuel();
        GameVisual.afficherTitreSection("Attaques de " + monstreActuel.getNomMonstre());
        int index = 1;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
        }

        Attaque attaqueChoisie = null;
        while (attaqueChoisie == null) {
            String choixInput = GameVisual.demanderSaisie(this.scanner, "Attaque choisie >");
            
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                
                if (indexChoisi < 1 || indexChoisi > monstreActuel.getAttaques().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstreActuel.getAttaques().size());
                    continue;
                }
                
                attaqueChoisie = monstreActuel.getAttaques().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        return attaqueChoisie;
    }

    public Objet utiliseObjet(Joueur joueur) {
        GameVisual.afficherTitreSection("Objets de " + joueur.getNomJoueur());
        List<Objet> objets = joueur.getObjets();
        if (objets.isEmpty()) {
            GameVisual.afficherErreur("Aucun objet disponible.");
            return null;
        }
        for (int i = 0; i < objets.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, objets.get(i).getNomObjet());
        }
        while (true) {
            String saisie = GameVisual.demanderSaisie(scanner, "Objet choisi >");
            int indexChoisi;
            try {
                indexChoisi = Integer.parseInt(saisie);
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Veuillez entrer un nombre valide.");
                continue;
            }

            if (indexChoisi < 1 || indexChoisi > objets.size()) {
                GameVisual.afficherErreur(
                        "Index invalide. Choisissez un nombre entre 1 et " + objets.size()
                );
                continue;
            }

            return objets.get(indexChoisi - 1);
        }
    }


    public Monstre changeMonstre(Joueur joueur) {
        GameVisual.afficherTitreSection("Changement de monstre - " + joueur.getNomJoueur());
        int index = 1;
        for (Monstre monstre : joueur.getMonstres()) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        Monstre monstreChoisi = null;
        while (monstreChoisi == null) {
            String choixInput = GameVisual.demanderSaisie(this.scanner, "Monstre envoye >");
            
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                
                if (indexChoisi < 1 || indexChoisi > joueur.getMonstres().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + joueur.getMonstres().size());
                    continue;
                }
                
                monstreChoisi = joueur.getMonstres().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        return monstreChoisi;
    }

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
            System.out.println("Le joueur " + joueur2.getNomJoueur() + " a gagné la partie !");
        } else if (joueur2.sontMonstresMorts()) {
            System.out.println("Le joueur " + joueur1.getNomJoueur() + " a gagné la partie !");
        }
    }
}
