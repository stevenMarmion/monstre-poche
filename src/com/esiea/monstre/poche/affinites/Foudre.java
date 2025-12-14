package src.com.esiea.monstre.poche.affinites;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.etats.Paralyse;

public class Foudre extends Type {
    private static final double CHANCE_PARALYSIE = 0; // à changer, jsp c'est quoi les chances

    public Foudre() {
        this.labelType = "Foudre";
        this.fortContre = new Eau();
        this.faibleContre = new Terre();
    }

    public void paralyser(Monstre cible) {
        if (Math.random() < CHANCE_PARALYSIE) {
            cible.setStatut(new Paralyse());
        }
    }
}
