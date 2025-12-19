package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Joueur;

/**
 * Interface pour les callbacks de navigation.
 */
public interface NavigationCallback {
    void showMainMenu();
    void showLocalGameSetup();
    void showBotGameSetup();
    void showOnlineGameSetup();
    void showMonsterSelection(Joueur joueur1, Joueur joueur2, boolean isPlayer1);
    void showAttackSelection(Joueur joueur1, Joueur joueur2, boolean isPlayer1);
    void showBattle(Joueur joueur1, Joueur joueur2);
}
