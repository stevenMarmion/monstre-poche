package com.esiea.monstre.poche.online;

import java.io.*;
import java.net.Socket;

/**
 * Classe client TCP pour se connecter au serveur et jouer en ligne.
 * Gère la communication bidirectionnelle avec le serveur.
 */
public class Client {
    private Socket socket;
    private String adresseServeur;
    private int portServeur;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean isConnected;
    private String idJoueur;
    private static final int PORT_DEFAULT = 5555;
    private static final String ADRESSE_DEFAULT = "localhost";
    
    public Client() {
        this(ADRESSE_DEFAULT, PORT_DEFAULT);
    }
    
    public Client(String adresseServeur, int portServeur) {
        this.adresseServeur = adresseServeur;
        this.portServeur = portServeur;
        this.isConnected = false;
    }
    
    /**
     * Se connecte au serveur
     */
    public boolean connecter() {
        try {
            socket = new Socket(adresseServeur, portServeur);
            
            // Initialiser les flux (OutputStream d'abord)
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            isConnected = true;
            System.out.println("[CLIENT] Connecté au serveur " + adresseServeur + ":" + portServeur);
            
            return true;
        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible de se connecter au serveur : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Envoie un message au serveur
     */
    public synchronized void envoyerMessage(GameMessage message) {
        if (!isConnected) {
            System.err.println("[ERREUR] Pas de connexion établie");
            return;
        }
        
        try {
            message.setIdJoueur(idJoueur);
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            System.out.println("[CLIENT] Message envoyé : " + message.getTypeAction());
        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible d'envoyer le message : " + e.getMessage());
            isConnected = false;
        }
    }
    
    /**
     * Reçoit un message du serveur (bloquant)
     */
    public GameMessage recevoirMessage() {
        if (!isConnected) {
            return null;
        }
        
        try {
            GameMessage message = (GameMessage) objectInputStream.readObject();
            System.out.println("[CLIENT] Message reçu : " + message.getTypeAction());
            return message;
        } catch (EOFException e) {
            System.out.println("[INFO] Connexion fermée par le serveur");
            isConnected = false;
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERREUR] Erreur lors de la réception du message : " + e.getMessage());
            isConnected = false;
            return null;
        }
    }
    
    /**
     * Lance un thread pour recevoir les messages du serveur
     */
    public void demarrerReceptionMessages(MessageListener listener) {
        Thread receptionThread = new Thread(() -> {
            while (isConnected) {
                GameMessage message = recevoirMessage();
                if (message != null && listener != null) {
                    listener.onMessageReceived(message);
                }
            }
        });
        receptionThread.setDaemon(true);
        receptionThread.start();
    }
    
    /**
     * Se déconnecte du serveur
     */
    public void deconnecter() {
        try {
            isConnected = false;
            
            // Envoyer un message de déconnexion
            GameMessage messageFin = new GameMessage(GameMessage.ActionType.DECONNEXION, 
                                                    idJoueur, "Client");
            try {
                envoyerMessage(messageFin);
            } catch (Exception e) {
                // Ignorer les erreurs lors de l'envoi du dernier message
            }
            
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("[CLIENT] Déconnecté du serveur");
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la déconnexion : " + e.getMessage());
        }
    }
    
    // Getters et setters
    public boolean isConnected() {
        return isConnected;
    }
    
    public String getIdJoueur() {
        return idJoueur;
    }
    
    public void setIdJoueur(String idJoueur) {
        this.idJoueur = idJoueur;
    }
    
    public String getAdresseServeur() {
        return adresseServeur;
    }
    
    public int getPortServeur() {
        return portServeur;
    }
    
    /**
     * Interface pour écouter les messages reçus
     */
    public interface MessageListener {
        void onMessageReceived(GameMessage message);
    }
}
