package com.esiea.monstre.poche.models.etats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;

class EmpoisonneTest {
    private Empoisonne statutEmpoisonne;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        statutEmpoisonne = new Empoisonne();
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testGetLabelStatut() {
        assertEquals("Empoisonne", statutEmpoisonne.getLabelStatut());
    }

    @Test
    void testAppliquerEffets() {
        double pvAvant = monstre.getPointsDeVie();
        double degats = 50;
        
        statutEmpoisonne.appliquerEffets(monstre, degats);
        
        // Les dégâts de poison sont égaux à degats / 10 = 50 / 10 = 5
        assertEquals(pvAvant - 5, monstre.getPointsDeVie());
    }

    @Test
    void testAppliquerEffetsAvecPeuDePV() {
        monstre.setPointsDeVie(3);
        double pvAvant = monstre.getPointsDeVie();
        double degats = 50;
        
        statutEmpoisonne.appliquerEffets(monstre, degats);
        
        // Les dégâts devraient être de 0 car les PV ne peuvent pas descendre en dessous de 0
        assertEquals(pvAvant, monstre.getPointsDeVie());
    }
}
