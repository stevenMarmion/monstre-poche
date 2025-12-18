package src.com.esiea.monstre.poche.loader;

import src.com.esiea.monstre.poche.entites.Monstre;

public class MonstreLoader extends ResourceLoader<Monstre> {

    public MonstreLoader(String cheminFichier) {
        super(cheminFichier);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected Monstre parseLigne(String ligne, int numeroLigne) throws ParseException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseLigne'");
    }

    /**
     * Retourne une attaque par son nom
     */
    @Override
    public Monstre getRessourceParNom(String nom) {
        return ressources.stream()
                .filter(a -> a.getNomMonstre().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }
}
