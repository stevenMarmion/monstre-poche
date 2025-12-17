package src.com.esiea.monstre.poche.etats;

import src.com.esiea.monstre.poche.entites.Monstre;

public class Empoisonne extends StatutMonstre {
    
    public Empoisonne() {
        this.labelStatut = "Empoisonne";
    }

    @Override
    public void appliquerEffets(Monstre cible) {
        double degatsPoison = cible.calculeDegatsAttaque(cible, cible, null) / 10; // Le poison fait perdre 10% des PV max à chaque tour
        cible.setPointsDeVie(cible.getPointsDeVie() - degatsPoison);
        System.out.println(cible.getNomMonstre() + " subit " + degatsPoison + " points de dégâts de poison !");
    }
}
