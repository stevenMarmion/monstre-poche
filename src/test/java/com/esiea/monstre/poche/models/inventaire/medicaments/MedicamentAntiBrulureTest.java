package com.esiea.monstre.poche.models.inventaire.medicaments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.items.medicaments.MedicamentAntiBrulure;
import com.esiea.monstre.poche.models.status.monster.Brule;
import com.esiea.monstre.poche.models.types.Normal;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class MedicamentAntiBrulureTest {
    private MedicamentAntiBrulure medicament;
    private Monstre monstre;

    @BeforeEach
    void setUp() {
        medicament = new MedicamentAntiBrulure("Anti-brûlure");
        monstre = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Anti-brûlure", medicament.getNomObjet());
    }

    @Test
    void testUtiliserObjetSurMonstreBrule() {
        monstre.setStatut(new Brule());
        assertEquals("Brule", monstre.getStatut().getLabelStatut());
        
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
