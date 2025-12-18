package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.inventaire.Objet;
// import com.esiea.monstre.poche.inventaire.medicaments.Medicament;
// import com.esiea.monstre.poche.inventaire.potions.Potion;
import com.esiea.monstre.poche.loader.AttaqueLoader;
// import com.esiea.monstre.poche.loader.MedicamentLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;
import com.esiea.monstre.poche.visual.GameVisual;

// import com.esiea.monstre.poche.loader.PotionLoader;

public class Combat {
    private Joueur joueur1;
    private Joueur joueur2;

    private Terrain terrain;
    private final Scanner scanner = new Scanner(System.in);

    public Combat(Joueur joueur1, Joueur joueur2, Terrain terrain) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.terrain = terrain;
    }
    
    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader /*, PotionLoader potionLoader, MedicamentLoader medicamentLoader*/) {
        this.selectionnerMonstre(monstreLoader, joueur1);
        this.selectionnerMonstre(monstreLoader, joueur2);

        this.selectionnerAttaque(attaqueLoader, joueur1);
        this.selectionnerAttaque(attaqueLoader, joueur2);

        /*this.selectionnerObjet(potionLoader, medicamentLoader, joueur1);
        this.selectionnerObjet(potionLoader, medicamentLoader, joueur2);*/

        this.executerTour();
    }

    public void executerTour() {
        while ((joueur1.sontMonstresMorts() || joueur2.sontMonstresMorts()) == false) {
            Object actionJoueur1 = this.gereChoixAction(joueur1);
            Object actionJoueur2 = this.gereChoixAction(joueur2);

            this.gereOrdreExecutionActions(actionJoueur1, actionJoueur2);
        }
        this.finDePartie();
    }

    public void selectionnerMonstre(MonstreLoader monstreLoader, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des monstres - " + joueur.getNomJoueur());
        GameVisual.afficherSousTitre("Monstres disponibles");

        int index = 1;
        for (Monstre monstre : monstreLoader.getRessources()) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < 3) {
            String monstreChoisi = GameVisual.demanderSaisie(this.scanner, "Choix " + (joueur.getMonstres().size() + 1) + "/3 >");
            Monstre monstreCharge = monstreLoader.getRessourceParNom(monstreChoisi);

            if (monstreCharge == null) {
                GameVisual.afficherErreur("Monstre introuvable. Merci de saisir un nom valide.");
                continue;
            }
            if (joueur.getMonstres().contains(monstreCharge)) {
                GameVisual.afficherErreur("Ce monstre a deja ete selectionne.");
                continue;
            }

            joueur.ajouterMonstre(monstreCharge);
            System.out.println("  [OK] Monstre ajoute : " + monstreCharge.getNomMonstre());
        }
        joueur.setMonstreActuel(joueur.getMonstres().get(0));
        System.out.println("Monstre actif initial : " + joueur.getMonstreActuel().getNomMonstre());
    }

    public void selectionnerAttaque(AttaqueLoader attaqueLoader, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des attaques - " + joueur.getNomJoueur());
        for (Monstre monstre : joueur.getMonstres()) {
            GameVisual.afficherSousTitre("Monstre : " + monstre.getNomMonstre());
            int index = 1;
            for (Attaque attaque : attaqueLoader.getRessources()) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    System.out.println(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
                }
            }

            while (monstre.getAttaques().size() < 4) {
                String nomAttaque = GameVisual.demanderSaisie(this.scanner, "Choix " + (monstre.getAttaques().size() + 1) + "/4 >");
                Attaque attaqueChargee = attaqueLoader.getRessourceParNom(nomAttaque);

                if (attaqueChargee == null || !monstre.getTypeMonstre().getLabelType().equals(attaqueChargee.getTypeAttaque().getLabelType())) {
                    GameVisual.afficherErreur("Attaque introuvable ou non compatible avec le type du monstre.");
                    continue;
                }

                if (monstre.getAttaques().contains(attaqueChargee)) {
                    GameVisual.afficherErreur("Attaque deja selectionnee pour ce monstre.");
                    continue;
                }

                monstre.ajouterAttaque(attaqueChargee);
                System.out.println("  [OK] Attaque ajoutee pour " + joueur.getNomJoueur() + " : " + attaqueChargee.getNomAttaque() + " (" + monstre.getNomMonstre() + ")");
            }
        }
    }

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

        String nomAttaqueChoisie = GameVisual.demanderSaisie(this.scanner, "Attaque choisie >");
        Attaque attaqueChoisie = null;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            if (attaque.getNomAttaque().equalsIgnoreCase(nomAttaqueChoisie)) {
                attaqueChoisie = attaque;
            }
        }

        while (attaqueChoisie == null) {
            GameVisual.afficherErreur("Attaque invalide. Merci de saisir l'intitule exact d'une attaque disponible.");
            nomAttaqueChoisie = GameVisual.demanderSaisie(this.scanner, "Attaque choisie >");
            for (Attaque attaque : monstreActuel.getAttaques()) {
                if (attaque.getNomAttaque().equalsIgnoreCase(nomAttaqueChoisie)) {
                    attaqueChoisie = attaque;
                }
            }
        }
        return attaqueChoisie;
    }

    public Objet utiliseObjet(Joueur joueur) {
        GameVisual.afficherTitreSection("Objets de " + joueur.getNomJoueur());
        int index = 1;
        for (Objet objet : joueur.getObjets()) {
            System.out.println(String.format("[%d] %s", index++, objet.getNomObjet()));
        }

        String nomObjetChoisi = GameVisual.demanderSaisie(this.scanner, "Objet choisi >");
        Objet objetChoisi = null;
        for (Objet objet : joueur.getObjets()) {
            if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                objetChoisi = objet;
            }
        }

        while (objetChoisi == null) {
            GameVisual.afficherErreur("Objet introuvable. Merci de saisir le nom exact d'un objet disponible.");
            nomObjetChoisi = GameVisual.demanderSaisie(this.scanner, "Objet choisi >");
            for (Objet objet : joueur.getObjets()) {
                if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                    objetChoisi = objet;
                }
            }
        }
        return objetChoisi;
    }

    public Monstre changeMonstre(Joueur joueur) {
        GameVisual.afficherTitreSection("Changement de monstre - " + joueur.getNomJoueur());
        int index = 1;
        for (Monstre monstre : joueur.getMonstres()) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        String nomMonstreChoisi = GameVisual.demanderSaisie(this.scanner, "Monstre envoye >");
        Monstre monstreChoisi = null;
        for (Monstre monstre : joueur.getMonstres()) {
            if (monstre.getNomMonstre().equalsIgnoreCase(nomMonstreChoisi)) {
                monstreChoisi = monstre;
            }
        }

        while (monstreChoisi == null) {
            GameVisual.afficherErreur("Monstre introuvable. Merci de saisir un nom de monstre disponible.");
            nomMonstreChoisi = GameVisual.demanderSaisie(this.scanner, "Monstre envoye >");
            for (Monstre monstre : joueur.getMonstres()) {
                if (monstre.getNomMonstre().equalsIgnoreCase(nomMonstreChoisi)) {
                    monstreChoisi = monstre;
                }
            }
        }
        return monstreChoisi;
    }

    public void gereOrdreExecutionActions(Object actionJoueur1, Object actionJoueur2) {
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
            } else {
                joueur2.getMonstreActuel().attaquer(joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);
                joueur1.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);
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
