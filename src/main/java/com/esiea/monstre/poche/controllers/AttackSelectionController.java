package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.views.AttackSelectionView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller pour la sélection des attaques.
 */
public class AttackSelectionController {
    
    private AttackSelectionView view;
    private NavigationCallback navigationCallback;
    private Joueur joueur;
    private Runnable onComplete;
    
    public AttackSelectionController(AttackSelectionView view, NavigationCallback navigationCallback, Joueur joueur) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        this.joueur = joueur;
        initializeEventHandlers();
    }
    
    public AttackSelectionController(AttackSelectionView view, NavigationCallback navigationCallback, Joueur joueur, Runnable onComplete) {
        this.view = view;
        this.navigationCallback = navigationCallback;
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
        navigationCallback.showMainMenu();
    }
    
    /**
     * Gère la validation de la sélection des attaques.
     */
    private void handleValidateSelection() {
        Monstre currentMonstre = joueur.getMonstreActuel();

        List<Attaque> selectedAttacks = view.getSelectedAttacks();
        currentMonstre.setAttaques(new ArrayList<>(selectedAttacks));

        System.out.println(joueur.getNomJoueur() + " a sélectionné " + selectedAttacks.size() + " attaques pour " + currentMonstre.getNomMonstre());

        // Passer au monstre suivant
        int currentMonsterIndex = joueur.getMonstres().indexOf(currentMonstre) + 1;
        if (currentMonsterIndex >= joueur.getMonstres().size()) {
            // Dernier monstre: notifier la fin de sélection si un callback est fourni
            if (onComplete != null) {
                onComplete.run();
            }
        } else {
            // Continuer avec le monstre suivant, en conservant le callback
            joueur.setMonstreActuel(joueur.getMonstres().get(currentMonsterIndex));
            if (onComplete != null) {
                navigationCallback.showAttackSelectionPlayer(joueur, onComplete);
            } else {
                navigationCallback.showAttackSelectionPlayer(joueur);
            }
        }
    }
}
