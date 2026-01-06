package com.esiea.monstre.poche.models.inventaire.medicaments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.affinites.Normal;
import com.esiea.monstre.poche.models.entites.Attaque;
import com.esiea.monstre.poche.models.entites.Monstre;
import com.esiea.monstre.poche.models.etats.Paralyse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class MedicamentAntiParalysieTest {
    private MedicamentAntiParalysie medicament;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        medicament = new MedicamentAntiParalysie("Anti-paralysie");
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Anti-paralysie", medicament.getNomObjet());
    }

    @Test
    void testUtiliserObjetSurMonstreParalyse() {
        monstre.setStatut(new Paralyse());
        assertEquals("Paralyse", monstre.getStatut().getLabelStatut());
        
        medicament.utiliserObjet(monstre);
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
    }

    @Test
    void testUtiliserObjetSurMonstreNormal() {
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
        
        medicament.utiliserObjet(monstre);
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
    }
}
