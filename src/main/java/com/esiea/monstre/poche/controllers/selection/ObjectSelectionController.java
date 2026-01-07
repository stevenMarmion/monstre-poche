package com.esiea.monstre.poche.controllers.selection;

import java.util.List;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.battle.CombatLogger;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.items.Objet;
import com.esiea.monstre.poche.views.gui.selection.ObjectSelectionView;

/**
 * Controller pour la sélection des objets.
 */
public class ObjectSelectionController {
    
    private ObjectSelectionView view;
    private INavigationCallback navigationCallback;
    private Joueur joueur;
    private Runnable onComplete;
    
    public ObjectSelectionController(ObjectSelectionView view, INavigationCallback navigationCallback, Joueur joueur) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        this.joueur = joueur;
        initializeEventHandlers();
    }
    
    public ObjectSelectionController(ObjectSelectionView view, INavigationCallback navigationCallback, Joueur joueur, Runnable onComplete) {
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
     * Gère la validation de la sélection des objets.
     */
    private void handleValidateSelection() {
        List<Objet> selectedObjects = view.getSelectedObjects();
        
        // Ajouter les objets sélectionnés à l'inventaire du joueur
        for (Objet objet : selectedObjects) {
            joueur.ajouterObjet(objet);
        }
        
        CombatLogger.info(joueur.getNomJoueur() + " a sélectionné " + selectedObjects.size() + " objets");
        
        // Notifier la fin de sélection si un callback est fourni
        if (onComplete != null) {
            onComplete.run();
        }
    }
}
