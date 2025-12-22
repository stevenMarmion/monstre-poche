package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Bot;
import com.esiea.monstre.poche.models.entites.Joueur;

/**
 * Interface pour les callbacks de navigation.
 */
public interface NavigationCallback {
    void showMainMenu();
    void showLocalGameSetup();
    void showBotGameSetup();
    void showOnlineGameSetup();
    void showMonsterSelectionPlayer(Joueur joueur);
    void showAttackSelectionPlayer(Joueur joueur);
    void showMonsterSelectionPlayer(Joueur joueur, Runnable onComplete);
    void showAttackSelectionPlayer(Joueur joueur, Runnable onComplete);
    void showBattle(Joueur joueur1, Joueur joueur2);
    void showBattleBot(Joueur joueur1, Bot bot);
    void showWinnerView(Joueur winner);
}
