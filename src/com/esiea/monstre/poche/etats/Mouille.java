package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Mouille extends StatutMonstre {
    
    public Mouille() {
        this.labelStatut = "Mouille";
    }

    public boolean glisser(Monstre cible) {
        return true;
    }
}
