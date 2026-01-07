package com.esiea.monstre.poche.controllers.selection;

import java.util.ArrayList;
import java.util.List;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.views.gui.selection.AttackSelectionView;

/**
 * Controller pour la sélection des attaques.
 */
public class AttackSelectionController {
    
    private AttackSelectionView view;
    private INavigationCallback INavigationCallback;
    private Joueur joueur;
    private Runnable onComplete;
    
    public AttackSelectionController(AttackSelectionView view, INavigationCallback INavigationCallback, Joueur joueur) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
        this.joueur = joueur;
        initializeEventHandlers();
    }
    
    public AttackSelectionController(AttackSelectionView view, INavigationCallback INavigationCallback, Joueur joueur, Runnable onComplete) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
        this.joueur = joueur;
        this.onComplete = onComplete;
        initializeEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'événements.
     */
    private void initializeEventHandlers() {
        view.getBtnBackToMenu().setOnAction(e -> handleBackToMenu());
        view.getBtnValidate().setOnAction(e -> handleValidateSelection());
    }
    
    /**
     * Gère le clic sur le bouton "Revenir au menu".
     */
    private void handleBackToMenu() {
        INavigationCallback.showMainMenu();
    }
    
    /**
     * Gère la validation de la sélection des attaques.
     */
    private void handleValidateSelection() {
        Monstre currentMonstre = joueur.getMonstreActuel();

        List<Attaque> selectedAttacks = view.getSelectedAttacks();
        currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));

        CombatLogger.info(joueur.getNomJoueur() + " a sélectionné " + selectedAttacks.size() + " attaques pour " + currentMonstre.getNomMonstre());

        // Passer au monstre suivant
        int currentMonsterIndex = joueur.getMonstres().indexOf(currentMonstre) + 1;
        if (currentMonsterIndex >= joueur.getMonstres().size()) {
            // Dernier monstre: passer à la sélection des objets
            INavigationCallback.showObjectSelectionPlayer(joueur, onComplete);
        } else {
            // Continuer avec le monstre suivant, en conservant le callback
            joueur.setMonstreActuel(joueur.getMonstres().get(currentMonsterIndex));
            INavigationCallback.showAttackSelectionPlayer(joueur, onComplete);
        }
    }
}
