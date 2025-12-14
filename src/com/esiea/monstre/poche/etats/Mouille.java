package src.com.esiea.monstre.poche.etats;

public class Mouille extends StatutMonstre {
    
    public Mouille(int nbToursEffet) {
        this.labelStatut = "Mouille";
        this.nbToursEffet = nbToursEffet;
        this.nbToursAvecEffet = nbToursEffet;
    }
}
