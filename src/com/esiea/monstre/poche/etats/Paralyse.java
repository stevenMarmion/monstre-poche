package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Paralyse extends StatutMonstre {

    public Paralyse() {
        this.labelStatut = "Paralyse";
    }

    public double rateAttaque(Monstre cible) {
        return 0;
    }

    public void estSortieParalysie(Monstre cible) {}
}
