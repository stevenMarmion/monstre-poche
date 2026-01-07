package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.status.monster.Asseche;

import static org.junit.jupiter.api.Assertions.*;

class AssecheTest {
    private Asseche statutAsseche;

    @BeforeEach
    void setUp() {
        statutAsseche = new Asseche();
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Asseche", statutAsseche.getLabelStatut());
    }
}
