package com.esiea.monstre.poche.visual;

import java.util.Scanner;

import com.esiea.monstre.poche.actions.Attaque;
import com.esiea.monstre.poche.entites.Monstre;

public class GameVisual {
    private static final String SECTION_DIVIDER = "----------------------------------------";
    
    public static void afficherTitreSection(String titre) {
        System.out.println("\n" + SECTION_DIVIDER);
        System.out.println(" " + titre);
        System.out.println(SECTION_DIVIDER);
    }

    public static void afficherSousTitre(String sousTitre) {
        System.out.println("  > " + sousTitre);
    }

    public static void afficherErreur(String message) {
        System.out.println("  [!] " + message);
    }

    public static String demanderSaisie(Scanner scanner, String invite) {
        System.out.print(invite + " ");
        return scanner.nextLine().trim();
    }

    public static String formatterMonstre(Monstre monstre) {
        return String.format("%-15s | PV:%-4.0f/%-4.0f | ATK:%-3d | DEF:%-3d | VIT:%-3d | Type:%s", monstre.getNomMonstre(), monstre.getPointsDeVie(), monstre.getPointsDeVieMax(), monstre.getAttaque(), monstre.getDefense(), monstre.getVitesse(), monstre.getTypeMonstre().getLabelType());
    }

    public static String formatterAttaque(Attaque attaque) {
        return String.format("%-18s | Puissance:%-3d | Type:%s", attaque.getNomAttaque(), attaque.getPuissanceAttaque(), attaque.getTypeAttaque().getLabelType());
    }
}
