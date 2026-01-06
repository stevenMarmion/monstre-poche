package com.esiea.monstre.poche.models.combats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.loader.GameResourcesFactory;
import com.esiea.monstre.poche.models.online.OnlineConnection;
import com.esiea.monstre.poche.models.visual.GameVisual;

/**
 * Variante en ligne du combat : le joueur1 est local, le joueur2 joue a distance.
 */
public class CombatEnLigne extends Combat {
    private final OnlineConnection connection;

    public CombatEnLigne(Joueur joueurLocal, Joueur joueurDistant, OnlineConnection connection) {
        super(joueurLocal, joueurDistant);
        this.connection = connection;
    }

    @Override
    public void lancer(GameResourcesFactory resourcesFactory) {
        afficherTitrePourTous("Configuration de vos monstres");
        this.selectionnerMonstre(resourcesFactory, joueur1);
        this.selectionnerAttaque(resourcesFactory, joueur1);


        //TODO selectionner objets

        afficherTitrePourTous("Configuration adversaire");
        selectionnerMonstreDistant(resourcesFactory, joueur2);
        selectionnerAttaqueDistant(resourcesFactory, joueur2);

        afficherTitrePourTous("COMBAT EN LIGNE LANCE !");
        this.executerTour();
    }



    @Override
    public void selectionnerMonstre(GameResourcesFactory resourcesFactory, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des monstres - " + joueur.getNomJoueur());
        GameVisual.afficherSousTitre("Monstres disponibles");
        int index = 1;
        List<Monstre> monstresDisponibles = resourcesFactory.getTousLesMonstres();
        for (Monstre monstre : monstresDisponibles) {
            CombatLogger.log(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < 3) {
            String choixInput = GameVisual.demanderSaisie(new Scanner(System.in), "Choix " + (joueur.getMonstres().size() + 1) + "/3 >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > monstresDisponibles.size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstresDisponibles.size());
                    continue;
                }
                Monstre monstreCharge = monstresDisponibles.get(indexChoisi - 1);
                if (joueur.getMonstres().contains(monstreCharge)) {
                    GameVisual.afficherErreur("Ce monstre a deja ete selectionne.");
                    continue;
                }
                joueur.ajouterMonstre(monstreCharge);
                CombatLogger.log("  [OK] Monstre ajoute : " + monstreCharge.getNomMonstre());
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
        joueur.setMonstreActuel(joueur.getMonstres().get(0));
        CombatLogger.log("Monstre actif initial : " + joueur.getMonstreActuel().getNomMonstre());
    }

    @Override
    public void selectionnerAttaque(GameResourcesFactory resourcesFactory, Joueur joueur) {
        GameVisual.afficherTitreSection("Selection des attaques - " + joueur.getNomJoueur());
        for (Monstre monstre : joueur.getMonstres()) {
            GameVisual.afficherSousTitre("Monstre : " + monstre.getNomMonstre());

            ArrayList<Attaque> attaquesCompatibles = new ArrayList<>();
            //TODO nombres magiques dans la fonction, et éventuellement faire une fonction utilitaire pour la récupération des attaques compatibles car là on duplique le meme bout de code
            // plusieurs fois dans Combat, CombatEnLigne, CombatBot
            for (Attaque attaque : resourcesFactory.getToutesLesAttaques()) {
                if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                    attaquesCompatibles.add(attaque);
                }
            }
            int index = 1;
            for (Attaque attaque : attaquesCompatibles) {
                broadcastToRemote(String.format("[%d] %s", index++, GameVisual.formatterAttaque(attaque)));
            }

            while (monstre.getAttaques().size() < 4) {
                String choixInput = GameVisual.demanderSaisie(new Scanner(System.in), "Choix " + (monstre.getAttaques().size() + 1) + "/4 >");
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
                    CombatLogger.log("  [OK] Attaque ajoutee : " + attaqueChargee.getNomAttaque());
                } catch (NumberFormatException e) {
                    GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
                }
            }
        }
    }

    @Override
    public void selectionnerObjet(GameResourcesFactory resourcesFactory, Joueur joueur) {
        //TODO
    }

