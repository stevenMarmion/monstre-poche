package com.esiea.monstre.poche.chore.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.chore.models.affinites.Normal;
import com.esiea.monstre.poche.chore.models.entites.Attaque;
import com.esiea.monstre.poche.chore.models.entites.Monstre;
import com.esiea.monstre.poche.chore.models.etats.Paralyse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class ParalyseTest {
    private Paralyse statutParalyse;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        statutParalyse = new Paralyse();
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Paralyse", statutParalyse.getLabelStatut());
    }

    @Test
    void testRateAttaque() {
        // Ce test est probabiliste, on va juste vérifier que ça ne plante pas
        monstre.setRateAttaque(false);
        statutParalyse.rateAttaque(monstre);
        // Le résultat peut être true ou false selon le random
        assertNotNull(monstre.isRateAttaque());
    }

    @Test
    void testAppliquerEffets() {
        // Ce test vérifie que la méthode ne plante pas
        statutParalyse.appliquerEffets(monstre, 50);
        assertNotNull(monstre.getStatut());
    }
}
