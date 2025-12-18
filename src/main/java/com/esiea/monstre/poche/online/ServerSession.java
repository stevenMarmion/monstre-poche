package com.esiea.monstre.poche.online;

import com.esiea.monstre.poche.combats.Combat;
import com.esiea.monstre.poche.entites.Joueur;
import com.esiea.monstre.poche.loader.AttaqueLoader;
import com.esiea.monstre.poche.loader.MonstreLoader;

/**
 * Classe gérant une session de jeu entre deux joueurs en ligne.
 * Synchronise les actions des deux joueurs et gère la communication.
 */
public class ServerSession implements Runnable {
    private ClientHandler client1;
    private ClientHandler client2;
    private volatile boolean sessionActive;
    
    public ServerSession(ClientHandler client1, ClientHandler client2, Joueur joueur1, Joueur joueur2,
                        AttaqueLoader attaqueLoader, MonstreLoader monstreLoader) {
        this.client1 = client1;
        this.client2 = client2;
        this.sessionActive = true;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("[SESSION] Nouvelle session démarrée entre " + client1.getIdJoueur() + " et " + client2.getIdJoueur());
            
            // Envoyer un message de confirmation aux deux clients
            envoyerMessageAuxDeux(new GameMessage(GameMessage.ActionType.CONNEXION, client1.getIdJoueur(), "Serveur", "Connexion établie. Attente de l'adversaire..."));
            
            // Boucle principale de la session
            while (sessionActive && client1.isConnected() && client2.isConnected()) {
                // Attendre l'action du joueur 1
                GameMessage actionJoueur1 = attendreAction(client1);
                if (!sessionActive || actionJoueur1 == null) {
                    break;
                }

                // Attendre l'action du joueur 2
                GameMessage actionJoueur2 = attendreAction(client2);
                if (!sessionActive || actionJoueur2 == null) {
                    break;
                }

                // À ce stade, on a les deux actions : on peut appliquer l'ordre d'exécution
                appliquerCoupleActions(actionJoueur1, actionJoueur2);
            }
            
            // Session terminée
            terminerSession();
            
        } catch (Exception e) {
            System.err.println("[ERREUR] Erreur dans la session : " + e.getMessage());
            terminerSession();
        }
    }
    
    /**
     * Attend une action d'un joueur (bloquant)
     */
    private GameMessage attendreAction(ClientHandler joueur) {
        GameMessage message = joueur.recevoirMessage();

        if (message == null) {
            sessionActive = false;
            return null;
        }

        if (message.getTypeAction() == GameMessage.ActionType.DECONNEXION
                || message.getTypeAction() == GameMessage.ActionType.FIN_PARTIE) {
            sessionActive = false;
        }

        return message;
    }

    /**
     * Applique les deux actions reçues et les relaie aux joueurs
     */
    private void appliquerCoupleActions(GameMessage actionJoueur1, GameMessage actionJoueur2) {
        // Ici on pourrait instancier/brancher un Combat ou une logique serveur pour déterminer l'ordre.
        // Pour l'instant on relaie les actions aux deux joueurs afin qu'ils appliquent localement l'ordre d'exécution.
        Combat.gereOrdreExecutionActions(actionJoueur1.getDonnees(), actionJoueur2.getDonnees());

        // Relayer l'action du joueur 1 au joueur 2 et inversement
        client2.envoyerMessage(actionJoueur1);
        client1.envoyerMessage(actionJoueur2);
    }
    
    /**
     * Envoie un message aux deux clients
     */
    private void envoyerMessageAuxDeux(GameMessage message) {
        client1.envoyerMessage(message);
        client2.envoyerMessage(message);
    }
    
    /**
     * Termine la session et ferme les connexions
     */
    private void terminerSession() {
        sessionActive = false;
        
        // Envoyer un message de fin aux deux clients
        GameMessage messageFin = new GameMessage(GameMessage.ActionType.DECONNEXION, 
                                                 "Serveur", "Serveur",
                                                 "Session terminée");
        try {
            client1.envoyerMessage(messageFin);
        } catch (Exception e) {
            System.out.println("[SESSION] Impossible d'envoyer le message de fin au client 1");
        }
        
        try {
            client2.envoyerMessage(messageFin);
        } catch (Exception e) {
            System.out.println("[SESSION] Impossible d'envoyer le message de fin au client 2");
        }
        
        // Fermer les connexions
        client1.fermer();
        client2.fermer();
        
        System.out.println("[SESSION] Session terminée");
    }
}

