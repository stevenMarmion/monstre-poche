package src.com.esiea.monstre.poche.etats;

public class SousTerre extends StatutMonstre {
    private static final int NB_TOURS_MAX_FUITE = 3;
    private static final int NB_TOURS_MIN_FUITE = 1;

    public SousTerre() {
        this.labelStatut = "SousTerre";
        this.nbToursEffet = (int) (Math.random() * NB_TOURS_MAX_FUITE) + NB_TOURS_MIN_FUITE;;
        this.nbToursAvecEffet = nbToursEffet;
    }
}
