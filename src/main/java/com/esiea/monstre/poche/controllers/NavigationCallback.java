package com.esiea.monstre.poche.controllers;

import com.esiea.monstre.poche.models.entites.Monstre;
import java.util.List;

/**
 * Interface pour les callbacks de navigation.
 */
public interface NavigationCallback {
    void showMainMenu();
    void showLocalGameSetup();
    void showBotGameSetup();
    void showOnlineGameSetup();
    void showMonsterSelection(String playerName, boolean isPlayer1);
    void showAttackSelection(String playerName, List<Monstre> monsters, boolean isPlayer1);
}
