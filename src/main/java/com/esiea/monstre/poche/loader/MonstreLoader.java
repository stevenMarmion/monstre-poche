package com.esiea.monstre.poche.loader;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.affinites.Type;
import com.esiea.monstre.poche.affinites.utils.AffinitesUtils;
import com.esiea.monstre.poche.entites.Monstre;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MonstreLoader extends ResourceLoader<Monstre> {

    private final Random random = new Random();

    public MonstreLoader(String nomFichier) {
        super(nomFichier);
    }

    @Override
    protected void chargerRessources() throws ParseException {

        try (BufferedReader reader = ouvrirResourcesReader()) {

            String ligne;
            int numeroLigne = 0;

            String nom = null;
            Type type = null;

            Integer hpMin = null, hpMax = null;
            Integer atkMin = null, atkMax = null;
            Integer defMin = null, defMax = null;
            Integer vitMin = null, vitMax = null;

            boolean dansMonster = false;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty() || ligne.startsWith("#")) continue;

                switch (ligne) {

                    case "Monster":
                        if (dansMonster) {
                            throw new ParseException("Monster imbriqué ligne " + numeroLigne);
                        }
                        dansMonster = true;

                        nom = null;
                        type = null;
                        hpMin = hpMax = null;
                        atkMin = atkMax = null;
                        defMin = defMax = null;
                        vitMin = vitMax = null;
                        break;

                    case "EndMonster":
                        if (!dansMonster) {
                            throw new ParseException("EndMonster sans Monster ligne " + numeroLigne);
                        }

                        if (nom == null || type == null ||
                                hpMin == null || atkMin == null ||
                                defMin == null || vitMin == null) {
                            throw new ParseException("Champ manquant pour le monstre ligne " + numeroLigne);
                        }

                        int hp = valeurAleatoire(hpMin, hpMax);
                        int attaque = valeurAleatoire(atkMin, atkMax);
                        int defense = valeurAleatoire(defMin, defMax);
                        int vitesse = valeurAleatoire(vitMin, vitMax);

                        Monstre monstre = new Monstre(
                                nom,
                                hp,
                                attaque,
                                defense,
                                vitesse,
                                new ArrayList<>(),
                                type
                        );
                        monstre.setPointsDeVieMax(hp);

                        ressources.add(monstre);
                        dansMonster = false;
                        break;

                    default:
                        if (!dansMonster) {
                            throw new ParseException("Ligne hors Monster ligne " + numeroLigne);
                        }

                        String[] parts = ligne.split("\\s+");
                        String cle = parts[0];

                        switch (cle) {
                            case "Name":
                                nom = parts[1];
                                break;

                            case "Type":
                                type = AffinitesUtils.getTypeFromString(parts[1]);
                                break;

                            case "HP":
                                hpMin = Integer.parseInt(parts[1]);
                                hpMax = Integer.parseInt(parts[2]);
                                break;

                            case "Attack":
                                atkMin = Integer.parseInt(parts[1]);
                                atkMax = Integer.parseInt(parts[2]);
                                break;

                            case "Defense":
                                defMin = Integer.parseInt(parts[1]);
                                defMax = Integer.parseInt(parts[2]);
                                break;

                            case "Speed":
                                vitMin = Integer.parseInt(parts[1]);
                                vitMax = Integer.parseInt(parts[2]);
                                break;

                            // Effets spéciaux → validés mais non stockés ici
                            case "Paralysis":
                            case "Flood":
                            case "Fall":
                                double proba = Double.parseDouble(parts[1]);
                                if (proba < 0 || proba > 1) {
                                    throw new ParseException("Probabilité invalide ligne " + numeroLigne);
                                }
                                break;

                            default:
                                throw new ParseException("Clé inconnue '" + cle + "' ligne " + numeroLigne);
                        }
                }
            }

            if (dansMonster) {
                throw new ParseException("Fichier terminé sans EndMonster");
            }

        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Erreur lecture fichier monstres", e);
        }
    }

    private int valeurAleatoire(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    @Override
    public Monstre getRessourceParNom(String nom){
        for (Monstre monstre: this.ressources){
            if (monstre.getNomMonstre().equals(nom)){
                return monstre;
            }
        }
        return null;
    }
}
