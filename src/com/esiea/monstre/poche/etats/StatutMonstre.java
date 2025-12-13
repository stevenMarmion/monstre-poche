package src.com.esiea.monstre.poche.etats;

public abstract class StatutMonstre {
    protected String labelStatut;
    protected int nbToursEffet;
    protected double chanceEffet;

    public void decrementerNbToursEffet() {
        if (nbToursEffet > 0) {
            nbToursEffet--;
        }
    }
}
