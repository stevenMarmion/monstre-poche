package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class SousTerre extends StatutMonstre {
    private static final int NB_TOURS_MAX_FUITE = 3;
    private static final int NB_TOURS_MIN_FUITE = 1;

    public SousTerre() {
        this.labelStatut = "SousTerre";
        this.nbToursEffet = (int) (Math.random() * NB_TOURS_MAX_FUITE) + NB_TOURS_MIN_FUITE;;
        this.nbToursAvecEffet = nbToursEffet;
    }

    @Override
    public void appliquerEffets(Monstre cible) {
        if (this.nbToursEffet > 0) {
            cible.setDefense(cible.getDefense() * 2); // surement problématique car grande chance que def * 2 puis def * 2 * 2, etc.
            this.decrementerNbToursAvecEffet();
        }
    }
}
