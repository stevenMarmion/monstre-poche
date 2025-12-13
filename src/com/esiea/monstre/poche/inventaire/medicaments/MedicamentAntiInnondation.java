package src.com.esiea.monstre.poche.inventaire.medicaments;

import src.com.esiea.monstre.poche.entites.Monstre;

public class MedicamentAntiInnondation extends Medicament {

    public MedicamentAntiInnondation(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserMedicament(Monstre cible) {}
}