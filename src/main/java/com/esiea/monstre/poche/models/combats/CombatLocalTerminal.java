package com.esiea.monstre.poche.models.combats;

import java.util.Scanner;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.visual.GameVisual;

/**
 * Combat pour deux joueurs locaux via terminal.
 * Contient la gestion d'entrée/sortie terminal et repose sur la logique métier de Combat.
 */
public class CombatLocalTerminal extends Combat {
    private final Scanner scanner = new Scanner(System.in);

    public CombatLocalTerminal(Joueur joueur1, Joueur joueur2) {
        super(joueur1, joueur2);
    }

    @Override
    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        GameVisual.afficherTitreSection("Configuration des monstres et attaques");
        selectionnerMonstre(monstreLoader, joueur1);
        selectionnerMonstre(monstreLoader, joueur2);

        selectionnerAttaque(attaqueLoader, joueur1);
        selectionnerAttaque(attaqueLoader, joueur2);

        GameVisual.afficherTitreSection("COMBAT LANCE !");
        executerTour();
    }

    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            Object actionJoueur1 = gereChoixAction(joueur1);
            Object actionJoueur2 = gereChoixAction(joueur2);
            Combat.gereOrdreExecutionActions(actionJoueur1, actionJoueur2);
        }
        super.finDePartie();
    }

    @Override
    public void selectionnerMonstre(MonstreLoader monstreLoader, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des monstres - " + joueur.getNomJoueur());
        GameVisual.afficherSousTitre("Monstres disponibles");

        int index = 1;
        for (Monstre monstre : monstreLoader.getRessources()) {
            System.out.println(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < 3) {
            String choixInput = GameVisual.demanderSaisie(this.scanner, "Choix " + (joueur.getMonstres().size() + 1) + "/3 >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > monstreLoader.getRessources().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstreLoader.getRessources().size());
                    continue;
                }
                Monstre monstreCharge = monstreLoader.getRessources().get(indexChoisi - 1);
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

    @Override
    public void selectionnerAttaque(AttaqueLoader attaqueLoader, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des attaques - " + joueur.getNomJoueur());
        for (Monstre monstre : joueur.getMonstres()) {
            GameVisual.afficherSousTitre("Monstre : " + monstre.getNomMonstre());
            java.util.ArrayList<Attaque> attaquesCompatibles = new java.util.ArrayList<>();
            for (Attaque attaque : attaqueLoader.getRessources()) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    attaquesCompatibles.add(attaque);
                }
            }
            int index = 1;
            for (Attaque attaque : attaquesCompatibles) {
                System.out.println(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
            }

            while (monstre.getAttaques().size() < 4) {
                String choixInput = GameVisual.demanderSaisie(this.scanner, "Choix " + (monstre.getAttaques().size() + 1) + "/4 >");
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

    @Override
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
                break;
            case "2":
                Objet objetChoisi = this.utiliseObjet(joueur);
                actionEffectuee = objetChoisi;
                break;
            case "3":
                Monstre monstreChoisi = this.changeMonstre(joueur);
                actionEffectuee = monstreChoisi;
                break;
            default:
                break;
        }
        return actionEffectuee;
    }

    @Override
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

    @Override
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

    @Override
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
}
