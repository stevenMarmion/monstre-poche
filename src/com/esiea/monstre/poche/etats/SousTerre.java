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

    public void appliquerEffets(Monstre cible) {
        if (this.nbToursEffet > 0) {
            if (!cible.getStatut().getLabelStatut().equals("SousTerre")) {
                cible.setDefense(cible.getDefense() * 2);
            }
            this.decrementerNbToursAvecEffet();
            sortirSousTerre(cible);
        }
    }

    public void sortirSousTerre(Monstre cible) {
        if (this.nbToursAvecEffet == 0) {
            cible.setStatut(new Normal());
            cible.setDefense(cible.getDefense() / 2);
        }
    }
}
