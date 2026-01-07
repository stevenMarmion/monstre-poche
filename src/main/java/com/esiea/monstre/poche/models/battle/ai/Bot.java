package com.esiea.monstre.poche.models.battle.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.items.Objet;

/**
 * Classe Bot représentant un adversaire IA controllé par l'ordinateur.
 * Le bot joue automatiquement sans intervention de l'utilisateur.
 */
public class Bot extends Joueur {
    private Random random;
    private int niveauDifficulte;

    private static final int NIVEAU_FACILE = 1;
    private static final int NIVEAU_MOYEN = 2;
    private static final int NIVEAU_DIFFICILE = 3;

    public Bot(String nomBot, int niveauDifficulte) {
        super(nomBot);
        this.random = new Random();
        this.niveauDifficulte = Math.min(Math.max(niveauDifficulte, 1), 3);
    }

    public Bot(String nomBot) {
        this(nomBot, NIVEAU_MOYEN);
    }

    /**
     * Charge automatiquement les monstres du Bot
     */
    public void chargerMonstresAutomatiquement(GameResourcesFactory resourceFactory) {
        List<Monstre> monstresDisponibles = resourceFactory.getTousLesMonstres();

        if (monstresDisponibles.isEmpty()) {
            CombatLogger.log("[ERREUR] Aucun monstre disponible pour le bot.");
            return;
        }

        ArrayList<Monstre> monstresSelectionnes = new ArrayList<>();
        ArrayList<Integer> indicesUtilises = new ArrayList<>();

        while (monstresSelectionnes.size() < Joueur.TAILLE_EQUIPE_MAX && monstresSelectionnes.size() < monstresDisponibles.size()) {
            int indexAleatoire = this.random.nextInt(monstresDisponibles.size());

            if (!indicesUtilises.contains(indexAleatoire)) {
                indicesUtilises.add(indexAleatoire);
                Monstre monstreOriginal = monstresDisponibles.get(indexAleatoire);
                Monstre monstreClone = monstreOriginal.copyOf();
                monstresSelectionnes.add(monstreClone);
                this.ajouterMonstre(monstreClone);
            }
        }

        if (Boolean.FALSE.equals(monstresSelectionnes.isEmpty())) {
            this.setMonstreActuel(monstresSelectionnes.get(0));
            CombatLogger.log("[BOT] " + this.getNomJoueur() + " a selectionne " + monstresSelectionnes.size() + " monstres.");
        }
    }

    /**
     * Charge automatiquement les attaques pour tous les monstres du Bot
     * 
     */
    public void chargerAttaquesAutomatiquement(GameResourcesFactory resourceFactory) {
        List<Attaque> attaquesDisponibles = resourceFactory.getToutesLesAttaques();

        if (attaquesDisponibles.isEmpty()) {
            CombatLogger.log("[ERREUR] Aucune attaque disponible pour le bot.");
            return;
        }

        for (Monstre monstre : this.getMonstres()) {
            ArrayList<Integer> indicesUtilises = new ArrayList<>();

            while (monstre.getAttaques().size() < Joueur.NOMBRE_ATTAQUES_PAR_MONSTRE && monstre.getAttaques().size() < attaquesDisponibles.size()) {
                int indexAleatoire = this.random.nextInt(attaquesDisponibles.size());

                if (!indicesUtilises.contains(indexAleatoire)) {
                    Attaque attaque = attaquesDisponibles.get(indexAleatoire);

                    // Vérifier que l'attaque est compatible avec le type du monstre
                    if (monstre.getTypeMonstre().getLabelType().equals(attaque.getTypeAttaque().getLabelType())) {
                        indicesUtilises.add(indexAleatoire);
                        monstre.ajouterAttaque(attaque);
                    }
                }
            }
        }

        CombatLogger.log("[BOT] " + this.getNomJoueur() + " a configure les attaques de ses monstres.");
    }

    /**
     * Charge automatiquement les objets pour tous les monstres du Bot
     */
    public void chargerObjetsAutomatiquement(GameResourcesFactory resourceFactory) {
        List<Objet> objetsDisponibles = resourceFactory.getTousLesObjets();

        if (objetsDisponibles.isEmpty()){
            System.out.println("Aucun objets disponibles pour le bot.");
        }
        while(this.getObjets().size() < Joueur.TAILLE_INVENTAIRE_MAX
            && this.getObjets().size() < objetsDisponibles.size()){

            int indexAleatoire = this.random.nextInt(objetsDisponibles.size());
            Objet objet = objetsDisponibles.get(indexAleatoire);

            this.getObjets().add(objet);
        }

        System.out.println("[BOT] " + this.getNomJoueur() + " a configuré sa liste d'objets.");
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

        switch (this.niveauDifficulte) {
            case NIVEAU_FACILE:
                return this.choisirActionFacile(monstreActuel);
            case NIVEAU_MOYEN:
                return this.choisirActionMoyen(monstreActuel, monstreAdversaire);
            case NIVEAU_DIFFICILE:
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
            if (attaque.getTypeAttaque().estFortContre().equals(monstre.getTypeMonstre().getLabelType())) {
                attaquesAvantageuses.add(attaque);
            }
        }

        if (!attaquesAvantageuses.isEmpty()) {
            return attaquesAvantageuses.get(this.random.nextInt(attaquesAvantageuses.size()));
        }

        return this.choisirActionMoyen(monstre, monstreAdversaire);
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
}
