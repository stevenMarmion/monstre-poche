package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlanteTest {
    private Plante typePlante;

    @BeforeEach
    void setUp() {
        typePlante = new Plante();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Plante", typePlante.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Eau", typePlante.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Feu", typePlante.estFaibleContre());
    }
}
