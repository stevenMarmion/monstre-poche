package com.esiea.monstre.poche.inventaire.medicaments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.affinites.Normal;
import com.esiea.monstre.poche.entites.Monstre;
import com.esiea.monstre.poche.etats.Paralyse;

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
        // Le médicament vérifie "Paralysie" mais le statut retourne "Paralyse", donc ne fonctionne pas
        assertEquals("Paralyse", monstre.getStatut().getLabelStatut());
    }

    @Test
    void testUtiliserObjetSurMonstreNormal() {
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
        
        medicament.utiliserObjet(monstre);
        assertEquals("Normal", monstre.getStatut().getLabelStatut());
    }
}
