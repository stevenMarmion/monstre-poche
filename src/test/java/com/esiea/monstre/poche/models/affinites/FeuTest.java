package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeuTest {
    private Feu typeFeu;

    @BeforeEach
    void setUp() {
        typeFeu = new Feu();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Feu", typeFeu.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Nature", typeFeu.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Eau", typeFeu.estFaibleContre());
    }
}
