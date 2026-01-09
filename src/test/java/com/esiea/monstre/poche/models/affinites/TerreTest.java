package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.types.Terre;

import static org.junit.jupiter.api.Assertions.*;

class TerreTest {
    private Terre typeTerre;

    @BeforeEach
    void setUp() {
        typeTerre = new Terre();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Terre", typeTerre.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Foudre", typeTerre.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Nature", typeTerre.estFaibleContre());
    }
}
