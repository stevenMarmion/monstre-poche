package com.esiea.monstre.poche.entites;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.affinites.Normal;

class JoueurTest {
    private Joueur joueur;
    private Monstre monstre1;
    private Monstre monstre2;
    private Monstre monstre3;
    private Monstre monstre4;

    @BeforeEach
    void setUp() {
        joueur = new Joueur("Steven");
        monstre1 = new Monstre("Pikachu", 100, 50, 40, 90, new ArrayList<Attaque>(), new Normal());
        monstre2 = new Monstre("Bulbizarre", 120, 45, 50, 45, new ArrayList<Attaque>(), new Normal());
        monstre3 = new Monstre("Salameche", 110, 60, 35, 65, new ArrayList<Attaque>(), new Normal());
        monstre4 = new Monstre("Carapuce", 115, 48, 65, 43, new ArrayList<Attaque>(), new Normal());
    }

    @Test
    void testConstructeur() {
        assertEquals("Steven", joueur.getNomJoueur());
        assertNotNull(joueur.getMonstres());
        assertEquals(0, joueur.getMonstres().size());
        assertNotNull(joueur.getObjets());
        assertEquals(0, joueur.getObjets().size());
    }

    @Test
    void testAjouterMonstre() {
        joueur.ajouterMonstre(monstre1);
        assertEquals(1, joueur.getMonstres().size());
        assertTrue(joueur.getMonstres().contains(monstre1));
    }

    @Test
    void testAjouterMonstreDuplique() {
        joueur.ajouterMonstre(monstre1);
        joueur.ajouterMonstre(monstre1);
        assertEquals(1, joueur.getMonstres().size());
    }

    @Test
    void testAjouterMonstreMax3() {
        joueur.ajouterMonstre(monstre1);
        joueur.ajouterMonstre(monstre2);
        joueur.ajouterMonstre(monstre3);
        assertEquals(3, joueur.getMonstres().size());
        
        // Tentative d'ajouter un 4ème monstre
        joueur.ajouterMonstre(monstre4);
        assertEquals(3, joueur.getMonstres().size());
    }

    @Test
    void testSetMonstreActuel() {
        joueur.ajouterMonstre(monstre1);
        joueur.setMonstreActuel(monstre1);
        assertEquals(monstre1, joueur.getMonstreActuel());
    }

    @Test
    void testSontMonstresMortsAvecMonstreVivant() {
        monstre1.setPointsDeVie(50);
        joueur.ajouterMonstre(monstre1);
        assertFalse(joueur.sontMonstresMorts());
    }

    @Test
    void testSontMonstresMortsAvecMonstreMort() {
        monstre1.setPointsDeVie(0);
        joueur.ajouterMonstre(monstre1);
        assertTrue(joueur.sontMonstresMorts());
    }

    @Test
    void testSontMonstresMortsAvecPlusieursMonstres() {
        monstre1.setPointsDeVie(0);
        monstre2.setPointsDeVie(0);
        monstre3.setPointsDeVie(50);
        
        joueur.ajouterMonstre(monstre1);
        joueur.ajouterMonstre(monstre2);
        joueur.ajouterMonstre(monstre3);
        
        assertFalse(joueur.sontMonstresMorts());
    }

    @Test
    void testSontMonstresMortsTousLesMonstres() {
        monstre1.setPointsDeVie(0);
        monstre2.setPointsDeVie(0);
        monstre3.setPointsDeVie(0);
        
        joueur.ajouterMonstre(monstre1);
        joueur.ajouterMonstre(monstre2);
        joueur.ajouterMonstre(monstre3);
        
        assertTrue(joueur.sontMonstresMorts());
    }

    @Test
    void testToString() {
        String result = joueur.toString();
        assertTrue(result.contains("Steven"));
        assertTrue(result.contains("Joueur"));
    }

    @Test
    void testEquals() {
        Joueur joueur2 = new Joueur("Steven");
        // Les joueurs avec le même nom et les mêmes listes vides sont égaux
        assertTrue(joueur.equals(joueur2));
        
        // Test avec null
        assertFalse(joueur.equals(null));
        
        // Test avec le même objet
        assertTrue(joueur.equals(joueur));
        
        // Test avec un objet d'un autre type
        assertFalse(joueur.equals("Steven"));
    }

    @Test
    void testEqualsAvecNomDifferent() {
        Joueur joueur2 = new Joueur("Kylian");
        assertFalse(joueur.equals(joueur2));
    }
}
