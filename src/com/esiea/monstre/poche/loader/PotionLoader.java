package src.com.esiea.monstre.poche.loader;

import src.com.esiea.monstre.poche.inventaire.potions.Potion;

public class PotionLoader extends ResourceLoader<Potion> {
    
    public PotionLoader(String cheminFichier) {
        super(cheminFichier);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected Potion parseLigne(String ligne, int numeroLigne) throws ParseException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parseLigne'");
    }

    /**
     * Retourne une attaque par son nom
     */
    @Override
    public Potion getRessourceParNom(String nom) {
        return ressources.stream()
                .filter(a -> a.getNomObjet().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }
    
}
