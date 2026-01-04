package com.esiea.monstre.poche.chore.models.entites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esiea.monstre.poche.chore.models.affinites.Type;
import com.esiea.monstre.poche.chore.models.combats.CombatLogger;
import com.esiea.monstre.poche.chore.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.chore.models.loader.MonstreLoader;

/**
 * Classe Bot représentant un adversaire IA controllé par l'ordinateur.
 * Le bot joue automatiquement sans intervention de l'utilisateur.
 */
public class Bot extends Joueur {
    private static final int NOMBRE_MONSTRES = 3;
    private static final int NOMBRE_ATTAQUES_PAR_MONSTRE = 4;
    private Random random;
    private int niveauDifficulte; // 1 = facile, 2 = moyen, 3 = difficile

    /**
     * Constructeur du Bot
     * 
     * @param nomBot           Le nom du bot
     * @param niveauDifficulte Le niveau de difficulté (1-3)
     */
    public Bot(String nomBot, int niveauDifficulte) {
        super(nomBot);
        this.random = new Random();
        this.niveauDifficulte = Math.min(Math.max(niveauDifficulte, 1), 3);
    }

    /**
     * Constructeur du Bot avec niveau de difficulte par défaut
     * 
     * @param nomBot Le nom du bot
     */
    public Bot(String nomBot) {
        this(nomBot, 2); // Niveau moyen par défaut
    }

    /**
     * Charge automatiquement les monstres du Bot
     * 
     * @param monstreLoader Loader pour accéder aux monstres disponibles
     */
    public void chargerMonstresAutomatiquement(MonstreLoader monstreLoader) {
        List<Monstre> monstresDisponibles = monstreLoader.getRessources();

        if (monstresDisponibles.isEmpty()) {
            CombatLogger.log("[ERREUR] Aucun monstre disponible pour le bot.");
            return;
        }

        // Mélanger la liste et sélectionner les N premiers monstres
        ArrayList<Monstre> monstresSelectionnes = new ArrayList<>();
        ArrayList<Integer> indicesUtilises = new ArrayList<>();

        while (monstresSelectionnes.size() < NOMBRE_MONSTRES && monstresSelectionnes.size() < monstresDisponibles.size()) {
            int indexAleatoire = this.random.nextInt(monstresDisponibles.size());

            if (!indicesUtilises.contains(indexAleatoire)) {
                indicesUtilises.add(indexAleatoire);
                // Cloner le monstre pour éviter les conflits
                Monstre monstreOriginal = monstresDisponibles.get(indexAleatoire);
                Monstre monstreClone = this.clonnerMonstre(monstreOriginal);
                monstresSelectionnes.add(monstreClone);
                this.ajouterMonstre(monstreClone);
            }
        }

        if (!monstresSelectionnes.isEmpty()) {
            this.setMonstreActuel(monstresSelectionnes.get(0));
            CombatLogger.log("[BOT] " + this.getNomJoueur() + " a selectionne " + monstresSelectionnes.size() + " monstres.");
        }
    }

    /**
     * Charge automatiquement les attaques pour tous les monstres du Bot
     * 
     * @param attaqueLoader Loader pour accéder aux attaques disponibles
     */
    public void chargerAttaquesAutomatiquement(AttaqueLoader attaqueLoader) {
        List<Attaque> attaquesDisponibles = attaqueLoader.getRessources();

        if (attaquesDisponibles.isEmpty()) {
            CombatLogger.log("[ERREUR] Aucune attaque disponible pour le bot.");
            return;
        }

        for (Monstre monstre : this.getMonstres()) {
            ArrayList<Integer> indicesUtilises = new ArrayList<>();

            while (monstre.getAttaques().size() < NOMBRE_ATTAQUES_PAR_MONSTRE
                    && monstre.getAttaques().size() < attaquesDisponibles.size()) {
                int indexAleatoire = this.random.nextInt(attaquesDisponibles.size());

                if (!indicesUtilises.contains(indexAleatoire)) {
                    Attaque attaque = attaquesDisponibles.get(indexAleatoire);

                    // Vérifier que l'attaque est compatible avec le type du monstre
                    if (monstre.getTypeMonstre().getLabelType()
                            .equals(attaque.getTypeAttaque().getLabelType())) {
                        indicesUtilises.add(indexAleatoire);
                        monstre.ajouterAttaque(attaque);
                    }
                }
            }
        }

        CombatLogger.log("[BOT] " + this.getNomJoueur() + " a configure les attaques de ses monstres.");
    }

