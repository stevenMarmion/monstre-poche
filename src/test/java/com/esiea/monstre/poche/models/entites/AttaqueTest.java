package com.esiea.monstre.poche.models.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.affinites.Eau;
import com.esiea.monstre.poche.models.affinites.Feu;
import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.affinites.Terre;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class AttaqueTest {
    private Attaque attaque;
    private Monstre monstreAttaquant;
    private Monstre monstreCible;

    @BeforeEach
    void setUp() {
        attaque = new Attaque("Tonnerre", 10, 90, 0.1, new Normal());
        monstreAttaquant = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
        monstreCible = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Tonnerre", attaque.getNomAttaque());
        assertEquals(10, attaque.getNbUtilisations());
        assertEquals(90, attaque.getPuissanceAttaque());
        assertEquals(0.1, attaque.getProbabiliteEchec());
        assertNotNull(attaque.getTypeAttaque());
    }

    @Test
    void testSetNomAttaque() {
        attaque.setNomAttaque("Eclair");
        assertEquals("Eclair", attaque.getNomAttaque());
    }

    @Test
    void testSetNbUtilisations() {
        attaque.setNbUtilisations(15);
        assertEquals(15, attaque.getNbUtilisations());
    }

    @Test
    void testSetPuissanceAttaque() {
        attaque.setPuissanceAttaque(100);
        assertEquals(100, attaque.getPuissanceAttaque());
    }

    @Test
    void testSetProbabiliteEchec() {
        attaque.setProbabiliteEchec(0.2);
        assertEquals(0.2, attaque.getProbabiliteEchec());
    }

    @Test
    void testSetTypeAttaque() {
        Feu typeFeu = new Feu();
        attaque.setTypeAttaque(typeFeu);
        assertEquals(typeFeu, attaque.getTypeAttaque());
    }

    @Test
    void testCalculeDegatsAttaqueNormal() {
        double degats = attaque.calculeDegatsAttaque(monstreAttaquant, monstreCible);
        
        // Formule: ((11 * 50 * 90) / (25 * 50) + 2) * 1 * coeff
        // Base = (49500 / 1250 + 2) * 1 = (39.6 + 2) = 41.6
        // Avec coefficient entre 0.85 et 1.0: entre 35.36 et 41.6
        assertTrue(degats >= 35 && degats <= 42);
    }

    @Test
    void testCalculeDegatsAttaqueAvecAvantage() {
        Attaque attaqueNormal = new Attaque("Push", 15, 90, 0.1, new Normal());
        Monstre monstreTerre = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Terre());
        
        double degats = attaqueNormal.calculeDegatsAttaque(monstreAttaquant, monstreTerre);
        
        // Avec avantage x2
        // Base = (49500 / 1250 + 2) * 2 = 83.2
        // Avec coefficient entre 0.85 et 1.0: entre 70.72 et 83.2
        System.out.println(degats);
        assertTrue(degats >= 70 && degats <= 84);
    }

    @Test
    void testCalculeDegatsAttaqueAvecDesavantage() {
        Attaque attaqueFeu = new Attaque("Lance-Flammes", 15, 90, 0.1, new Feu());
        Monstre monstreEau = new Monstre("Carapuce", 115, 48, 65, 43, new ArrayList<Attaque>(), new Eau());
        
        double degats = attaqueFeu.calculeDegatsAttaque(monstreAttaquant, monstreEau);
        
        // Avec désavantage x0.5
        // Base = (49500 / 1625 + 2) * 0.5 = 15.96
        // Avec coefficient entre 0.85 et 1.0: entre 13.57 et 15.96
        assertTrue(degats >= 13 && degats <= 17);
    }

    @Test
    void testToString() {
        String result = attaque.toString();
        assertTrue(result.contains("Tonnerre"));
        assertTrue(result.contains("10"));
        assertTrue(result.contains("90"));
    }

    @Test
    void testCalculeDegatsAttaqueAvecDifferentsAttributs() {
        Monstre monstreAttaquantFort = new Monstre("Machamp", 200, 100, 80, 60, new ArrayList<Attaque>(), new Normal());
        Monstre monstreCibleFaible = new Monstre("Roucool", 80, 30, 25, 70, new ArrayList<Attaque>(), new Normal());
        
        Attaque attaquePuissante = new Attaque("Poing Dynamique", 5, 120, 0.0, new Normal());
        
        double degats = attaquePuissante.calculeDegatsAttaque(monstreAttaquantFort, monstreCibleFaible);
        
        // Formule: ((11 * 100 * 120) / (25 * 25) + 2) * 1 * coeff
        // Base = (132000 / 625 + 2) * 1 = 213.2
        // Avec coefficient entre 0.85 et 1.0: entre 181.22 et 213.2
        assertTrue(degats >= 180 && degats <= 215);
    }
}
