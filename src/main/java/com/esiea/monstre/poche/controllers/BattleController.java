package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.combats.Combat;
import com.esiea.monstre.poche.models.combats.CombatBot;
import com.esiea.monstre.poche.models.combats.CombatLocalTerminal;
import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.views.BattleView;
import java.util.List;

/**
 * Controller pour la gestion du système de combat.
 * Gère les tours, les actions et la logique du combat.
 */
public class BattleController {
    private BattleView view;
    private NavigationCallback navigationCallback;
    private Combat combat;
    
    // Stockage des choix des deux joueurs
    private Object player1Action = null;
    private Object player2Action = null;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    
    public BattleController(BattleView view, NavigationCallback navigationCallback) {
        this.view = view;
        this.navigationCallback = navigationCallback;
        if (view.getJoueur2() instanceof Bot) {
            this.combat = new CombatBot(view.getJoueur1(), (Bot) view.getJoueur2());
        } else {
            this.combat = new CombatLocalTerminal(view.getJoueur1(), view.getJoueur2());
        }
        initializeEventHandlers();
        view.setTurn(true); // Toujours joueur 1 qui choisit en premier
        view.updateBattleLog("À " + view.getJoueur1().getNomJoueur() + " de choisir son action !");
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
     * Déclenche le choix automatique du Bot et exécute le tour.
     */
    private void handleBotTurn() {
        Bot bot = (Bot) view.getJoueur2();
        Attaque attaqueBot = bot.choisirActionAutomatiquement(view.getJoueur1().getMonstreActuel());
        player2Action = attaqueBot;
        player2Ready = true;
        if (attaqueBot != null) {
            view.updateBattleLog(view.getJoueur2().getNomJoueur() + " (Bot) a choisi: " + attaqueBot.getNomAttaque());
        }
        executeTurnActions();
    }
    
    /**
     * Gère l'action Attaquer.
     */
    private void handleAttackAction() {
        if (!player1Ready) {
            // Joueur 1 choisit son attaque
            List<Attaque> attaques = view.getJoueur1().getMonstreActuel().getAttaques();
            if (attaques == null || attaques.isEmpty()) {
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " n'a pas d'attaques disponibles.");
                return;
            }
            view.displayAttackChoices(attaques, attaque -> {
                player1Action = attaque;
                player1Ready = true;
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " a choisi : " + attaque.getNomAttaque());
                
                // Si mode Bot, déclencher le tour du Bot automatiquement
                if (view.getJoueur2() instanceof Bot) {
                    handleBotTurn();
                } else {
                    // Sinon, attendre le choix du joueur 2
                    view.setTurn(false);
                    view.updateBattleLog("À " + view.getJoueur2().getNomJoueur() + " de choisir son action !");
                }
            });
        } else if (!player2Ready) {
            // Joueur 2 choisit son attaque
            List<Attaque> attaques = view.getJoueur2().getMonstreActuel().getAttaques();
            if (attaques == null || attaques.isEmpty()) {
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " n'a pas d'attaques disponibles.");
                return;
            }
            view.displayAttackChoices(attaques, attaque -> {
                player2Action = attaque;
                player2Ready = true;
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " a choisi : " + attaque.getNomAttaque());
                executeTurnActions();
            });
        }
    }

    /**
     * Gère l'action Changer: affiche les monstres disponibles et applique le changement.
     */
    private void handleSwitchAction() {
        if (!player1Ready) {
            // Joueur 1 choisit son changement
            List<Monstre> candidates = view.getJoueur1().getMonstres();
            Monstre current = view.getJoueur1().getMonstreActuel();
            List<Monstre> available = candidates.stream()
                    .filter(m -> m.getPointsDeVie() > 0 && m != current)
                    .toList();
            view.displayMonsterChoices(available, selected -> {
                player1Action = selected;
                player1Ready = true;
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " choisit d'envoyer " + selected.getNomMonstre() + " !");
                
                // Si mode Bot, déclencher le tour du Bot automatiquement
                if (view.getJoueur2() instanceof Bot) {
                    handleBotTurn();
                } else {
                    // Sinon, attendre le choix du joueur 2
                    view.setTurn(false);
                    view.updateBattleLog("À " + view.getJoueur2().getNomJoueur() + " de choisir son action !");
                }
            });
        } else if (!player2Ready) {
            // Joueur 2 choisit son changement
            List<Monstre> candidates = view.getJoueur2().getMonstres();
            Monstre current = view.getJoueur2().getMonstreActuel();
            List<Monstre> available = candidates.stream()
                    .filter(m -> m.getPointsDeVie() > 0 && m != current)
                    .toList();
            view.displayMonsterChoices(available, selected -> {
                player2Action = selected;
                player2Ready = true;
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " choisit d'envoyer " + selected.getNomMonstre() + " !");
                executeTurnActions();
            });
        }
    }
    
    /**
     * Exécute les actions des deux joueurs dans l'ordre de vitesse.
     */
    private void executeTurnActions() {
        if (!player1Ready || !player2Ready) {
            return; // Attendre que les deux joueurs soient prêts
        }
        // Afficher uniquement les informations du tour courant
        view.clearBattleLog();

        // Annonce des actions
        view.updateBattleLog("— Actions annoncées —");
        view.updateBattleLog(view.getJoueur1().getNomJoueur() + ": " + formatAction(player1Action));
        view.updateBattleLog(view.getJoueur2().getNomJoueur() + ": " + formatAction(player2Action));

        // Activer le logger et exécuter
        CombatLogger.clear();

        // Utiliser la logique d'ordre d'exécution
        Combat.gereOrdreExecutionActions(player1Action, player2Action);

        // Récupérer et afficher les logs détaillés
        String detailed = CombatLogger.getFormattedLogs();
        if (detailed != null && !detailed.isEmpty()) {
            view.updateBattleLog(detailed);
        }
        
        // Mise à jour de l'affichage
        view.updatePokemonDisplay();
        
        // Vérifier fin de combat
        Joueur winner = Combat.getAWinner();
        if (winner != null) {
            navigationCallback.showWinnerView(winner);
            return;
        }
        
        // Réinitialiser pour le prochain tour
        player1Action = null;
        player2Action = null;
        player1Ready = false;
        player2Ready = false;
        
        view.setTurn(true);
        view.updateBattleLog("À " + view.getJoueur1().getNomJoueur() + " de choisir son action !");
    }

    private String formatAction(Object action) {
        if (action == null) return "(aucune)";
        if (action instanceof Attaque) {
            return "Attaque: " + ((Attaque) action).getNomAttaque();
        }
        if (action instanceof Monstre) {
            return "Changement: " + ((Monstre) action).getNomMonstre();
        }
        return action.getClass().getSimpleName();
    }
    
    /**
     * Gère l'action Objet.
     */
    private void handleItemAction() {
        view.updateBattleLog("Fonctionnalité Objet non implémentée.");
        // TODO: implémenter la logique des objets
    }

    public Combat getCombat() {
        return combat;
    }
}
