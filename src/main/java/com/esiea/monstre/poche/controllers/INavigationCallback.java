package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.battle.ai.Bot;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.views.gui.battle.BattleView;

/**
 * Interface pour les callbacks de navigation.
 */
public interface INavigationCallback {
    void showMainMenu();
    void showLocalGameSetup();
    void showBotGameSetup();
    void showOnlineGameSetup();
    void showMonsterSelectionPlayer(Joueur joueur, Runnable onComplete);
    void showAttackSelectionPlayer(Joueur joueur, Runnable onComplete);
    void showObjectSelectionPlayer(Joueur joueur, Runnable onComplete);
    void showBattle(Joueur joueur1, Joueur joueur2);
    void showBattleBot(Joueur joueur1, Bot bot);
    void showWinnerView(Joueur winner);
    void showBattleOnline(BattleView battleView);
}
