package com.esiea.monstre.poche.models.loader;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.affinites.Eau;
import com.esiea.monstre.poche.models.affinites.Foudre;
import com.esiea.monstre.poche.models.affinites.Type;
import com.esiea.monstre.poche.models.affinites.utils.AffinitesUtils;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.models.inventaire.medicaments.MedicamentAntiBrulure;
import com.esiea.monstre.poche.models.inventaire.medicaments.MedicamentAntiParalysie;
import com.esiea.monstre.poche.models.inventaire.medicaments.MedicamentAntiPoison;
import com.esiea.monstre.poche.models.inventaire.potions.PotionDegat;
import com.esiea.monstre.poche.models.inventaire.potions.PotionSante;
import com.esiea.monstre.poche.models.inventaire.potions.PotionVitesse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


/**
 * Classe  permettant de charger dans l'application les données à disposition, définies dans des fichiers texte présents dans les ressources du projet
 */
public class GameResourcesLoader {

    private static final String NOM_FICHIER_RESSOURCE_MONSTRES = "monsters.txt";
    private static final String NOM_FICHIER_RESSOURCE_ATTAQUES = "attacks.txt";
    private static final String NOM_FICHIER_RESSOURCE_OBJETS = "objects.txt";

    private final Random randomNumberGenerator;

    private List<Monstre> monstresDeBase;
    private List<Attaque> attaquesDeBase;
    private List<Objet> objetsDeBase;


    public GameResourcesLoader() {
        this.randomNumberGenerator = new Random();
        try {
            this.monstresDeBase = chargerRessourceMonstres();
            this.attaquesDeBase = chargerRessourceAttaques();
            this.objetsDeBase = chargerRessourceObjets();
        } catch (ParseException e) {
            System.out.printf("Erreur durant le parsing des ressources : %s", e.getMessage());
        }
    }

    public List<Monstre> getListeMonstresDeBase(){
        return this.monstresDeBase;
    }

    public List<Attaque> getListeAttaquesDeBase(){
        return this.attaquesDeBase;
    }

    public List<Objet> getListeObjetsDeBase(){
        return this.objetsDeBase;
    }

    protected BufferedReader ouvrirResourcesReader(String nomFichier) throws IOException {
        var stream = getClass().getClassLoader().getResourceAsStream(nomFichier);

        if (stream == null) {
            throw new IOException("Ressource introuvable dans le dossier resources du projet : " + nomFichier);
        }

        return new BufferedReader(new java.io.InputStreamReader(stream));
    }


