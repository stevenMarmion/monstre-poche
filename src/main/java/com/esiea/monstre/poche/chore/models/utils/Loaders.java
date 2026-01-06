package com.esiea.monstre.poche.chore.models.utils;

import com.esiea.monstre.poche.chore.models.loader.AttaqueLoader;
import com.esiea.monstre.poche.chore.models.loader.MonstreLoader;

public class Loaders {
    public static AttaqueLoader attaqueLoader;
    public static MonstreLoader monstreLoader;

    public static void chargeLoaders() {
        attaqueLoader = new AttaqueLoader("attacks.txt");
        attaqueLoader.charger();
        monstreLoader = new MonstreLoader("monsters.txt");
        monstreLoader.charger();
    }
}
