package com.esiea.monstre.poche.models.inventaire.potions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.items.potions.PotionDegat;
import com.esiea.monstre.poche.models.types.Normal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class PotionDegatTest {
    private PotionDegat potionDegat;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        potionDegat = new PotionDegat("Potion de dégâts", 30);
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Potion de dégâts", potionDegat.getNomObjet());
    }

    @Test
    void testUtiliserObjet() {
        int attaqueAvant = monstre.getAttaque();
        potionDegat.utiliserObjet(monstre);
        assertEquals(attaqueAvant + 30, monstre.getAttaque());
    }

    @Test
    void testUtiliserObjetPlusieursGois() {
        int attaqueAvant = monstre.getAttaque();
        potionDegat.utiliserObjet(monstre);
        potionDegat.utiliserObjet(monstre);
        assertEquals(attaqueAvant + 60, monstre.getAttaque());
    }
}
