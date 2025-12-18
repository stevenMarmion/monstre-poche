package com.esiea.monstre.poche.online;

import java.io.*;
import java.net.Socket;

/**
 * Classe gérant la communication avec un client connecté.
 * Chaque client connecté au serveur a un ClientHandler dédié.
 */
public class ClientHandler {
    private Socket socket;
    private String idJoueur;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean isConnected;
    
    public ClientHandler(Socket socket, String idJoueur) {
        this.socket = socket;
        this.idJoueur = idJoueur;
        this.isConnected = true;
        
        try {
            // L'ordre est important : d'abord OutputStream, puis InputStream
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible d'initialiser les flux pour " + idJoueur + " : " + e.getMessage());
            isConnected = false;
        }
    }
    
    /**
     * Envoie un message au client
     */
    public synchronized void envoyerMessage(GameMessage message) {
        if (!isConnected) {
            System.err.println("[ERREUR] " + idJoueur + " n'est pas connecté");
            return;
        }
        
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
            System.out.println("[ENVOI à " + idJoueur + "] " + message.getTypeAction());
        } catch (IOException e) {
            System.err.println("[ERREUR] Impossible d'envoyer le message à " + idJoueur + " : " + e.getMessage());
            isConnected = false;
        }
    }
    
    /**
     * Reçoit un message du client (bloquant)
     */
    public GameMessage recevoirMessage() {
        if (!isConnected) {
            return null;
        }
        
        try {
            GameMessage message = (GameMessage) objectInputStream.readObject();
            System.out.println("[RECU de " + idJoueur + "] " + message.getTypeAction());
            return message;
        } catch (EOFException e) {
            System.out.println("[INFO] " + idJoueur + " s'est déconnecté");
            isConnected = false;
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERREUR] Erreur lors de la réception du message de " + idJoueur + " : " + e.getMessage());
            isConnected = false;
            return null;
        }
    }
    
    /**
     * Ferme la connexion du client
     */
    public void fermer() {
        try {
            isConnected = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("[INFO] Connexion fermée pour " + idJoueur);
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
    
    // Getters
    public String getIdJoueur() {
        return idJoueur;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public Socket getSocket() {
        return socket;
    }
}
