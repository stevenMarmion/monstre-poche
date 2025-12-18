package com.esiea.monstre.poche.online;

import java.io.Serializable;

/**
 * Classe représentant un message d'action de jeu envoyé entre clients et serveur.
 * Implémente Serializable pour pouvoir être sérialisée sur le réseau.
 */
public class GameMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum ActionType {
        MONSTRE_SELECTIONNE,      // Sélection d'un monstre
        ATTAQUE_CHOISIE,          // Choix d'une attaque
        MONSTRE_CHANGE,           // Changement de monstre
        OBJET_UTILISE,            // Utilisation d'un objet
        JOUEUR_PRET,              // Joueur prêt à commencer
        FIN_PARTIE,               // Fin du combat
        CONNEXION,                // Connexion établie
        SYNCHRONISATION,          // Demande de synchronisation
        ERREUR,                   // Message d'erreur
        DECONNEXION              // Déconnexion
    }
    
    private ActionType typeAction;
    private String idJoueur;
    private String nomJoueur;
    private Object donnees;      // Les données de l'action (nom du monstre, attaque, etc)
    private long timestamp;      // Horodatage du message
    private String message;      // Message texte optionnel
    
    public GameMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public GameMessage(ActionType typeAction, String idJoueur, String nomJoueur) {
        this();
        this.typeAction = typeAction;
        this.idJoueur = idJoueur;
        this.nomJoueur = nomJoueur;
    }
    
    public GameMessage(ActionType typeAction, String idJoueur, String nomJoueur, Object donnees) {
        this(typeAction, idJoueur, nomJoueur);
        this.donnees = donnees;
    }
    
    // Getters et setters
    public ActionType getTypeAction() {
        return typeAction;
    }
    
    public void setTypeAction(ActionType typeAction) {
        this.typeAction = typeAction;
    }
    
    public String getIdJoueur() {
        return idJoueur;
    }
    
    public void setIdJoueur(String idJoueur) {
        this.idJoueur = idJoueur;
    }
    
    public String getNomJoueur() {
        return nomJoueur;
    }
    
    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }
    
    public Object getDonnees() {
        return donnees;
    }
    
    public void setDonnees(Object donnees) {
        this.donnees = donnees;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "GameMessage [typeAction=" + typeAction + ", idJoueur=" + idJoueur + 
               ", nomJoueur=" + nomJoueur + ", donnees=" + donnees + 
               ", timestamp=" + timestamp + ", message=" + message + "]";
    }
}
