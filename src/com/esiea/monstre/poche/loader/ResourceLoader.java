package src.com.esiea.monstre.poche.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.entites.Monstre;

/**
 * Classe abstraite définissant le contrat de base pour tous les loaders de ressources
 */
public abstract class ResourceLoader<T> {
    protected String cheminFichier;
    protected List<T> ressources;
    protected Map<String, String> erreurs;

    public ResourceLoader(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        this.ressources = new ArrayList<>();
        this.erreurs = new HashMap<>();
    }

    /**
     * Charge les ressources depuis le fichier
     * @return true si le chargement a réussi, false sinon
     */
    public boolean charger() {
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            int numeroLigne = 0;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;

                if (ligne.trim().isEmpty() || ligne.trim().startsWith("#")) {
                    continue;
                }
                try {
                    T ressource = parseLigne(ligne, numeroLigne);
                    if (ressource != null) {
                        ressources.add(ressource);
                    }
                } catch (ParseException e) {
                    erreurs.put("Ligne " + numeroLigne, e.getMessage());
                    System.err.println("Erreur ligne " + numeroLigne + ": " + e.getMessage());
                }
            }

            return erreurs.isEmpty();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier " + cheminFichier + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Parse une ligne du fichier et retourne l'objet correspondant
     */
    protected abstract T parseLigne(String ligne, int numeroLigne) throws ParseException;

    /**
     * Retourne la liste des ressources chargées
     */
    public List<T> getRessources() {
        return new ArrayList<>(ressources);
    }

    /**
     * Retourne les erreurs rencontrées lors du chargement
     */
    public Map<String, String> getErreurs() {
        return new HashMap<>(erreurs);
    }

    /**
     * Retourne le nombre de ressources chargées avec succès
     */
    public int getNombreRessources() {
        return ressources.size();
    }

    public abstract T getRessourceParNom(String nom);

    /**
     * Exception personnalisée pour les erreurs de parsing
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