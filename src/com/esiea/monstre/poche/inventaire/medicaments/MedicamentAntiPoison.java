package src.com.esiea.monstre.poche.inventaire.medicaments;

import src.com.esiea.monstre.poche.entites.Monstre;

public class MedicamentAntiPoison extends Medicament {

    public MedicamentAntiPoison(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserMedicament(Monstre cible) {}
    
}
