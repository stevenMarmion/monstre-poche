package com.esiea.monstre.poche.chore.models.inventaire.medicaments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.chore.models.affinites.Normal;
import com.esiea.monstre.poche.chore.models.entites.Attaque;
import com.esiea.monstre.poche.chore.models.entites.Monstre;
import com.esiea.monstre.poche.chore.models.etats.Empoisonne;
import com.esiea.monstre.poche.chore.models.inventaire.medicaments.MedicamentAntiPoison;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class MedicamentAntiPoisonTest {
    private MedicamentAntiPoison medicament;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        medicament = new MedicamentAntiPoison("Anti-poison");
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Anti-poison", medicament.getNomObjet());
    }

    @Test
    void testUtiliserObjetSurMonstreEmpoisonne() {
        monstre.setStatut(new Empoisonne());
        assertEquals("Empoisonne", monstre.getStatut().getLabelStatut());
        
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
