package com.esiea.monstre.poche.models.combats;

import java.io.IOException;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.models.loader.MonstreLoader;
import com.esiea.monstre.poche.models.online.OnlineConnection;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.visual.GameVisual;

/**
 * Variante en ligne du combat : le joueur1 est local, le joueur2 joue a distance.
 */
public class CombatEnLigne extends Combat {
    private final OnlineConnection connection;

    public CombatEnLigne(Joueur joueurLocal, Joueur joueurDistant, Terrain terrain, OnlineConnection connection) {
        super(joueurLocal, joueurDistant, terrain);
        this.connection = connection;
    }

    @Override
    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        afficherTitrePourTous("Configuration de vos monstres");
        this.selectionnerMonstre(monstreLoader, joueur1);
        this.selectionnerAttaque(attaqueLoader, joueur1);

        connection.sendInfo("Configuration de vos monstres");
        selectionnerMonstreDistant(monstreLoader, joueur2);
        selectionnerAttaqueDistant(attaqueLoader, joueur2);

        afficherTitrePourTous("COMBAT EN LIGNE LANCE !");
        connection.sendInfo("Combat lance !");
        this.executerTour();
    }

    @Override
    public void executerTour() {
        while (!joueur1.sontMonstresMorts() && !joueur2.sontMonstresMorts()) {
            Object actionLocal = this.gereChoixAction(joueur1);
            Object actionDistant = this.gereChoixActionDistant();

            informerActions(actionLocal, actionDistant);
            gereOrdreExecutionActionsEnLigne(actionLocal, actionDistant);
        }
        this.finDePartie();
    }

    private void informerActions(Object actionLocal, Object actionDistant) {
        String descriptionLocal = decrireAction(joueur1.getMonstreActuel(), actionLocal);
        String descriptionDistant = decrireAction(joueur2.getMonstreActuel(), actionDistant);
        broadcast(descriptionLocal);
        broadcast(descriptionDistant);
    }

    private String decrireAction(Monstre monstre, Object action) {
        if (action instanceof Attaque) {
            return "[ACTION] " + monstre.getNomMonstre() + " lance " + ((Attaque) action).getNomAttaque();
        }
        if (action instanceof Monstre) {
            return "[ACTION] changement de monstre -> " + ((Monstre) action).getNomMonstre();
        }
        if (action instanceof Objet) {
            return "[ACTION] utilisation de l'objet " + ((Objet) action).getNomObjet();
        }
        return "[ACTION] aucune action";
    }

    private void selectionnerMonstreDistant(MonstreLoader monstreLoader, Joueur joueur) {
        broadcastToRemoteTitre("Selection des monstres - " + joueur.getNomJoueur());
        broadcastToRemoteSousTitre("Monstres disponibles");
        int index = 1;
        for (Monstre monstre : monstreLoader.getRessources()) {
            broadcastToRemote(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < 3) {
            try {
                String choixInput = connection.ask("Choix " + (joueur.getMonstres().size() + 1) + "/3 >");
                int indexChoisi = Integer.parseInt(choixInput);

                if (indexChoisi < 1 || indexChoisi > monstreLoader.getRessources().size()) {
                    broadcastToRemoteErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstreLoader.getRessources().size());
                    continue;
                }

                Monstre monstreCharge = monstreLoader.getRessources().get(indexChoisi - 1);

                if (joueur.getMonstres().contains(monstreCharge)) {
                    broadcastToRemoteErreur("Ce monstre a deja ete selectionne.");
                    continue;
                }

                joueur.ajouterMonstre(monstreCharge);
                broadcastToRemote("  [OK] Monstre ajoute : " + monstreCharge.getNomMonstre());
            } catch (NumberFormatException e) {
                broadcastToRemoteErreur("Saisie invalide. Veuillez entrer un numero.");
            } catch (IOException e) {
                broadcastToRemote("Connexion fermee pendant la selection.");
                return;
            }
        }
        joueur.setMonstreActuel(joueur.getMonstres().get(0));
        broadcastToRemote("Monstre actif initial : " + joueur.getMonstreActuel().getNomMonstre());
    }

    private void selectionnerAttaqueDistant(AttaqueLoader attaqueLoader, Joueur joueur) {
        broadcastToRemoteTitre("Selection des attaques - " + joueur.getNomJoueur());
        for (Monstre monstre : joueur.getMonstres()) {
            broadcastToRemoteSousTitre("Monstre : " + monstre.getNomMonstre());
            java.util.ArrayList<Attaque> attaquesCompatibles = new java.util.ArrayList<>();
            for (Attaque attaque : attaqueLoader.getRessources()) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    attaquesCompatibles.add(attaque);
                }
            }
            int index = 1;
            for (Attaque attaque : attaquesCompatibles) {
                broadcastToRemote(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
            }

            while (monstre.getAttaques().size() < 4) {
                try {
                    String choixInput = connection.ask("Choix " + (monstre.getAttaques().size() + 1) + "/4 >");
                    int indexChoisi = Integer.parseInt(choixInput);

                    if (indexChoisi < 1 || indexChoisi > attaquesCompatibles.size()) {
                        broadcastToRemoteErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + attaquesCompatibles.size());
                        continue;
                    }

                    Attaque attaqueChargee = attaquesCompatibles.get(indexChoisi - 1);

                    if (monstre.getAttaques().contains(attaqueChargee)) {
                        broadcastToRemoteErreur("Attaque deja selectionnee pour ce monstre.");
                        continue;
                    }

                    monstre.ajouterAttaque(attaqueChargee);
                    broadcastToRemote("  [OK] Attaque ajoutee : " + attaqueChargee.getNomAttaque());
                } catch (NumberFormatException e) {
                    broadcastToRemoteErreur("Saisie invalide. Veuillez entrer un numero.");
                } catch (IOException e) {
                    broadcastToRemote("Connexion fermee pendant la selection d'attaques.");
                    return;
                }
            }
        }
    }

    private Object gereChoixActionDistant() {
        Monstre actif = joueur2.getMonstreActuel();
        broadcastToRemoteTitre("Tour de " + joueur2.getNomJoueur());
        broadcastToRemote("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());
        broadcastToRemote("");
        broadcastToRemote("Actions disponibles :");
        broadcastToRemote("  1) Attaquer");
        broadcastToRemote("  2) Utiliser un objet");
        broadcastToRemote("  3) Changer de monstre");

        String choixAction;
        try {
            choixAction = connection.ask("Votre choix >");
            while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
                broadcastToRemoteErreur("Saisie invalide. Merci de choisir 1, 2 ou 3.");
                choixAction = connection.ask("Votre choix >");
            }
        } catch (IOException e) {
            broadcastToRemote("Connexion fermee pendant le choix d'action.");
            return null;
        }

        switch (choixAction) {
            case "1":
                return this.choixAttaqueDistant(joueur2);
            case "2":
                return this.utiliseObjetDistant(joueur2);
            case "3":
                return this.changeMonstreDistant(joueur2);
            default:
                return null;
        }
    }

    private Attaque choixAttaqueDistant(Joueur joueur) {
        Monstre monstreActuel = joueur.getMonstreActuel();
        broadcastToRemoteTitre("Attaques de " + monstreActuel.getNomMonstre());
        int index = 1;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            broadcastToRemote(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
        }

        while (true) {
            try {
                String choixInput = connection.ask("Attaque choisie >");
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > monstreActuel.getAttaques().size()) {
                    broadcastToRemoteErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstreActuel.getAttaques().size());
                    continue;
                }
                return monstreActuel.getAttaques().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                broadcastToRemoteErreur("Saisie invalide. Veuillez entrer un numero.");
            } catch (IOException e) {
                broadcastToRemote("Connexion fermee pendant la selection d'attaque.");
                return null;
            }
        }
    }

    private Objet utiliseObjetDistant(Joueur joueur) {
        broadcastTitre("Objets de " + joueur.getNomJoueur());
        int index = 1;
        for (Objet objet : joueur.getObjets()) {
            broadcastToRemote(String.format("[%d] %s", index++, objet.getNomObjet()));
        }

        try {
            String nomObjetChoisi = connection.ask("Objet choisi >");
            for (Objet objet : joueur.getObjets()) {
                if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                    return objet;
                }
            }
            broadcastToRemoteErreur("Objet introuvable. L'action est ignoree.");
        } catch (IOException e) {
            broadcastToRemote("Connexion fermee pendant le choix d'objet.");
        }
        return null;
    }

    private Monstre changeMonstreDistant(Joueur joueur) {
        broadcastTitre("Changement de monstre - " + joueur.getNomJoueur());
        int index = 1;
        for (Monstre monstre : joueur.getMonstres()) {
            broadcast(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (true) {
            try {
                String choixInput = connection.ask("Monstre envoye >");
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > joueur.getMonstres().size()) {
                    broadcastErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + joueur.getMonstres().size());
                    continue;
                }
                return joueur.getMonstres().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                broadcastErreur("Saisie invalide. Veuillez entrer un numero.");
            } catch (IOException e) {
                broadcast("Connexion fermee pendant le changement de monstre.");
                return null;
            }
        }
    }

    @Override
    public void finDePartie() {
        if (joueur1.sontMonstresMorts()) {
            broadcast("Le joueur " + joueur2.getNomJoueur() + " a gagne la partie !");
        } else if (joueur2.sontMonstresMorts()) {
            broadcast("Le joueur " + joueur1.getNomJoueur() + " a gagne la partie !");
        }
        connection.sendEnd("Fin de la partie. Merci d'avoir joue.");
    }

    private void broadcast(String message) {
        System.out.println(message);
        connection.sendInfo(message);
    }

    private void broadcastToRemote(String message) {
        connection.sendInfo(message);
    }

    private void broadcastTitre(String titre) {
        GameVisual.afficherTitreSection(titre);
        String formatted = GameVisual.formatterTitre(titre);
        for (String line : formatted.split("\n")) {
            if (!line.isEmpty()) {
                connection.sendInfo(line);
            }
        }
    }

    private void broadcastToRemoteTitre(String titre) {
        String formatted = GameVisual.formatterTitre(titre);
        for (String line : formatted.split("\n")) {
            if (!line.isEmpty()) {
                connection.sendInfo(line);
            }
        }
    }

    private void broadcastToRemoteSousTitre(String sousTitre) {
        connection.sendInfo(GameVisual.formatterSousTitre(sousTitre));
    }

    private void broadcastErreur(String message) {
        GameVisual.afficherErreur(message);
        connection.sendInfo(GameVisual.formatterErreur(message));
    }

    private void broadcastToRemoteErreur(String message) {
        connection.sendInfo(GameVisual.formatterErreur(message));
    }

    private void afficherTitrePourTous(String titre) {
        GameVisual.afficherTitreSection(titre);
        String formatted = GameVisual.formatterTitre(titre);
        for (String line : formatted.split("\n")) {
            if (!line.isEmpty()) {
                connection.sendInfo(line);
            }
        }
    }

    private void gereOrdreExecutionActionsEnLigne(Object actionJoueur1, Object actionJoueur2) {
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
                executerAttaqueAvecRelai(joueur1.getMonstreActuel(), joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);
                executerAttaqueAvecRelai(joueur2.getMonstreActuel(), joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            } else {
                executerAttaqueAvecRelai(joueur2.getMonstreActuel(), joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);
                executerAttaqueAvecRelai(joueur1.getMonstreActuel(), joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);

                if (joueur1.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur1.switchMonstreActuelAuto();
                } else if (joueur2.getMonstreActuel().getPointsDeVie() == 0) {
                    joueur2.switchMonstreActuelAuto();
                }
            }
        } else if (actionJoueur1 instanceof Attaque) {
            executerAttaqueAvecRelai(joueur1.getMonstreActuel(), joueur2.getMonstreActuel(), terrain, (Attaque) actionJoueur1);
        } else if (actionJoueur2 instanceof Attaque) {
            executerAttaqueAvecRelai(joueur2.getMonstreActuel(), joueur1.getMonstreActuel(), terrain, (Attaque) actionJoueur2);
        }
    }

    private void executerAttaqueAvecRelai(Monstre attaquant, Monstre cible, Terrain terrain, Attaque attaque) {
        // Exécuter l'attaque (génère des System.out.println)
        attaquant.attaquer(cible, terrain, attaque);
        
        // Relayer les informations de l'attaque au client
        broadcast("\n[COMBAT] " + attaquant.getNomMonstre() + " attaque " + cible.getNomMonstre() + " avec " + attaque.getNomAttaque() + ".");
        
        // Calculer et afficher les dégats
        double degats = attaque.calculeDegatsAttaque(attaquant, cible);
        broadcast("         Degats infliges : " + (int) degats);
        
        if (cible.getPointsDeVie() <= 0) {
            broadcast("         " + cible.getNomMonstre() + " est KO !");
        }
        
        broadcast("         PV restants pour " + cible.getNomMonstre() + " : " + (int) cible.getPointsDeVie());
    }
}
