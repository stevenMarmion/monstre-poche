package com.esiea.monstre.poche.loader;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.affinites.Type;
import com.esiea.monstre.poche.affinites.utils.AffinitesUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class AttaqueLoader extends ResourceLoader<Attaque> {

    public AttaqueLoader(String nomFichier) {
        super(nomFichier);
    }

    @Override
    protected void chargerRessources() throws ParseException {

        try (BufferedReader reader = ouvrirResourcesReader()) {

            String ligne;
            int numeroLigne = 0;

            String nom = null;
            Type type = null;
            int power = -1;
            int nbUse = -1;
            double fail = -1;

            boolean dansAttack = false;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty() || ligne.startsWith("#")) continue;

                switch (ligne) {

                    case "Attack":
                        if (dansAttack) {
                            throw new ParseException("Attack imbriqué ligne " + numeroLigne);
                        }
                        dansAttack = true;
                        nom = null;
                        type = null;
                        power = -1;
                        nbUse = -1;
                        fail = -1;
                        break;

                    case "EndAttack":
                        if (!dansAttack) {
                            throw new ParseException("EndAttack sans Attack ligne " + numeroLigne);
                        }
                        if (nom == null || type == null || power < 0 || nbUse < 0 || fail < 0) {
                            throw new ParseException("Champ manquant pour l'attaque ligne " + numeroLigne);
                        }

                        ressources.add(new Attaque(nom, nbUse, power, fail, type));
                        dansAttack = false;
                        break;

                    default:
                        if (!dansAttack) {
                            throw new ParseException("Ligne hors Attack ligne " + numeroLigne);
                        }

                        String[] parts = ligne.split("\\s+", 2);
                        if (parts.length != 2) {
                            throw new ParseException("Format invalide ligne " + numeroLigne);
                        }

                        String cle = parts[0];
                        String valeur = parts[1];

                        switch (cle) {
                            case "Name":
                                nom = valeur;
                                break;
                            case "Type":
                                type = AffinitesUtils.getTypeFromString(valeur);
                                break;
                            case "Power":
                                power = Integer.parseInt(valeur);
                                break;
                            case "NbUse":
                                nbUse = Integer.parseInt(valeur);
                                break;
                            case "Fail":
                                fail = Double.parseDouble(valeur);
                                if (fail < 0.0 || fail > 1.0) {
                                    throw new ParseException("Fail hors [0,1] ligne " + numeroLigne);
                                }
                                break;
                            default:
                                throw new ParseException("Clé inconnue '" + cle + "' ligne " + numeroLigne);
                        }
                }
            }

            if (dansAttack) {
                throw new ParseException("Fichier terminé sans EndAttack");
            }

        } catch (IOException | NumberFormatException e) {
            throw new ParseException("Erreur lecture fichier", e);
        }
    }

    @Override
    public Attaque getRessourceParNom(String nom) {
        return ressources.stream()
                .filter(a -> a.getNomAttaque().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }
}
