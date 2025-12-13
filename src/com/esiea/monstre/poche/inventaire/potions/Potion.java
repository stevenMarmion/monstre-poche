package src.com.esiea.monstre.poche.inventaire.potions;

import src.com.esiea.monstre.poche.entites.Monstre;
import src.com.esiea.monstre.poche.inventaire.Objet;

public abstract class Potion extends Objet {

    public Potion(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    public abstract void utiliserPotion(Monstre cible);
}