    @Override
    public Object gereChoixAction(Joueur joueur) {
        // Afficher localement ET envoyer au client distant
        broadcastTitre("Tour de " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        String infoMonstre = "Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse();
        CombatLogger.log(infoMonstre);
        connection.sendInfo(infoMonstre);
        
        CombatLogger.log("Actions disponibles :");
        CombatLogger.log("  1) Attaquer");
        CombatLogger.log("  2) Utiliser un objet");
        CombatLogger.log("  3) Changer de monstre");
        connection.sendInfo("Actions disponibles :");
        connection.sendInfo("  1) Attaquer");
        connection.sendInfo("  2) Utiliser un objet");
        connection.sendInfo("  3) Changer de monstre");
        
        String choixAction = GameVisual.demanderSaisie(new Scanner(System.in), "Votre choix >");
        while (!choixAction.equals("1") && !choixAction.equals("2") && !choixAction.equals("3")) {
            CombatLogger.error("Saisie invalide. Merci de choisir 1, 2 ou 3.");
            choixAction = GameVisual.demanderSaisie(new Scanner(System.in), "Votre choix >");
        }
        switch (choixAction) {
            case "1":
                return choixAttaqueLocal(joueur);
            case "2":
                return utiliseObjetLocal(joueur);
            case "3":
                return changeMonstreLocal(joueur);
            default:
                return null;
        }
    }

    private Attaque choixAttaqueLocal(Joueur joueur) {
        Monstre monstreActuel = joueur.getMonstreActuel();
        GameVisual.afficherTitreSection("Attaques de " + monstreActuel.getNomMonstre());
        int index = 1;
        for (Attaque attaque : monstreActuel.getAttaques()) {
            String ppStatus = attaque.getNbUtilisations() <= 0 ? " [VIDE]" : "";
            CombatLogger.log(String.format("[%d] %s%s", index++, GameVisual.formatterAttaque(attaque), ppStatus));
        }
        // Option mains nues toujours disponible
        CombatLogger.log(String.format("[0] MAINS NUES            | PP:infini | Puissance: faible"));
        
        while (true) {
            String choixInput = GameVisual.demanderSaisie(new Scanner(System.in), "Attaque choisie (0 pour mains nues) >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi == 0) {
                    // Mains nues - retourne null
                    CombatLogger.log("  [OK] Attaque à mains nues sélectionnée.");
                    return null;
                }
                if (indexChoisi < 1 || indexChoisi > monstreActuel.getAttaques().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir 0 ou un nombre entre 1 et " + monstreActuel.getAttaques().size());
                    continue;
                }
                Attaque attaqueTemp = monstreActuel.getAttaques().get(indexChoisi - 1);
                if (attaqueTemp.getNbUtilisations() <= 0) {
                    GameVisual.afficherErreur("Cette attaque n'a plus de PP ! Choisissez une autre attaque ou 0 pour mains nues.");
                    continue;
                }
                return attaqueTemp;
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
    }

    private Objet utiliseObjetLocal(Joueur joueur) {
        GameVisual.afficherTitreSection("Objets de " + joueur.getNomJoueur());
        int index = 1;
        for (Objet objet : joueur.getObjets()) {
            CombatLogger.log(String.format("[%d] %s", index++, objet.getNomObjet()));
        }
        String nomObjetChoisi = GameVisual.demanderSaisie(new Scanner(System.in), "Objet choisi >");
        for (Objet objet : joueur.getObjets()) {
            if (objet.getNomObjet().equalsIgnoreCase(nomObjetChoisi)) {
                return objet;
            }
        }
        GameVisual.afficherErreur("Objet introuvable. L'action est ignoree.");
        return null;
    }

    private Monstre changeMonstreLocal(Joueur joueur) {
        GameVisual.afficherTitreSection("Changement de monstre - " + joueur.getNomJoueur());
        int index = 1;
        for (Monstre monstre : joueur.getMonstres()) {
            CombatLogger.log(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }
        while (true) {
            String choixInput = GameVisual.demanderSaisie(new Scanner(System.in), "Monstre envoye >");
            try {
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi < 1 || indexChoisi > joueur.getMonstres().size()) {
                    GameVisual.afficherErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + joueur.getMonstres().size());
                    continue;
                }
                return joueur.getMonstres().get(indexChoisi - 1);
            } catch (NumberFormatException e) {
                GameVisual.afficherErreur("Saisie invalide. Veuillez entrer un numero.");
            }
        }
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
        CombatLogger.logSeparateur();
        String descriptionLocal = decrireAction(joueur1, actionLocal);
        String descriptionDistant = decrireAction(joueur2, actionDistant);
        CombatLogger.log(descriptionLocal);
        CombatLogger.log(descriptionDistant);
        connection.sendInfo("----------------------------------------");
        connection.sendInfo(descriptionLocal);
        connection.sendInfo(descriptionDistant);
    }

    private String decrireAction(Joueur joueur, Object action) {
        Monstre monstre = joueur.getMonstreActuel();
        if (action instanceof Attaque) {
            return "  " + joueur.getNomJoueur() + " : " + monstre.getNomMonstre() + " va utiliser " + ((Attaque) action).getNomAttaque();
        }
        if (action instanceof Monstre) {
            return "  " + joueur.getNomJoueur() + " : change de monstre pour " + ((Monstre) action).getNomMonstre();
        }
        if (action instanceof Objet) {
            return "  " + joueur.getNomJoueur() + " : utilise " + ((Objet) action).getNomObjet();
        }
        return "  " + joueur.getNomJoueur() + " : aucune action";
    }

    private void selectionnerMonstreDistant(GameResourcesFactory resourcesFactory, Joueur joueur) {
        broadcastToRemoteTitre("Selection des monstres - " + joueur.getNomJoueur());
        broadcastToRemoteSousTitre("Monstres disponibles");
        int index = 1;
        List<Monstre> monstresDisponibles = resourcesFactory.getTousLesMonstres();
        for (Monstre monstre : monstresDisponibles) {
            broadcastToRemote(String.format("[%d] %s", index++, GameVisual.formatterMonstre(monstre)));
        }

        while (joueur.getMonstres().size() < 3) {
            try {
                String choixInput = connection.ask("Choix " + (joueur.getMonstres().size() + 1) + "/3 >");
                int indexChoisi = Integer.parseInt(choixInput);

                if (indexChoisi < 1 || indexChoisi > monstresDisponibles.size()) {
                    broadcastToRemoteErreur("Index invalide. Veuillez choisir un nombre entre 1 et " + monstresDisponibles.size());
                    continue;
                }

                Monstre monstreCharge = monstresDisponibles.get(indexChoisi - 1);

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

    private void selectionnerAttaqueDistant(GameResourcesFactory resourcesFactory, Joueur joueur) {
        broadcastToRemoteTitre("Selection des attaques - " + joueur.getNomJoueur());
        for (Monstre monstre : joueur.getMonstres()) {
            broadcastToRemoteSousTitre("Monstre : " + monstre.getNomMonstre());
            ArrayList<Attaque> attaquesCompatibles = new ArrayList<>();
            //TODO nombres magiques dans la fonction, et éventuellement faire une fonction utilitaire pour la récupération des attaques compatibles car là on duplique le meme bout de code
            // plusieurs fois dans Combat, CombatEnLigne, CombatBot
            for (Attaque attaque : resourcesFactory.getToutesLesAttaques()) {
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
            String ppStatus = attaque.getNbUtilisations() <= 0 ? " [VIDE]" : "";
            broadcastToRemote(String.format("[%d] %s%s", index++, GameVisual.formatterAttaque(attaque), ppStatus));
        }
        // Option mains nues toujours disponible
        broadcastToRemote(String.format("[0] MAINS NUES            | PP:infini | Puissance: faible"));

        while (true) {
            try {
                String choixInput = connection.ask("Attaque choisie (0 pour mains nues) >");
                int indexChoisi = Integer.parseInt(choixInput);
                if (indexChoisi == 0) {
                    // Mains nues - retourne null
                    broadcastToRemote("  [OK] Attaque à mains nues sélectionnée.");
                    return null;
                }
                if (indexChoisi < 1 || indexChoisi > monstreActuel.getAttaques().size()) {
                    broadcastToRemoteErreur("Index invalide. Veuillez choisir 0 ou un nombre entre 1 et " + monstreActuel.getAttaques().size());
                    continue;
                }
                Attaque attaqueTemp = monstreActuel.getAttaques().get(indexChoisi - 1);
                if (attaqueTemp.getNbUtilisations() <= 0) {
                    broadcastToRemoteErreur("Cette attaque n'a plus de PP ! Choisissez une autre attaque ou 0 pour mains nues.");
                    continue;
                }
                return attaqueTemp;
            } catch (NumberFormatException e) {
                broadcastToRemoteErreur("Saisie invalide. Veuillez entrer un numero.");
            } catch (IOException e) {
                broadcastToRemote("Connexion fermee pendant la selection d'attaque.");
                return null;
            }
        }
    }

    public Objet utiliseObjetDistant(Joueur joueur) {
        broadcastTitre("Objets de " + joueur.getNomJoueur());
        int index = 1;
        List<Objet> objets = joueur.getObjets();
        if (objets.isEmpty()) {
            broadcastToRemoteErreur("Aucun objet disponible.");
            return null;
        }
        for (Objet objet : objets) {
            broadcastToRemote(String.format("[%d] %s", index++, objet.getNomObjet()));

            for (int i = 0; i < objets.size(); i++) {
                System.out.printf("[%d] %s%n", i + 1, objets.get(i).getNomObjet());
            }
            while (true) {
                String indexObjetChoisi;
                try {
                    indexObjetChoisi = connection.ask("Objet choisi >");
                } catch (IOException e) {
                    broadcastToRemote("Connexion fermee pendant la selection d'attaque.");
                    return null;
                }
                int indexChoisi;
                try {
                    indexChoisi = Integer.parseInt(indexObjetChoisi);
                } catch (NumberFormatException e) {
                    broadcastToRemoteErreur("Veuillez entrer un nombre valide.");
                    continue;
                }

                if (indexChoisi < 1 || indexChoisi > objets.size()) {
                    broadcastToRemoteErreur(
                            "Index invalide. Choisissez un nombre entre 1 et " + objets.size()
                    );
                    continue;
                }

                return objets.get(indexChoisi - 1);
            }
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

    public void finDePartie() {
        Joueur gagnant;
        if (joueur1.sontMonstresMorts()) {
            gagnant = joueur2;
        } else {
            gagnant = joueur1;
        }
        
        // Log local
        CombatLogger.log("");
        CombatLogger.log("========================================");
        CombatLogger.log("  VICTOIRE DE " + gagnant.getNomJoueur().toUpperCase() + " !");
        CombatLogger.log("========================================");
        CombatLogger.log("");
        
        // Envoyer au client
        connection.sendInfo("");
        connection.sendInfo("========================================");
        connection.sendInfo("  VICTOIRE DE " + gagnant.getNomJoueur().toUpperCase() + " !");
        connection.sendInfo("========================================");
        connection.sendEnd("Fin de la partie. Merci d'avoir joue.");
    }

    private void broadcast(String message) {
        CombatLogger.log(message);
        connection.sendInfo(message);
    }

    private void broadcastToRemote(String message) {
        CombatLogger.log(message);
        connection.sendInfo(message);
    }

    private void broadcastTitre(String titre) {
        CombatLogger.logTitre(titre);
        connection.sendInfo("");
        connection.sendInfo("========================================");
        connection.sendInfo("  " + titre.toUpperCase());
        connection.sendInfo("========================================");
    }

    private void broadcastToRemoteTitre(String titre) {
        CombatLogger.logTitre(titre);
        connection.sendInfo("");
        connection.sendInfo("========================================");
        connection.sendInfo("  " + titre.toUpperCase());
        connection.sendInfo("========================================");
    }

    private void broadcastToRemoteSousTitre(String sousTitre) {
        CombatLogger.logSousTitre(sousTitre);
        connection.sendInfo("");
        connection.sendInfo("  > " + sousTitre);
        connection.sendInfo("----------------------------------------");
    }

    private void broadcastErreur(String message) {
        CombatLogger.error(message);
        connection.sendInfo("[ERREUR] " + message);
    }

    private void broadcastToRemoteErreur(String message) {
        CombatLogger.error(message);
        connection.sendInfo("[ERREUR] " + message);
    }

    private void afficherTitrePourTous(String titre) {
        CombatLogger.logTitre(titre);
        connection.sendInfo("");
        connection.sendInfo("========================================");
        connection.sendInfo("  " + titre.toUpperCase());
        connection.sendInfo("========================================");
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
        // Calculer les degats avant l'attaque
        double degats = attaque.calculeDegatsAttaque(attaquant, cible);
        
        // Executer l'attaque
        attaquant.attaquer(cible, terrain, attaque);
        
        // Logger l'attaque de maniere uniforme
        String msgAttaque = attaquant.getNomMonstre() + " utilise " + attaque.getNomAttaque() + " !";
        String msgDegats = "    -> " + cible.getNomMonstre() + " subit " + (int) degats + " degats";
        String msgPV = "    -> PV de " + cible.getNomMonstre() + ": " + (int) cible.getPointsDeVie() + "/" + (int) cible.getPointsDeVieMax();
        
        CombatLogger.log("");
        CombatLogger.log("  " + msgAttaque);
        CombatLogger.log(msgDegats);
        CombatLogger.log(msgPV);
        
        connection.sendInfo("");
        connection.sendInfo("  " + msgAttaque);
        connection.sendInfo(msgDegats);
        connection.sendInfo(msgPV);
        
        if (cible.getPointsDeVie() <= 0) {
            String msgKO = "  " + cible.getNomMonstre() + " est K.O. !";
            CombatLogger.log("");
            CombatLogger.log(msgKO);
            connection.sendInfo("");
            connection.sendInfo(msgKO);
        }
    }

    @Override
    public Attaque choixAttaque(Joueur joueur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'choixAttaque'");
    }

    @Override
    public Objet utiliseObjet(Joueur joueur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'utiliseObjet'");
    }

    @Override
    public Monstre changeMonstre(Joueur joueur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeMonstre'");
    }
}
