package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.views.MonsterSelectionView;

import java.util.List;

/**
 * Controller pour la sélection des monstres.
 */
public class MonsterSelectionController {
    
    private MonsterSelectionView view;
    private NavigationCallback navigationCallback;
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Monstre> selectedMonsters;
    private boolean isPlayer1;
    
    public MonsterSelectionController(MonsterSelectionView view, NavigationCallback navigationCallback, 
                                     Joueur joueur1, Joueur joueur2, boolean isPlayer1) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.isPlayer1 = isPlayer1;
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
        navigationCallback.showMainMenu();
    }
    
    /**
     * Gère la validation de la sélection des monstres.
     */
    private void handleValidateSelection() {
        selectedMonsters = view.getSelectedMonsters();
        System.out.println((isPlayer1 ? joueur1.getNomJoueur() : joueur2.getNomJoueur()) + " a sélectionné " + selectedMonsters.size() + " monstres");
        
        // Passer à la sélection des attaques
        navigationCallback.showAttackSelection(joueur1, joueur2, isPlayer1);
    }
    
    public List<Monstre> getSelectedMonsters() {
        return selectedMonsters;
    }
}
