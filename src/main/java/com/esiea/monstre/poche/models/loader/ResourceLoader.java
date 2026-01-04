package com.esiea.monstre.poche.models.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;



public abstract class ResourceLoader<T> {

    protected String nomFichier;
    protected List<T> ressources;
    protected Map<String, String> erreurs;

    public ResourceLoader(String nomFichier) {
        this.nomFichier = nomFichier;
        this.ressources = new ArrayList<>();
        this.erreurs = new HashMap<>();
    }

    /**
     * Point d'entrée principal
     */
    public boolean charger() {
        erreurs.clear();
        ressources.clear();

        try {
            chargerRessources();
            return erreurs.isEmpty();
        } catch (ParseException e) {
            erreurs.put("Parsing", e.getMessage());
            System.err.println("Erreur parsing : " + e.getMessage());
            return false;
        }
    }

    public List<T> getRessources() {
        return new ArrayList<>(ressources);
    }

    public Map<String, String> getErreurs() {
        return new HashMap<>(erreurs);
    }

    public int getNombreRessources() {
        return ressources.size();
    }

    protected BufferedReader ouvrirResourcesReader() throws IOException {
        var stream = getClass().getClassLoader().getResourceAsStream(nomFichier);

        if (stream == null) {
            throw new IOException("Ressource introuvable dans le dossier resources du projet : " + nomFichier);
        }

        return new BufferedReader(new InputStreamReader(stream));
    }

    public abstract T getRessourceParNom(String nom);

    protected abstract void chargerRessources() throws ParseException;

    /**
     * Exception personnalisée
     */
    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
