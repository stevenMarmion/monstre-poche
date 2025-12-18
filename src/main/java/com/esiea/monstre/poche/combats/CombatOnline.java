package com.esiea.monstre.poche.combats;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.entites.Terrain;
import com.esiea.monstre.poche.inventaire.Objet;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;
import com.esiea.monstre.poche.online.Client;
import com.esiea.monstre.poche.online.GameMessage;
import com.esiea.monstre.poche.visual.GameVisual;

/**
 * Classe CombatOnline qui gère un combat entre un joueur local et un joueur en ligne.
 * Utilise un Client TCP pour communiquer avec le serveur et l'adversaire distant.
 */
public class CombatOnline extends Combat {
    private Client client;
    private final Scanner scanner = new Scanner(System.in);

    public CombatOnline(Joueur joueur, Client client, Terrain terrain) {
        super(joueur, null, terrain);
        this.client = client;
        joueur1 = joueur;
        joueur2 = null; // L'adversaire distant
    }

    /**
     * Lance le combat en ligne avec synchronisation réseau
     */
    @Override
    public void lancer(MonstreLoader monstreLoader, AttaqueLoader attaqueLoader) {
        GameVisual.afficherTitreSection("Bienvenue " + joueur1.getNomJoueur());
        
        // Le joueur local sélectionne ses monstres et attaques
        System.out.println("[INFO] Configuration de vos monstres et attaques...");
        this.selectionnerMonstre(monstreLoader, joueur1);
        this.selectionnerAttaque(attaqueLoader, joueur1);

        // Envoyer le message de prêt au serveur
        GameMessage messagePret = new GameMessage(GameMessage.ActionType.JOUEUR_PRET, 
                                                  joueur1.getNomJoueur(), 
                                                  joueur1.getNomJoueur(),
                                                  joueur1.getMonstres().size());
        client.envoyerMessage(messagePret);
        
        System.out.println("[INFO] En attente de l'adversaire...");
        
        // Attendre que l'adversaire soit prêt
        GameMessage messageAdversaire = null;
        while (messageAdversaire == null || messageAdversaire.getTypeAction() != GameMessage.ActionType.JOUEUR_PRET) {
            messageAdversaire = client.recevoirMessage();
            
            if (messageAdversaire == null) {
                GameVisual.afficherErreur("Erreur : connexion perdue avec le serveur.");
                return;
            }
            
            if (messageAdversaire.getTypeAction() == GameMessage.ActionType.CONNEXION) {
                System.out.println("[INFO] " + messageAdversaire.getMessage());
                continue; // Ignorer le message de connexion et attendre le prochain
            }
            
            if (messageAdversaire.getTypeAction() != GameMessage.ActionType.JOUEUR_PRET) {
                System.out.println("[INFO] Message reçu : " + messageAdversaire.getTypeAction());
                continue; // Ignorer les autres messages et attendre le JOUEUR_PRET
            }
        }
        
        GameVisual.afficherTitreSection("COMBAT EN LIGNE LANCÉ !");
        System.out.println("Adversaire : " + messageAdversaire.getNomJoueur());
        
        // Boucle de combat
        executerTour();
    }

    /**
     * Exécute les tours de combat en ligne
     */
    @Override
    public void executerTour() {
        boolean combatActif = true;
        
        while (combatActif) {
            // Le joueur local choisit son action
            Object actionJoueurLocal = this.gereChoixAction(joueur1);


            // Envoyer l'action au serveur (et donc à l'adversaire)
            if (actionJoueurLocal instanceof Attaque) {
                Attaque attaque = (Attaque) actionJoueurLocal;
                GameMessage message = new GameMessage(GameMessage.ActionType.ATTAQUE_CHOISIE,
                                                     joueur1.getNomJoueur(),
                                                     joueur1.getNomJoueur(),
                                                     attaque);
                client.envoyerMessage(message);
            } else if (actionJoueurLocal instanceof Monstre) {
                Monstre monstre = (Monstre) actionJoueurLocal;
                GameMessage message = new GameMessage(GameMessage.ActionType.MONSTRE_CHANGE,
                                                     joueur1.getNomJoueur(),
                                                     joueur1.getNomJoueur(),
                                                     monstre);
                client.envoyerMessage(message);
            } else if (actionJoueurLocal instanceof Objet) {
                Objet objet = (Objet) actionJoueurLocal;
                GameMessage message = new GameMessage(GameMessage.ActionType.OBJET_UTILISE,
                                                     joueur1.getNomJoueur(),
                                                     joueur1.getNomJoueur(),
                                                     objet);
                client.envoyerMessage(message);
            }

            // Recevoir l'action de l'adversaire
            GameMessage messageAdversaire = client.recevoirMessage();
            
            if (messageAdversaire == null) {
                GameVisual.afficherErreur("Connexion perdue avec l'adversaire.");
                combatActif = false;
            } else if (messageAdversaire.getTypeAction() == GameMessage.ActionType.DECONNEXION) {
                System.out.println("[INFO] L'adversaire s'est déconnecté.");
                combatActif = false;
            } else if (messageAdversaire.getTypeAction() == GameMessage.ActionType.FIN_PARTIE) {
                System.out.println("[INFO] L'adversaire a terminé le combat.");
                combatActif = false;
            }
            // Ici, on pourrait traiter l'action de l'adversaire
        }

        finDePartie();
    }

    /**
     * Fin du combat en ligne
     */
    @Override
    public void finDePartie() {
        GameVisual.afficherTitreSection("FIN DU COMBAT EN LIGNE");
        System.out.println("Combat terminé !");
        
        // Envoyer un message de fin
        GameMessage messageFin = new GameMessage(GameMessage.ActionType.FIN_PARTIE,
                                                joueur1.getNomJoueur(),
                                                joueur1.getNomJoueur());
        try {
            client.envoyerMessage(messageFin);
        } catch (Exception e) {
            System.out.println("[INFO] Impossible d'envoyer le message de fin");
        }

        // Se déconnecter
        client.deconnecter();
    }

    /**
     * Affiche l'interface pour les choix d'action avec vue sur l'adversaire
     */
    @Override
    public Object gereChoixAction(Joueur joueur) {
        GameVisual.afficherTitreSection("Votre tour - " + joueur.getNomJoueur());
        Monstre actif = joueur.getMonstreActuel();
        System.out.println("Monstre actif : " + actif.getNomMonstre() + " | PV " + (int) actif.getPointsDeVie() + "/" + (int) actif.getPointsDeVieMax() + " | ATK " + actif.getAttaque() + " | DEF " + actif.getDefense() + " | VIT " + actif.getVitesse());

        System.out.println("\nActions disponibles :");
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
}
