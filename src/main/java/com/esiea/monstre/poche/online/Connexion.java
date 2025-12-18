package com.esiea.monstre.poche.online;

/**
 * Classe utilitaire pour gérer les connexions au serveur de jeu en ligne.
 * Fournit des méthodes statiques pour se connecter et créer des clients.
 */
public class Connexion {
    private static final String ADRESSE_SERVEUR_DEFAULT = "localhost";
    private static final int PORT_SERVEUR_DEFAULT = 5555;
    
    /**
     * Se connecte au serveur par défaut (localhost:5555)
     */
    public static Client connecterAuServeur() {
        return connecterAuServeur(ADRESSE_SERVEUR_DEFAULT, PORT_SERVEUR_DEFAULT);
    }
    
    /**
     * Se connecte à un serveur spécifique
     */
    public static Client connecterAuServeur(String adresse, int port) {
        Client client = new Client(adresse, port);
        
        if (client.connecter()) {
            System.out.println("[CONNEXION] Connexion réussie au serveur " + adresse + ":" + port);
            return client;
        } else {
            System.err.println("[CONNEXION] Échec de la connexion au serveur");
            return null;
        }
    }
    
    /**
     * Vérifie la disponibilité du serveur
     */
    public static boolean verifierServeur(String adresse, int port) {
        Client client = new Client(adresse, port);
        boolean connecte = client.connecter();
        
        if (connecte) {
            client.deconnecter();
        }
        
        return connecte;
    }
    
    /**
     * Affiche les informations de connexion
     */
    public static void afficherInfoConnexion(Client client) {
        if (client != null && client.isConnected()) {
            System.out.println("========================================");
            System.out.println("Connexion établie");
            System.out.println("Serveur: " + client.getAdresseServeur());
            System.out.println("Port: " + client.getPortServeur());
            System.out.println("ID Joueur: " + client.getIdJoueur());
            System.out.println("========================================");
        } else {
            System.out.println("[INFO] Aucune connexion active");
        }
    }
}

