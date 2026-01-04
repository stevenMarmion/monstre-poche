package com.esiea.monstre.poche.controllers.selection;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.views.gui.selection.MonsterSelectionView;

import java.util.List;

/**
 * Controller pour la sélection des monstres.
 */
public class MonsterSelectionController {
    
    private MonsterSelectionView view;
    private INavigationCallback INavigationCallback;
    private Joueur joueur;
    private List<Monstre> selectedMonsters;
    private Runnable onComplete;
    
    public MonsterSelectionController(MonsterSelectionView view, INavigationCallback INavigationCallback, Joueur joueur) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
        this.joueur = joueur;
        initializeEventHandlers();
    }

    public MonsterSelectionController(MonsterSelectionView view, INavigationCallback INavigationCallback, Joueur joueur, Runnable onComplete) {
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
     * Gère la validation de la sélection des monstres.
     */
    private void handleValidateSelection() {
        CombatLogger.info(joueur.getNomJoueur() + " a sélectionné " + view.getSelectedMonsters().size() + " monstres");
        
        List<Monstre> selectedMonsters = view.getSelectedMonsters();
        joueur.setMonstres(selectedMonsters);
        joueur.setMonstreActuel(selectedMonsters.get(0));

        // Passer à la sélection des attaques en conservant le callback de fin si présent
        INavigationCallback.showAttackSelectionPlayer(joueur, onComplete);
    }
    
    public List<Monstre> getSelectedMonsters() {
        return selectedMonsters;
    }
}
