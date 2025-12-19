package com.esiea.monstre.poche.models.inventaire.potions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

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
