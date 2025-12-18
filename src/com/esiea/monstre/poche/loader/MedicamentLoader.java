package src.com.esiea.monstre.poche.loader;

import src.com.esiea.monstre.poche.inventaire.medicaments.Medicament;

public class MedicamentLoader extends ResourceLoader<Medicament> {

    public MedicamentLoader(String cheminFichier) {
        super(cheminFichier);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected Medicament parseLigne(String ligne, int numeroLigne) throws ParseException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseLigne'");
    }
    
    /**
     * Retourne une attaque par son nom
     */
    @Override
    public Medicament getRessourceParNom(String nom) {
        return ressources.stream()
                .filter(a -> a.getNomObjet().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }

}
