package src.com.esiea.monstre.poche.inventaire.medicaments;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.inventaire.Objet;

public abstract class Medicament extends Objet {

    public Medicament(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    public abstract void utiliserMedicament(Monstre cible);
}
