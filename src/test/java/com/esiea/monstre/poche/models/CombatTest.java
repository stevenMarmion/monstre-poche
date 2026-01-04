package com.esiea.monstre.poche.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esiea.monstre.pochebis.models.affinites.Normal;
import com.esiea.monstre.pochebis.models.combats.Combat;
import com.esiea.monstre.pochebis.models.combats.CombatLocalTerminal;
import com.esiea.monstre.pochebis.models.entites.Attaque;
import com.esiea.monstre.pochebis.models.entites.Joueur;
import com.esiea.monstre.pochebis.models.entites.Monstre;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class CombatTest {
    private Combat combat;
    private Joueur joueur1;
    private Joueur joueur2;
    private Monstre monstre1;
    private Monstre monstre2;

    @BeforeEach
    void setUp() {
        joueur1 = new Joueur("Steven");
        joueur2 = new Joueur("Kylian");
        combat = new CombatLocalTerminal(joueur1, joueur2);
        
        monstre1 = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
        monstre2 = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
        
        Attaque attaque1 = new Attaque("Tonnerre", 10, 90, 0.1, new Normal());
        Attaque attaque2 = new Attaque("Fouet Liane", 15, 45, 0.0, new Normal());
        
        monstre1.ajouterAttaque(attaque1);
        monstre2.ajouterAttaque(attaque2);
        
        joueur1.ajouterMonstre(monstre1);
        joueur2.ajouterMonstre(monstre2);
    }

    @Test
    void testConstructeur() {
        assertNotNull(combat);
    }

    @Test
    void testCombatAvecMonstres() {
        joueur1.setMonstreActuel(monstre1);
        joueur2.setMonstreActuel(monstre2);
        
        assertNotNull(joueur1.getMonstreActuel());
        assertNotNull(joueur2.getMonstreActuel());
    }

    @Test
    void testJoueursAvecMonstres() {
        assertEquals(1, joueur1.getMonstres().size());
        assertEquals(1, joueur2.getMonstres().size());
    }
}
