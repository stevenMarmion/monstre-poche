package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EauTest {
    private Eau typeEau;

    @BeforeEach
    void setUp() {
        typeEau = new Eau();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Eau", typeEau.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Feu", typeEau.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Foudre", typeEau.estFaibleContre());
    }
}
