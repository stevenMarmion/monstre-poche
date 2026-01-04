package com.esiea.monstre.poche.models.affinites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.pochebis.models.affinites.Nature;

import static org.junit.jupiter.api.Assertions.*;

class NatureTest {
    private Nature typeNature;

    @BeforeEach
    void setUp() {
        typeNature = new Nature();
    }

    @Test
    void testGetLabelType() {
        assertNull(typeNature.getLabelType());
    }

    @Test
    void testEstFortContre() {
        assertNull(typeNature.estFortContre());
    }

    @Test
    void testEstFaibleContre() {
        assertNull(typeNature.estFaibleContre());
    }
}
