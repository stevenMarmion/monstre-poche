package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.status.monster.SousTerre;
import com.esiea.monstre.poche.models.types.Normal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class SousTerreTest {
    private SousTerre statutSousTerre;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        statutSousTerre = new SousTerre();
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("SousTerre", statutSousTerre.getLabelStatut());
    }

    // @Test
    void testAppliquerEffets() {
        int defenseInitiale = monstre.getDefense();
        monstre.setStatut(statutSousTerre);
        
        statutSousTerre.appliquerEffets(monstre);
        
        // La défense devrait doubler
        assertEquals(defenseInitiale * 2, monstre.getDefense());
    }
}
