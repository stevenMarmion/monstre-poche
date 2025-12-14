package src.com.esiea.monstre.poche.etats;

public abstract class StatutMonstre {
    protected String labelStatut;
    protected int nbToursEffet;
    protected int nbToursAvecEffet;

    public void decrementerNbToursAvecEffet() {
        if (nbToursAvecEffet > 0) {
            nbToursAvecEffet--;
        }
    }

    public String getLabelStatut() {
        return this.labelStatut;
    }
}
