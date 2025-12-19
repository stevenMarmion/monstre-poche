package com.esiea.monstre.poche.models.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.esiea.monstre.poche.models.etats.Asseche;
import com.esiea.monstre.poche.models.etats.Innonde;
import com.esiea.monstre.poche.models.etats.StatutTerrain;

class TerrainTest {
    private Terrain terrain;
    private StatutTerrain statutAsseche;
    private StatutTerrain statutInnonde;

    @BeforeEach
    void setUp() {
        statutAsseche = new Asseche();
        statutInnonde = new Innonde(3);
        terrain = new Terrain("Terrain de combat", statutAsseche);
    }

    @Test
    void testConstructeur() {
        assertEquals("Terrain de combat", terrain.getNomTerrain());
        assertNotNull(terrain.getStatutTerrain());
        assertEquals("Asseche", terrain.getStatutTerrain().getLabelStatut());
    }

    @Test
    void testSetStatutTerrain() {
        terrain.setStatutTerrain(statutInnonde);
        assertEquals(statutInnonde, terrain.getStatutTerrain());
        assertEquals("Innonde", terrain.getStatutTerrain().getLabelStatut());
    }

    @Test
    void testEstInnondeAvecTerrainAsseche() {
        assertFalse(terrain.estInnonde());
    }

    @Test
    void testEstInnondeAvecTerrainInnonde() {
        terrain.setStatutTerrain(statutInnonde);
        assertTrue(terrain.estInnonde());
    }

    @Test
    void testToString() {
        String result = terrain.toString();
        assertTrue(result.contains("Terrain de combat"));
        assertTrue(result.contains("Asseche"));
    }

    @Test
    void testEquals() {
        Terrain terrain2 = new Terrain("Terrain de combat", statutAsseche);
        assertTrue(terrain.equals(terrain2));
        
        // Test avec null
        assertFalse(terrain.equals(null));
        
        // Test avec le même objet
        assertTrue(terrain.equals(terrain));
        
        // Test avec un objet d'un autre type
        assertFalse(terrain.equals("Terrain"));
    }

    @Test
    void testEqualsAvecNomDifferent() {
        Terrain terrain2 = new Terrain("Arène", statutAsseche);
        assertFalse(terrain.equals(terrain2));
    }

    @Test
    void testEqualsAvecStatutDifferent() {
        Terrain terrain2 = new Terrain("Terrain de combat", statutInnonde);
        assertFalse(terrain.equals(terrain2));
    }
}
