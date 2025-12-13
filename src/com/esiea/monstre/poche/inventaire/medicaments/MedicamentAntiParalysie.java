package src.com.esiea.monstre.poche.inventaire.medicaments;

import src.com.esiea.monstre.poche.entites.Monstre;

public class MedicamentAntiParalysie extends Medicament {

    public MedicamentAntiParalysie(String nomObjet) {
        super(nomObjet);
    }

    @Override
    public void utiliserMedicament(Monstre cible) {}
}