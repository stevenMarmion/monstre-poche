package com.esiea.monstre.poche;

import java.util.Scanner;

import com.esiea.monstre.poche.models.game.GameApp;
import com.esiea.monstre.poche.models.game.GameVisual;

public class MonstrePoche {
    private static final int LANCEMENT_PAR_INTERFACE = 1;
    private static final int LANCEMENT_PAR_TERMINAL = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        gereModeLancement(GameVisual.afficherDemandeInterfaceTerminal(scanner));
        scanner.close();
    }

    public static void gereModeLancement(int modeLancement) {
        switch (modeLancement) {
            case LANCEMENT_PAR_INTERFACE:
                GameApp.startAppInterface();
                break;
            case LANCEMENT_PAR_TERMINAL:
                GameApp.startAppTerminal();
                break;
            default:
                break;
        }
    }
}

