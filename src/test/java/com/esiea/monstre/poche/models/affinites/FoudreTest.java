package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.types.Foudre;

import static org.junit.jupiter.api.Assertions.*;

class FoudreTest {
    private Foudre typeFoudre;

    @BeforeEach
    void setUp() {
        typeFoudre = new Foudre();
        typeFoudre.setChanceParalysie(0.5);
    }

    @Test
    void testGetLabelType() {
        assertEquals("Foudre", typeFoudre.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Eau", typeFoudre.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Terre", typeFoudre.estFaibleContre());
    }
}
