package com.esiea.monstre.poche.controllers.battle;

import javafx.application.Platform;

import java.util.List;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.combats.Combat;
import com.esiea.monstre.poche.models.combats.CombatBot;
import com.esiea.monstre.poche.models.combats.CombatLocalTerminal;
import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.inventaire.Objet;
import com.esiea.monstre.poche.views.gui.battle.BattleView;

/**
 * Controller pour la gestion du système de combat.
 * Gère les tours, les actions et la logique du combat.
 */
public class BattleController {
    private BattleView view;
    private INavigationCallback INavigationCallback;
    private Combat combat;
    
    // Stockage des choix des deux joueurs
    private Object player1Action = null;
    private Object player2Action = null;
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    
    public BattleController(BattleView view, INavigationCallback INavigationCallback) {
        this.view = view;
        this.INavigationCallback = INavigationCallback;
        if (view.getJoueur2() instanceof Bot) {
            this.combat = new CombatBot(view.getJoueur1(), (Bot) view.getJoueur2());
        } else {
            this.combat = new CombatLocalTerminal(view.getJoueur1(), view.getJoueur2());
        }
        
        // Configurer le CombatLogger pour envoyer les logs à l'interface graphique
        CombatLogger.setGuiCallback(message -> {
            Platform.runLater(() -> view.updateBattleLog(message));
        });
        CombatLogger.clear();
        
        // Log du début de combat
        CombatLogger.logDebutCombat(view.getJoueur1(), view.getJoueur2());
        
        initializeEventHandlers();
        view.setTurn(true); // Toujours joueur 1 qui choisit en premier
        CombatLogger.logTourJoueur(view.getJoueur1());
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
            view.displayAttackChoices(attaques, attaque -> {
                player1Action = attaque; // null = mains nues
                player1Ready = true;
                String nomAttaque = attaque != null ? attaque.getNomAttaque() : "Mains nues";
                view.updateBattleLog(view.getJoueur1().getNomJoueur() + " a choisi : " + nomAttaque);
                
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
            view.displayAttackChoices(attaques, attaque -> {
                player2Action = attaque; // null = mains nues
                player2Ready = true;
                String nomAttaque = attaque != null ? attaque.getNomAttaque() : "Mains nues";
                view.updateBattleLog(view.getJoueur2().getNomJoueur() + " a choisi : " + nomAttaque);
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

        // Effacer uniquement les logs du tour actuel (pas tout l'historique)
        CombatLogger.clearCurrentTurn();

        // Utiliser la logique d'ordre d'exécution
        Combat.gereOrdreExecutionActions(player1Action, player2Action);

        // Récupérer et afficher les logs du tour actuel
        String detailed = CombatLogger.getFormattedCurrentTurnLogs();
        if (detailed != null && !detailed.isEmpty()) {
            view.updateBattleLog(detailed);
        }
        
        // Mise à jour de l'affichage
        view.updatePokemonDisplay();
        
        // Vérifier fin de combat
        Joueur winner = Combat.getAWinner();
        if (winner != null) {
            INavigationCallback.showWinnerView(winner);
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
        if (action == null) return "Attaque: Mains nues";
        if (action instanceof Attaque) {
            return "Attaque: " + ((Attaque) action).getNomAttaque();
        }
        if (action instanceof Monstre) {
            return "Changement: " + ((Monstre) action).getNomMonstre();
        }
        if (action instanceof Objet) {
            return "Objet: " + ((Objet) action).getNomObjet();
        }
        return action.getClass().getSimpleName();
    }
    
    /**
     * Gère l'action Objet : affiche les objets disponibles et permet de les utiliser.
     */
    private void handleItemAction() {
        Joueur currentPlayer = !player1Ready ? view.getJoueur1() : view.getJoueur2();
        
        List<Objet> objets = currentPlayer.getObjets();
        if (objets == null || objets.isEmpty()) {
            view.updateBattleLog(currentPlayer.getNomJoueur() + " n'a pas d'objets dans son sac !");
            return;
        }
        
        view.displayItemChoices(objets, objet -> {
            // Utiliser l'objet sur le monstre actuel du joueur
            Monstre cible = currentPlayer.getMonstreActuel();
            
            if (!player1Ready) {
                // Joueur 1 utilise un objet
                objet.utiliserObjet(cible);
                currentPlayer.getObjets().remove(objet);
                
                CombatLogger.logUtilisationObjet(currentPlayer, objet.getNomObjet(), cible);
                view.updateBattleLog(currentPlayer.getNomJoueur() + " utilise " + objet.getNomObjet() + " sur " + cible.getNomMonstre() + " !");
                
                // L'objet compte comme action du tour
                player1Action = objet;
                player1Ready = true;
                
                view.updatePokemonDisplay();
                
                // Si mode Bot, déclencher le tour du Bot automatiquement
                if (view.getJoueur2() instanceof Bot) {
                    handleBotTurn();
                } else {
                    // Sinon, attendre le choix du joueur 2
                    view.setTurn(false);
                    view.updateBattleLog("À " + view.getJoueur2().getNomJoueur() + " de choisir son action !");
                }
            } else if (!player2Ready) {
                // Joueur 2 utilise un objet
                objet.utiliserObjet(cible);
                currentPlayer.getObjets().remove(objet);
                
                CombatLogger.logUtilisationObjet(currentPlayer, objet.getNomObjet(), cible);
                view.updateBattleLog(currentPlayer.getNomJoueur() + " utilise " + objet.getNomObjet() + " sur " + cible.getNomMonstre() + " !");
                
                player2Action = objet;
                player2Ready = true;
                
                view.updatePokemonDisplay();
                executeTurnActions();
            }
        });
    }

    public Combat getCombat() {
        return combat;
    }
}
