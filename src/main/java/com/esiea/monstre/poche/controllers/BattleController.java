package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.entites.Terrain;
import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.views.BattleView;
import java.util.List;

/**
 * Controller pour la gestion du système de combat.
 * Gère les tours, les actions et la logique du combat.
 */
public class BattleController {
    private BattleView view;
    private NavigationCallback navigationCallback;
    private int player1CurrentIndex;
    private int player2CurrentIndex;
    private boolean isPlayer1Turn;
    private Terrain terrain;
    
    public BattleController(BattleView view, NavigationCallback navigationCallback) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        this.player1CurrentIndex = 0;
        this.player2CurrentIndex = 0;
        this.terrain = new Terrain("Arène", new Asseche());
        
        // Déterminer qui commence (celui avec la plus haute vitesse)
        this.isPlayer1Turn = view.getJoueur1().getMonstres().get(0).getVitesse() >= view.getJoueur2().getMonstres().get(0).getVitesse();
        
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnAttack().setOnAction(e -> handleAttackAction());
        view.getBtnSwitch().setOnAction(e -> handleSwitchAction());
        view.getBtnItem().setOnAction(e -> handleItemAction());
    }
    
    /**
     * Gère l'action Attaquer.
     */
    private void handleAttackAction() {
        if (isPlayer1Turn) {
            List<Attaque> attaques = view.getJoueur1().getMonstreActuel().getAttaques();
            if (attaques == null || attaques.isEmpty()) {
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " n'a pas d'attaques disponibles.");
                return;
            }
            view.displayAttackChoices(attaques, attaque -> {
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " choisit: " + attaque.getNomAttaque());
                view.getJoueur1().getMonstreActuel().attaquer(view.getJoueur2().getMonstreActuel(), terrain, attaque);
                autoSwitchIfFainted();
                view.updatePokemonDisplay();
                // Vérifier fin du combat
                checkBattleEnd();
                // Changer de tour
                isPlayer1Turn = false;
                view.setTurn(isPlayer1Turn);
            });
        } else {
            List<Attaque> attaques = view.getJoueur2().getMonstreActuel().getAttaques();
            if (attaques == null || attaques.isEmpty()) {
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " n'a pas d'attaques disponibles.");
                return;
            }
            view.displayAttackChoices(attaques, attaque -> {
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " choisit: " + attaque.getNomAttaque());
                view.getJoueur2().getMonstreActuel().attaquer(view.getJoueur1().getMonstreActuel(), terrain, attaque);
                autoSwitchIfFainted();
                view.updatePokemonDisplay();
                // Vérifier fin du combat
                checkBattleEnd();
                // Changer de tour
                isPlayer1Turn = true;
                view.setTurn(isPlayer1Turn);
            });
        }
    }

    /**
     * Gère l'action Changer: affiche les monstres disponibles et applique le changement.
     */
    private void handleSwitchAction() {
        if (isPlayer1Turn) {
            List<Monstre> candidates = view.getJoueur1().getMonstres();
            Monstre current = view.getJoueur1().getMonstreActuel();
            // Filtrer: vivants et différents du courant
            List<Monstre> available = candidates.stream()
                    .filter(m -> m.getPointsDeVie() > 0 && m != current)
                    .toList();
            view.displayMonsterChoices(available, selected -> {
                view.getJoueur1().setMonstreActuel(selected);
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " envoie " + selected.getNomMonstre() + " !");
                view.updatePokemonDisplay();
                // Le changement consomme le tour
                isPlayer1Turn = false;
                view.setTurn(isPlayer1Turn);
            });
        } else {
            List<Monstre> candidates = view.getJoueur2().getMonstres();
            Monstre current = view.getJoueur2().getMonstreActuel();
            List<Monstre> available = candidates.stream()
                    .filter(m -> m.getPointsDeVie() > 0 && m != current)
                    .toList();
            view.displayMonsterChoices(available, selected -> {
                view.getJoueur2().setMonstreActuel(selected);
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " envoie " + selected.getNomMonstre() + " !");
                view.updatePokemonDisplay();
                isPlayer1Turn = true;
                view.setTurn(isPlayer1Turn);
            });
        }
    }

    /**
     * Auto switch au premier monstre vivant si le courant est K.O.
     */
    private void autoSwitchIfFainted() {
        // Joueur 1
        if (view.getJoueur1().getMonstreActuel().getPointsDeVie() <= 0) {
            Monstre next = findFirstAlive(view.getJoueur1().getMonstres());
            if (next != null) {
                view.getJoueur1().setMonstreActuel(next);
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " envoie " + next.getNomMonstre() + " !");
            }
        }
        // Joueur 2
        if (view.getJoueur2().getMonstreActuel().getPointsDeVie() <= 0) {
            Monstre next = findFirstAlive(view.getJoueur2().getMonstres());
            if (next != null) {
                view.getJoueur2().setMonstreActuel(next);
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " envoie " + next.getNomMonstre() + " !");
            }
        }
    }

    private Monstre findFirstAlive(List<Monstre> list) {
        for (Monstre m : list) {
            if (m.getPointsDeVie() > 0) return m;
        }
        return null;
    }
    
    
    
    /**
     * Gère l'action Objet.
     */
    private void handleItemAction() {
        if (isPlayer1Turn) {
            view.updateBattleLog(view.getJoueur1().getNomJoueur() + " utilise un objet !");
            // TODO 
        } else {
            view.updateBattleLog(view.getJoueur2().getNomJoueur() + " utilise un objet !");
            // TODO
        }
    }
    
    /**
     * Exécute un tour de combat.
     */
    public void executeTurn(Object actionJoueur1, Object actionJoueur2) {
        // Déterminer l'ordre d'action basé sur la vitesse
        int player1Speed = view.getJoueur1().getMonstreActuel().getVitesse();
        int player2Speed = view.getJoueur2().getMonstreActuel().getVitesse();
        
        if (player1Speed >= player2Speed) {
            view.getJoueur1().getMonstreActuel().attaquer(view.getJoueur2().getMonstreActuel(), terrain, (Attaque) actionJoueur1);
            view.getJoueur2().getMonstreActuel().attaquer(view.getJoueur1().getMonstreActuel(), terrain, (Attaque) actionJoueur2);

            if (view.getJoueur1().getMonstreActuel().getPointsDeVie() == 0) {
                view.getJoueur1().switchMonstreActuelAuto();
            } else if (view.getJoueur2().getMonstreActuel().getPointsDeVie() == 0) {
                view.getJoueur2().switchMonstreActuelAuto();
            }
        } else {
            view.getJoueur2().getMonstreActuel().attaquer(view.getJoueur1().getMonstreActuel(), terrain, (Attaque) actionJoueur2);
            view.getJoueur1().getMonstreActuel().attaquer(view.getJoueur2().getMonstreActuel(), terrain, (Attaque) actionJoueur1);

            if (view.getJoueur1().getMonstreActuel().getPointsDeVie() == 0) {
                view.getJoueur1().switchMonstreActuelAuto();
            } else if (view.getJoueur2().getMonstreActuel().getPointsDeVie() == 0) {
                view.getJoueur2().switchMonstreActuelAuto();
            }
        }
        
        view.updatePokemonDisplay();
        
        // Vérifier la fin du combat
        checkBattleEnd();
    }
    
    /**
     * Vérifie si le combat est terminé.
     */
    private void checkBattleEnd() {
        // Vérifier si tous les Pokémon d'un joueur sont vaincus
        boolean player1Defeated = view.getJoueur1().getMonstres().stream()
                .allMatch(m -> m.getPointsDeVie() <= 0);
        boolean player2Defeated = view.getJoueur2().getMonstres().stream()
                .allMatch(m -> m.getPointsDeVie() <= 0);
        
        if (player1Defeated) {
            view.updateBattleLog(view.getJoueur2().getNomJoueur() + " a remporté le combat !");
            navigationCallback.showMainMenu();
        } else if (player2Defeated) {
            view.updateBattleLog(view.getJoueur1().getNomJoueur() + " a remporté le combat !");
            navigationCallback.showMainMenu();
        }
    }
    
    /**
     * Change le Pokémon actif du joueur spécifié.
     */
    public void switchPokemon(boolean isPlayer1, int newIndex) {
        if (isPlayer1) {
            if (newIndex >= 0 && newIndex < view.getJoueur1().getMonstres().size()) {
                player1CurrentIndex = newIndex;
                view.getJoueur1().setMonstreActuel(view.getJoueur1().getMonstres().get(newIndex));
                view.updatePokemonDisplay();
            }
        } else {
            if (newIndex >= 0 && newIndex < view.getJoueur2().getMonstres().size()) {
                player2CurrentIndex = newIndex;
                view.getJoueur2().setMonstreActuel(view.getJoueur2().getMonstres().get(newIndex));
                view.updatePokemonDisplay();
            }
        }
    }
    
    // Getters
    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }
    
    public void setPlayer1Turn(boolean player1Turn) {
        isPlayer1Turn = player1Turn;
    }
    
    public int getPlayer1CurrentIndex() {
        return player1CurrentIndex;
    }
    
    public int getPlayer2CurrentIndex() {
        return player2CurrentIndex;
    }
}
