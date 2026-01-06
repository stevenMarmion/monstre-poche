package com.esiea.monstre.poche.chore.models.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.chore.models.affinites.Normal;
import com.esiea.monstre.poche.chore.models.entites.Attaque;
import com.esiea.monstre.poche.chore.models.entites.Monstre;
import com.esiea.monstre.poche.chore.models.entites.Terrain;
import com.esiea.monstre.poche.chore.models.etats.Asseche;
import com.esiea.monstre.poche.chore.models.etats.Brule;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class MonstreTest {
    private Monstre monstre;
    private ArrayList<Attaque> attaques;

    @BeforeEach
    void setUp() {
        attaques = new ArrayList<>();
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, attaques, new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Pikachu", monstre.getNomMonstre());
        assertEquals(100, monstre.getPointsDeVie());
        assertEquals(100, monstre.getPointsDeVieMax());
        assertEquals(50, monstre.getAttaque());
        assertEquals(40, monstre.getDefense());
        assertEquals(90, monstre.getVitesse());
        assertNotNull(monstre.getAttaques());
        assertNotNull(monstre.getTypeMonstre());
        assertNotNull(monstre.getStatut());
        assertFalse(monstre.isRateAttaque());
    }

    @Test
    void testSetPointsDeVie() {
        monstre.setPointsDeVie(50);
        assertEquals(50, monstre.getPointsDeVie());
    }

    @Test
    void testSetPointsDeVieNegatif() {
        monstre.setPointsDeVie(-10);
        assertEquals(0, monstre.getPointsDeVie());
    }

    @Test
    void testSetPointsDeVieSuperieurMax() {
        monstre.setPointsDeVie(150);
        assertEquals(100, monstre.getPointsDeVie());
    }

    @Test
    void testSetPointsDeVieMax() {
        monstre.setPointsDeVieMax(200);
        assertEquals(200, monstre.getPointsDeVieMax());
    }

    @Test
    void testSetAttaque() {
        monstre.setAttaque(75);
        assertEquals(75, monstre.getAttaque());
    }

    @Test
    void testSetDefense() {
        monstre.setDefense(60);
        assertEquals(60, monstre.getDefense());
    }

    @Test
    void testSetVitesse() {
        monstre.setVitesse(100);
        assertEquals(100, monstre.getVitesse());
    }

    @Test
    void testSetStatut() {
        Brule brule = new Brule();
        monstre.setStatut(brule);
        assertEquals(brule, monstre.getStatut());
    }

    @Test
    void testSetRateAttaque() {
        monstre.setRateAttaque(true);
        assertTrue(monstre.isRateAttaque());
        
        monstre.setRateAttaque(false);
        assertFalse(monstre.isRateAttaque());
    }

    @Test
    void testAjouterAttaque() {
        Attaque attaque1 = new Attaque("Tonnerre", 10, 90, 0.1, new Normal());
        monstre.ajouterAttaque(attaque1);
        assertEquals(1, monstre.getAttaques().size());
        assertTrue(monstre.getAttaques().contains(attaque1));
    }

    @Test
    void testAjouterAttaqueDupliquee() {
        Attaque attaque1 = new Attaque("Tonnerre", 10, 90, 0.1, new Normal());
        monstre.ajouterAttaque(attaque1);
        monstre.ajouterAttaque(attaque1);
        assertEquals(1, monstre.getAttaques().size());
    }

    @Test
    void testAjouterAttaqueMax4() {
        Attaque attaque1 = new Attaque("Tonnerre", 10, 90, 0.1, new Normal());
        Attaque attaque2 = new Attaque("Eclair", 15, 40, 0.0, new Normal());
        Attaque attaque3 = new Attaque("Fatal-Foudre", 5, 120, 0.3, new Normal());
        Attaque attaque4 = new Attaque("Cage-Eclair", 20, 60, 0.05, new Normal());
        Attaque attaque5 = new Attaque("Charge", 35, 35, 0.0, new Normal());
        
        monstre.ajouterAttaque(attaque1);
        monstre.ajouterAttaque(attaque2);
        monstre.ajouterAttaque(attaque3);
        monstre.ajouterAttaque(attaque4);
        assertEquals(4, monstre.getAttaques().size());
        
        // Tentative d'ajouter une 5ème attaque
        monstre.ajouterAttaque(attaque5);
        assertEquals(4, monstre.getAttaques().size());
        assertFalse(monstre.getAttaques().contains(attaque5));
    }

    @Test
    void testCalculeDegat() {
        Monstre cible = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
        double degats = monstre.calculeDegat(monstre, cible);
        
        // Les dégâts doivent être entre 0.85 et 1.0 fois le calcul de base
        // Calcul de base: 20 * (50/50) = 20
        assertTrue(degats >= 17 && degats <= 20);
    }

    @Test
    void testAttaquerSansAttaque() {
        Monstre cible = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
        Terrain terrain = new Terrain("Terrain de combat", new Asseche());
        double pvAvant = cible.getPointsDeVie();
        
        monstre.attaquer(cible, terrain, null);
        
        assertTrue(cible.getPointsDeVie() < pvAvant);
    }

    @Test
    void testAttaquerAvecAttaque() {
        Monstre cible = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
        Terrain terrain = new Terrain("Terrain de combat", new Asseche());
        Attaque attaque = new Attaque("Tonnerre", 10, 90, 0.0, new Normal());
        monstre.ajouterAttaque(attaque);
        
        double pvAvant = cible.getPointsDeVie();
        monstre.attaquer(cible, terrain, attaque);
        
        assertTrue(cible.getPointsDeVie() < pvAvant);
    }

    @Test
    void testToString() {
        String result = monstre.toString();
        assertTrue(result.contains("Pikachu"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("50"));
        assertTrue(result.contains("40"));
        assertTrue(result.contains("90"));
    }

    @Test
    void testEquals() {
        Monstre monstre2 = new Monstre("Pikachu", 100, 50, 40, 90, attaques, new Normal());
        assertTrue(monstre.equals(monstre2));
        
        // Test avec null
        assertFalse(monstre.equals(null));
        
        // Test avec le même objet
        assertTrue(monstre.equals(monstre));
        
        // Test avec un objet d'un autre type
        assertFalse(monstre.equals("Pikachu"));
    }

    @Test
    void testEqualsAvecNomDifferent() {
        Monstre monstre2 = new Monstre("Bulbizarre", 100, 50, 40, 90, attaques, new Normal());
        assertFalse(monstre.equals(monstre2));
    }

    @Test
    void testEqualsAvecStatsDifferentes() {
        Monstre monstre2 = new Monstre("Pikachu", 100, 55, 40, 90, attaques, new Normal());
        assertFalse(monstre.equals(monstre2));
    }
}
