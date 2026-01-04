package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.pochebis.models.affinites.Normal;
import com.esiea.monstre.pochebis.models.entites.Attaque;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.etats.SousTerre;

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

    @Test
    void testSortirSousTerre() {
        // On simule que le monstre a passé tous ses tours sous terre
        monstre.setStatut(statutSousTerre);
        monstre.setDefense(80); // Défense doublée
        
        // On force nbToursAvecEffet à 0
        while (statutSousTerre.nbToursAvecEffet > 0) {
            statutSousTerre.decrementerNbToursAvecEffet();
        }
        
        statutSousTerre.sortirSousTerre(monstre);
        
        // Le statut devrait revenir à Normal
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
        // La défense devrait être divisée par 2
        assertEquals(40, monstre.getDefense());
    }
}
