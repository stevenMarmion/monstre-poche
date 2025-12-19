package com.esiea.monstre.poche.models.inventaire.potions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

class PotionVitesseTest {
    private PotionVitesse potionVitesse;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        potionVitesse = new PotionVitesse("Potion de vitesse", 20);
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Potion de vitesse", potionVitesse.getNomObjet());
    }

    @Test
    void testUtiliserObjet() {
        int vitesseAvant = monstre.getVitesse();
        potionVitesse.utiliserObjet(monstre);
        assertEquals(vitesseAvant + 20, monstre.getVitesse());
    }

    @Test
    void testUtiliserObjetPlusieursGois() {
        int vitesseAvant = monstre.getVitesse();
        potionVitesse.utiliserObjet(monstre);
        potionVitesse.utiliserObjet(monstre);
        assertEquals(vitesseAvant + 40, monstre.getVitesse());
    }
}