    /**
     * Le bot choisit automatiquement une action (attaque ou changement de monstre)
     * 
     * @param monstreAdversaire Le monstre actuel de l'adversaire
     * @return L'attaque choisie par le bot ou null
     */
    public Attaque choisirActionAutomatiquement(Monstre monstreAdversaire) {
        Monstre monstreActuel = this.getMonstreActuel();

        if (monstreActuel == null || monstreActuel.getPointsDeVie() <= 0) {
            this.changerMonstreAutomatiquement();
            return null;
        }

        // Décision basée sur la difficulté
        switch (this.niveauDifficulte) {
            case 1: // Facile
                return this.choisirActionFacile(monstreActuel);
            case 2: // Moyen
                return this.choisirActionMoyen(monstreActuel, monstreAdversaire);
            case 3: // Difficile
                return this.choisirActionDifficile(monstreActuel, monstreAdversaire);
            default:
                return this.choisirActionMoyen(monstreActuel, monstreAdversaire);
        }
    }

    /**
     * IA facile : choisit aléatoirement une attaque
     */
    private Attaque choisirActionFacile(Monstre monstre) {
        ArrayList<Attaque> attaques = monstre.getAttaques();
        if (attaques.isEmpty()) {
            return null;
        }
        return attaques.get(this.random.nextInt(attaques.size()));
    }

    /**
     * IA moyen : préfère les attaques du même type que le monstre
     */
    private Attaque choisirActionMoyen(Monstre monstre, Monstre monstreAdversaire) {
        ArrayList<Attaque> attaques = monstre.getAttaques();
        ArrayList<Attaque> attaquesOptimales = new ArrayList<>();

        // Chercher les attaques du même type
        for (Attaque attaque : attaques) {
            if (attaque.getTypeAttaque().getLabelType().equals(monstre.getTypeMonstre().getLabelType())) {
                attaquesOptimales.add(attaque);
            }
        }

        if (!attaquesOptimales.isEmpty()) {
            return attaquesOptimales.get(this.random.nextInt(attaquesOptimales.size()));
        }

        return this.choisirActionFacile(monstre);
    }

    /**
     * IA difficile : analyse les affinités de type et choisit intelligemment
     */
    private Attaque choisirActionDifficile(Monstre monstre, Monstre monstreAdversaire) {
        ArrayList<Attaque> attaques = monstre.getAttaques();
        ArrayList<Attaque> attaquesAvantageuses = new ArrayList<>();

        // Chercher les attaques qui ont un avantage de type
        for (Attaque attaque : attaques) {
            // Vérifier si l'attaque a un avantage contre le type de l'adversaire
            double multiplicateur = this.calculerMultiplicateurType(attaque.getTypeAttaque(),
                    monstreAdversaire.getTypeMonstre());

            if (multiplicateur > 1.0) {
                attaquesAvantageuses.add(attaque);
            }
        }

        if (!attaquesAvantageuses.isEmpty()) {
            return attaquesAvantageuses.get(this.random.nextInt(attaquesAvantageuses.size()));
        }

        // Sinon, utiliser l'IA moyen
        return this.choisirActionMoyen(monstre, monstreAdversaire);
    }

    /**
     * Calcule le multiplicateur de dégâts basé sur les types
     */
    private double calculerMultiplicateurType(Type typeAttaque, Type typeDefense) {
        // Cette méthode peut être améliorée avec une vraie logique d'affinités
        // Pour l'instant, retour basé sur le label du type
        if (typeAttaque.getLabelType().equals(typeDefense.getLabelType())) {
            return 1.0;
        }
        return 1.0;
    }

    /**
     * Le bot change automatiquement de monstre s'il en a un disponible
     */
    public void changerMonstreAutomatiquement() {
        Monstre monstreActuel = this.getMonstreActuel();

        for (Monstre monstre : this.getMonstres()) {
            if (monstre != monstreActuel && monstre.getPointsDeVie() > 0) {
                this.setMonstreActuel(monstre);
                return;
            }
        }
    }

    /**
     * Clone un monstre pour éviter les conflits entre joueurs
     */
    private Monstre clonnerMonstre(Monstre original) {
        ArrayList<Attaque> attaquesClonees = new ArrayList<>(original.getAttaques());
        Monstre clone = new Monstre(
                original.getNomMonstre(),
                (int) original.getPointsDeVieMax(),
                original.getAttaque(),
                original.getDefense(),
                original.getVitesse(),
                attaquesClonees,
                original.getTypeMonstre());
        return clone;
    }

    /**
     * Retourne le niveau de difficulté du bot
     */
    public int getNiveauDifficulte() {
        return this.niveauDifficulte;
    }

    /**
     * Définit le niveau de difficulté du bot
     */
    public void setNiveauDifficulte(int niveauDifficulte) {
        this.niveauDifficulte = Math.min(Math.max(niveauDifficulte, 1), 3);
    }
}
