package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Normal extends StatutMonstre {
    
    public Normal() {
        this.labelStatut = "Normal";
    }

    @Override
    public void appliquerEffets(Monstre cible) {
        // on applique aucun effet car le statut est normal
    }
}
