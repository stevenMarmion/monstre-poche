package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.Mouille;

public class Eau extends Type {
    private static final int NB_TOURS_MAX_INNONDATION = 3;
    private static final int NB_TOURS_MIN_INNONDATION = 1;

    private double probabiliteInnondation;
    private double probabiliteFaireChuter;

    public Eau() {
        this.labelType = "Eau";
        this.fortContre = new Feu();
        this.faibleContre = new Foudre();
        this.probabiliteInnondation = 0.4; // A DEFINIR, PAS DANS LE CDC
        this.probabiliteFaireChuter = 0.3; //  A DEFINIR, PAS DANS LE CDC
    }

    public boolean faitChuter(Monstre cible) {
        return Math.random() < this.probabiliteFaireChuter;
    }

    public boolean innondeTerrain(Monstre cible) {
        boolean innonde = Math.random() < this.probabiliteInnondation;
        if (innonde) {
            cible.setStatut(new Mouille((int) (Math.random() * NB_TOURS_MAX_INNONDATION) + NB_TOURS_MIN_INNONDATION));
            return true;
        } else {
            return false;
        }
    }
}
