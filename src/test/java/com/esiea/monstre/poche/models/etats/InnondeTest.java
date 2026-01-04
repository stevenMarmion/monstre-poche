package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.pochebis.models.etats.Innonde;

import static org.junit.jupiter.api.Assertions.*;

class InnondeTest {
    private Innonde statutInnonde;

    @BeforeEach
    void setUp() {
        statutInnonde = new Innonde(3);
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Innonde", statutInnonde.getLabelStatut());
    }
}
