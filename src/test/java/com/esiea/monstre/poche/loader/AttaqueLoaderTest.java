package com.esiea.monstre.poche.loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.esiea.monstre.poche.actions.Attaque;

class AttaqueLoaderTest {
    private AttaqueLoader loader;

    @BeforeEach
    void setUp() {
        loader = new AttaqueLoader("attacks.txt");
    }

    @Test
    void testConstructeur() {
        assertNotNull(loader);
        assertEquals(0, loader.getNombreRessources());
    }

    @Test
    void testCharger() {
        boolean resultat = loader.charger();
        assertTrue(resultat);
        assertTrue(loader.getNombreRessources() > 0);
    }

    @Test
    void testGetRessources() {
        loader.charger();
        assertNotNull(loader.getRessources());
        assertTrue(loader.getRessources().size() > 0);
    }

    @Test
    void testGetRessourceParNom() {
        loader.charger();
        Attaque attaque = loader.getRessourceParNom("Tonnerre");
        
        if (attaque != null) {
            assertEquals("Tonnerre", attaque.getNomAttaque());
            assertTrue(attaque.getPuissanceAttaque() > 0);
            assertTrue(attaque.getNbUtilisations() > 0);
            assertTrue(attaque.getProbabiliteEchec() >= 0);
        }
    }

    @Test
    void testGetRessourceParNomInexistant() {
        loader.charger();
        Attaque attaque = loader.getRessourceParNom("AttaqueInexistante");
        assertNull(attaque);
    }

    @Test
    void testGetErreurs() {
        assertNotNull(loader.getErreurs());
    }

    @Test
    void testChargerAvecFichierInexistant() {
        AttaqueLoader loaderInvalide = new AttaqueLoader("fichier_inexistant.txt");
        boolean resultat = loaderInvalide.charger();
        assertFalse(resultat);
        assertTrue(loaderInvalide.getErreurs().size() > 0);
    }

    @Test
    void testChargerPlusieursAttaques() {
        loader.charger();
        assertTrue(loader.getNombreRessources() >= 1);
        
        for (Attaque attaque : loader.getRessources()) {
            assertNotNull(attaque.getNomAttaque());
            assertNotNull(attaque.getTypeAttaque());
            assertTrue(attaque.getPuissanceAttaque() >= 0);
            assertTrue(attaque.getNbUtilisations() > 0);
            assertTrue(attaque.getProbabiliteEchec() >= 0.0 && attaque.getProbabiliteEchec() <= 1.0);
        }
    }
}
