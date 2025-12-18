package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.inventaire.Objet;
import com.esiea.monstre.poche.inventaire.medicaments.Medicament;
import com.esiea.monstre.poche.inventaire.potions.Potion;
import com.esiea.monstre.poche.loader.AttaqueLoader;
// import com.esiea.monstre.poche.loader.MedicamentLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;

// import com.esiea.monstre.poche.loader.PotionLoader;

public class Combat {
    private Joueur joueur1;
    private Joueur joueur2;

    private Terrain terrain;


    public void executerTour() {
        while ((joueur1.sontMonstresMorts() || joueur2.sontMonstresMorts()) == false) {
            Object actionJoueur1 = this.gereChoixAction(joueur1);
            Object actionJoueur2 = this.gereChoixAction(joueur2);

            this.gereOrdreExecutionActions(actionJoueur1, actionJoueur2);
        }
        this.finDePartie();
    }

    public void selectionnerMonstre(MonstreLoader monstreLoader, Joueur joueur) {
        while (joueur.getMonstres().size() <= 3) {
            System.out.println("Merci de choisir un monstre :");
            for (Monstre monstre : monstreLoader.getRessources()) {
                System.out.println("Nom du monstre : " + monstre.getNomMonstre() + "Type monstre : " + monstre.getTypeMonstre());
            }
            Scanner scanner = new Scanner(System.in);
            String monstreChoisi = scanner.nextLine();
            scanner.close();
            joueur.getMonstres().add(monstreLoader.getRessourceParNom(monstreChoisi));
        }
    }

    public void selectionnerAttaque(AttaqueLoader attaqueLoader, Joueur joueur) {
        for (Monstre monstre : joueur.getMonstres()) {
            System.out.println("Choisissez les attaques pour le monstre " + monstre.getNomMonstre() + " :");
            while (monstre.getAttaques().size() <= 4) {
                for (Attaque attaque : attaqueLoader.getRessources()) {
                    if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                        System.out.println("Nom de l'attaque : " + attaque.getNomAttaque() + " Type d'attaque : " + attaque.getTypeAttaque().getLabelType() + " Puissance : " + attaque.getPuissanceAttaque());
                    }
                }
                Scanner scanner = new Scanner(System.in);
                String nomAttaque = scanner.nextLine();
                scanner.close();
                monstre.ajouterAttaque(attaqueLoader.getRessourceParNom(nomAttaque));
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
            String objetChoisi = scanner.nextLine();
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
        System.out.println("Merci de choisir une action " + joueur.getNomJoueur() + " : ");
        System.out.println("1. Attaquer");
        System.out.println("2. Utiliser un objet");
        System.out.println("3. Changer de monstre");

        Scanner scanner = new Scanner(System.in);
        int choixAction = scanner.nextInt();
        scanner.close();
        while (choixAction != 1 || choixAction != 2 || choixAction != 3) {
            System.out.println("Choix invalide, merci de choisir une action " + joueur.getNomJoueur() + " : ");
            System.out.println("1. Attaquer");
            System.out.println("2. Utiliser un objet");
            System.out.println("3. Changer de monstre");
            scanner = new Scanner(System.in);
            choixAction = scanner.nextInt();
            scanner.close();
        }

        Object actionEffectuee = null;
        switch (choixAction) {
            case 1:
                Attaque attaqueChoisie = this.choixAttaque(joueur);
                actionEffectuee = attaqueChoisie;
                // joueur.getMonstreActuel().attaquer(joueur2.getMonstreActuel(), terrain, attaqueChoisie);
                break;
            case 2:
                Objet objetChoisi = this.utiliseObjet(joueur);
                actionEffectuee = objetChoisi;
                // objetChoisi.utiliserObjet(joueur.getMonstreActuel());
                break;
            case 3:
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
        System.out.println("Merci de choisir une attaque pour le monstre " + joueur.getMonstreActuel().getNomMonstre() + " de " + joueur.getNomJoueur() + " : ");
        Monstre monstreActuel = joueur.getMonstreActuel();
        for (Attaque attaque : monstreActuel.getAttaques()) {
            System.out.println("Nom de l'attaque : " + attaque.getNomAttaque() + " Puissance : " + attaque.getPuissanceAttaque());
        }

        Scanner scanner = new Scanner(System.in);
        String nomAttaqueChoisie = scanner.nextLine();
        scanner.close();
        Attaque attaqueChoisie = null;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            if (attaque.getNomAttaque().equalsIgnoreCase(nomAttaqueChoisie)) {
                attaqueChoisie = attaque;
            }
        }

        while (attaqueChoisie == null) {
            System.out.println("Attaque invalide, merci de choisir une attaque pour le monstre " + joueur.getMonstreActuel().getNomMonstre() + " de " + joueur.getNomJoueur() + " : ");
            for (Attaque attaque : monstreActuel.getAttaques()) {
                System.out.println("Nom de l'attaque : " + attaque.getNomAttaque() + " Puissance : " + attaque.getPuissanceAttaque());
            }
            scanner = new Scanner(System.in);
            nomAttaqueChoisie = scanner.nextLine();
            scanner.close();
            for (Attaque attaque : monstreActuel.getAttaques()) {
                if (attaque.getNomAttaque().equalsIgnoreCase(nomAttaqueChoisie)) {
                    attaqueChoisie = attaque;
                }
            }
        }
        return attaqueChoisie;
    }

    public Objet utiliseObjet(Joueur joueur) {
        System.out.println("Merci de choisir un objet à utiliser : ");
        for (Objet objet : joueur.getObjets()) {
            System.out.println("Nom de l'objet : " + objet.getNomObjet());
        }

        Scanner scanner = new Scanner(System.in);
        String nomObjetChoisi = scanner.nextLine();
        scanner.close();
        Objet objetChoisi = null;
        for (Objet objet : joueur.getObjets()) {
            if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                objetChoisi = objet;
            }
        }

        while (objetChoisi == null) {
            System.out.println("Objet invalide, merci de choisir un objet à utiliser : ");
            for (Objet objet : joueur.getObjets()) {
                System.out.println("Nom de l'objet : " + objet.getNomObjet());
            }
            scanner = new Scanner(System.in);
            nomObjetChoisi = scanner.nextLine();
            scanner.close();
            for (Objet objet : joueur.getObjets()) {
                if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                    objetChoisi = objet;
                }
            }
        }
        return objetChoisi;
    }

    public Monstre changeMonstre(Joueur joueur) {
        System.out.println("Merci de choisir un monstre à envoyer au combat : ");
        for (Monstre monstre : joueur.getMonstres()) {
            System.out.println("Nom du monstre : " + monstre.getNomMonstre());
        }

        Scanner scanner = new Scanner(System.in);
        String nomMonstreChoisi = scanner.nextLine();
        scanner.close();
        Monstre monstreChoisi = null;
        for (Monstre monstre : joueur.getMonstres()) {
            if (monstre.getNomMonstre().equalsIgnoreCase(nomMonstreChoisi)) {
                monstreChoisi = monstre;
            }
        }

        while (monstreChoisi == null) {
            System.out.println("Monstre invalide, merci de choisir un monstre à envoyer au combat : ");
            for (Monstre monstre : joueur.getMonstres()) {
                System.out.println("Nom du monstre : " + monstre.getNomMonstre());
            }
            scanner = new Scanner(System.in);
            nomMonstreChoisi = scanner.nextLine();
            scanner.close();
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
