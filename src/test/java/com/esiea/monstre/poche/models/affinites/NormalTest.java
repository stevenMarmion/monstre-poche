package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.types.Normal;

import static org.junit.jupiter.api.Assertions.*;

class NormalTest {
    private Normal typeNormal;

    @BeforeEach
    void setUp() {
        typeNormal = new Normal();
    }

    @Test
    void testGetLabelType() {
        assertEquals("Normal", typeNormal.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertEquals("Terre", typeNormal.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertEquals("Nature", typeNormal.estFaibleContre());
    }
}
