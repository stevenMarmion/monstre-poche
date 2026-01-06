package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsecteTest {
    private Insecte typeInsecte;

    @BeforeEach
    void setUp() {
        typeInsecte = new Insecte();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Insecte", typeInsecte.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Plante", typeInsecte.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Feu", typeInsecte.estFaibleContre());
    }
}