    private List<Monstre> chargerRessourceMonstres() throws ParseException {
        List<Monstre> monstresDeBase = new ArrayList<>();

        try (BufferedReader reader = ouvrirResourcesReader(NOM_FICHIER_RESSOURCE_MONSTRES)) {

            String ligne;
            int numeroLigne = 0;

            String nom = null;
            Type type = null;

            Integer hpMin = null, hpMax = null;
            Integer atkMin = null, atkMax = null;
            Integer defMin = null, defMax = null;
            Integer vitMin = null, vitMax = null;
            
            // Probabilités spéciales pour les types
            Double probaParalysie = null;  // Pour Foudre
            Double probaFlood = null;       // Pour Eau
            Double probaFall = null;        // Pour Eau

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
                        probaParalysie = null;
                        probaFlood = null;
                        probaFall = null;
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
                        
                        // Appliquer les probabilités spéciales au type
                        if (type instanceof Foudre && probaParalysie != null) {
                            ((Foudre) type).setChanceParalysie(probaParalysie);
                        }
                        if (type instanceof Eau) {
                            if (probaFlood != null) {
                                ((Eau) type).setProbabiliteInnondation(probaFlood);
                            }
                            if (probaFall != null) {
                                ((Eau) type).setProbabiliteFaireChuter(probaFall);
                            }
                        }

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

                        monstresDeBase.add(monstre);
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

                            // Effets spéciaux pour types spéciaux
                            case "Paralysis":
                                probaParalysie = Double.parseDouble(parts[1]);
                                if (probaParalysie < 0 || probaParalysie > 1) {
                                    throw new ParseException("Probabilité Paralysis invalide ligne " + numeroLigne);
                                }
                                break;
                                
                            case "Flood":
                                probaFlood = Double.parseDouble(parts[1]);
                                if (probaFlood < 0 || probaFlood > 1) {
                                    throw new ParseException("Probabilité Flood invalide ligne " + numeroLigne);
                                }
                                break;
                                
                            case "Fall":
                                probaFall = Double.parseDouble(parts[1]);
                                if (probaFall < 0 || probaFall > 1) {
                                    throw new ParseException("Probabilité Fall invalide ligne " + numeroLigne);
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

        } catch (ParseException | IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Erreur lecture fichier monstres", e);
        }
        return monstresDeBase;
    }

    private List<Attaque> chargerRessourceAttaques() throws ParseException {
        List<Attaque> attaquesDeBase = new ArrayList<>();

        try (BufferedReader reader = ouvrirResourcesReader(NOM_FICHIER_RESSOURCE_ATTAQUES)) {

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

                        attaquesDeBase.add(new Attaque(nom, nbUse, power, fail, type));
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
        return attaquesDeBase;
    }

    private List<Objet> chargerRessourceObjets() throws ParseException {
        List<Objet> objetsDeBase = new ArrayList<>();

        try (BufferedReader reader = ouvrirResourcesReader(NOM_FICHIER_RESSOURCE_OBJETS)) {

            String ligne;
            int numeroLigne = 0;

            boolean dansPotion = false;
            boolean dansMedicament = false;

            String nom = null;
            String type = null;
            Integer value = null;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty() || ligne.startsWith("#")) continue;

                switch (ligne) {

                    // POTION
                    case "Potion":
                        if (dansPotion || dansMedicament)
                            throw new ParseException("Bloc imbriqué ligne " + numeroLigne);

                        dansPotion = true;
                        nom = null;
                        type = null;
                        value = null;
                        break;

                    case "EndPotion":
                        if (!dansPotion || nom == null || type == null || value == null)
                            throw new ParseException("Potion incomplète ligne " + numeroLigne);

                        objetsDeBase.add(creerPotion(nom, type, value));
                        dansPotion = false;
                        break;

                    // MEDICAMENT
                    case "Medicament":
                        if (dansPotion || dansMedicament)
                            throw new ParseException("Bloc imbriqué ligne " + numeroLigne);

                        dansMedicament = true;
                        nom = null;
                        type = null;
                        break;

                    case "EndMedicament":
                        if (!dansMedicament || nom == null || type == null)
                            throw new ParseException("Medicament incomplet ligne " + numeroLigne);

                        objetsDeBase.add(creerMedicament(nom, type));
                        dansMedicament = false;
                        break;

                    // CONTENU
                    default:
                        String[] parts = ligne.split("\\s+", 2);
                        if (parts.length != 2)
                            throw new ParseException("Format invalide ligne " + numeroLigne);

                        switch (parts[0]) {
                            case "Name":
                                nom = parts[1];
                                break;
                            case "Type":
                                type = parts[1].toLowerCase();
                                break;
                            case "Value":
                                value = Integer.parseInt(parts[1]);
                                break;
                            default:
                                throw new ParseException("Clé inconnue '" + parts[0] + "' ligne " + numeroLigne);
                        }
                }
            }

            if (dansPotion || dansMedicament)
                throw new ParseException("Fichier terminé sans EndPotion ou EndMedicament");

        } catch (IOException | NumberFormatException e) {
            throw new ParseException("Erreur lecture fichier objets", e);
        }
        return objetsDeBase;
    }

    private int valeurAleatoire(int min, int max) {
        return min + randomNumberGenerator.nextInt(max - min + 1);
    }

    private Objet creerPotion(String nom, String type, int value) throws ParseException {
        return switch (type) {
            case "soin" -> new PotionSante(nom, value);
            case "attaque" -> new PotionDegat(nom, value);
            case "vitesse" -> new PotionVitesse(nom, value);
            default -> throw new ParseException("Type de potion inconnu : " + type);
        };
    }

    private Objet creerMedicament(String nom, String type) throws ParseException {
        return switch (type) {
            case "brulure" -> new MedicamentAntiBrulure(nom);
            case "paralysie" -> new MedicamentAntiParalysie(nom);
            case "poison" -> new MedicamentAntiPoison(nom);
            //case "inondation":
            //  return new MedicamentAntiInondation(nom);
            default -> throw new ParseException("Type de medicament inconnu : " + type);
        };
    }

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
