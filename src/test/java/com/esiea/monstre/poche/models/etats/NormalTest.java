package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalTest {
    private Normal statutNormal;

    @BeforeEach
    void setUp() {
        statutNormal = new Normal();
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Normal", statutNormal.getLabelStatut());
    }
}
