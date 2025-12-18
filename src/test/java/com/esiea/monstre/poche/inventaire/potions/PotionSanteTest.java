package com.esiea.monstre.poche.inventaire.potions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.affinites.Normal;
import com.esiea.monstre.poche.entites.Monstre;

class PotionSanteTest {
    private PotionSante potionSante;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        potionSante = new PotionSante("Potion de soin", 50);
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Potion de soin", potionSante.getNomObjet());
    }

    @Test
    void testUtiliserObjet() {
        monstre.setPointsDeVie(50);
        potionSante.utiliserObjet(monstre);
        assertEquals(100, monstre.getPointsDeVie());
    }

    @Test
    void testUtiliserObjetSansDepasser() {
        monstre.setPointsDeVie(80);
        potionSante.utiliserObjet(monstre);
        assertEquals(100, monstre.getPointsDeVie()); // Ne dépasse pas le max
    }

    @Test
    void testUtiliserObjetAvecPVMax() {
        monstre.setPointsDeVie(100);
        potionSante.utiliserObjet(monstre);
        assertEquals(100, monstre.getPointsDeVie());
    }
}
