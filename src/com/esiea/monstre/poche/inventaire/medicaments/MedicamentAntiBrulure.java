package src.com.esiea.monstre.poche.inventaire.medicaments;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.Normal;

public class MedicamentAntiBrulure extends Medicament {

    public MedicamentAntiBrulure(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserObjet(Monstre cible) {
        if (cible.getStatut().getLabelStatut().equals("Brulure")) {
            System.out.println("Utilisation de " + this.nomObjet + " sur " + cible.getNomMonstre());
            cible.setStatut(new Normal());
        }
    }
}
