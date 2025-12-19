package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.views.AttackSelectionView;

import java.util.List;

/**
 * Controller pour la sélection des attaques.
 */
public class AttackSelectionController {
    
    private AttackSelectionView view;
    private NavigationCallback navigationCallback;
    private Joueur joueur1;
    private Joueur joueur2;
    private boolean isPlayer1;
    private int currentMonsterIndex;
    
    public AttackSelectionController(AttackSelectionView view, NavigationCallback navigationCallback,
                                    Joueur joueur1, Joueur joueur2, boolean isPlayer1) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        this.isPlayer1 = isPlayer1;
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.currentMonsterIndex = 0;
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
        Monstre currentMonstre = view.getCurrentMonstre();
        List<Attaque> selectedAttacks = view.getSelectedAttacks();
        
        // Assigner les attaques au monstre
        currentMonstre.setAttaques(new java.util.ArrayList<>(selectedAttacks));
        
        System.out.println((isPlayer1 ? joueur1.getNomJoueur() : joueur2.getNomJoueur()) + " a sélectionné " + selectedAttacks.size() + 
                         " attaques pour " + currentMonstre.getNomMonstre());
        
        // Passer au monstre suivant
        currentMonsterIndex++;
        if (currentMonsterIndex >= (isPlayer1 ? joueur1.getMonstres().size() : joueur2.getMonstres().size())) {
            // Tous les monstres ont leurs attaques
            if (isPlayer1) {
                // Passer à la sélection des monstres du joueur 2
                // Cette logique sera gérée par MonstrePocheUI
                System.out.println("Attaques sélectionnées pour " + (isPlayer1 ? joueur1.getNomJoueur() : joueur2.getNomJoueur()));
            } else {
                System.out.println("Sélection terminée pour les deux joueurs !");
            }
            // La navigation sera gérée par MonstrePocheUI
        } else {
            // Afficher la sélection pour le monstre suivant
            navigationCallback.showAttackSelection(joueur1, joueur2, isPlayer1);
        }
    }
    
    public int getCurrentMonsterIndex() {
        return currentMonsterIndex;
    }
}
