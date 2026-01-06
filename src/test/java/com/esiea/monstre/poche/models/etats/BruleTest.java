package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class BruleTest {
    private Brule statutBrule;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        statutBrule = new Brule();
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Brule", statutBrule.getLabelStatut());
    }

    @Test
    void testAppliquerEffets() {
        double pvAvant = monstre.getPointsDeVie();
        double degats = 50;
        
        statutBrule.appliquerEffets(monstre, degats);
        
        // CDC: Les dégâts de brûlure sont égaux à attaque / 10 = 50 / 10 = 5
        assertEquals(pvAvant - 5, monstre.getPointsDeVie());
    }

    @Test
    void testAppliquerEffetsAvecPeuDePV() {
        monstre.setPointsDeVie(3);
        double degats = 50;
        
        statutBrule.appliquerEffets(monstre, degats);
        
        // CDC: Dégâts brûlure = attaque/10 = 50/10 = 5
        // Avec 3 PV, le monstre tombe à 0 (pas en négatif)
        assertEquals(0.0, monstre.getPointsDeVie());
    }
}
