package com.esiea.monstre.poche.loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.esiea.monstre.poche.entites.Monstre;

class MonstreLoaderTest {
    private MonstreLoader loader;

    @BeforeEach
    void setUp() {
        loader = new MonstreLoader("monsters.txt");
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
        Monstre monstre = loader.getRessourceParNom("Pikachu");
        
        if (monstre != null) {
            assertEquals("Pikachu", monstre.getNomMonstre());
            assertTrue(monstre.getPointsDeVie() > 0);
            assertTrue(monstre.getAttaque() > 0);
            assertTrue(monstre.getDefense() > 0);
            assertTrue(monstre.getVitesse() > 0);
        }
    }

    @Test
    void testGetRessourceParNomInexistant() {
        loader.charger();
        Monstre monstre = loader.getRessourceParNom("MonstreInexistant");
        assertNull(monstre);
    }

    @Test
    void testGetErreurs() {
        assertNotNull(loader.getErreurs());
    }

    @Test
    void testChargerAvecFichierInexistant() {
        MonstreLoader loaderInvalide = new MonstreLoader("fichier_inexistant.txt");
        boolean resultat = loaderInvalide.charger();
        assertFalse(resultat);
        assertTrue(loaderInvalide.getErreurs().size() > 0);
    }
}
