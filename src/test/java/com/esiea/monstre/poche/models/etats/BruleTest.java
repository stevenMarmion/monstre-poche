package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.pochebis.models.affinites.Normal;
import com.esiea.monstre.pochebis.models.entites.Attaque;
import com.esiea.monstre.pochebis.models.entites.Monstre;
import com.esiea.monstre.pochebis.models.etats.Brule;

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
        
        // Les dégâts de brûlure sont égaux à degats / 10 = 50 / 10 = 5
        assertEquals(pvAvant - 5, monstre.getPointsDeVie());
    }

    @Test
    void testAppliquerEffetsAvecPeuDePV() {
        monstre.setPointsDeVie(3);
        double pvAvant = monstre.getPointsDeVie();
        double degats = 50;
        
        statutBrule.appliquerEffets(monstre, degats);
        
        // Les dégâts devraient être de 0 car les PV ne peuvent pas descendre en dessous de 0
        // selon la logique du code
        assertEquals(pvAvant, monstre.getPointsDeVie());
    }
}
