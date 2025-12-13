package src.com.esiea.monstre.poche.inventaire.potions;

import src.com.esiea.monstre.poche.entites.Monstre;

public class PotionDegat extends Potion {
    private int pointsDeDegat;

    public PotionDegat(String nomObjet, int pointsDeDegat) {
        super(nomObjet);
        this.pointsDeDegat = pointsDeDegat;
    }

    public int getPointsDeDegat() {
        return pointsDeDegat;
    }

    public void setPointsDeDegat(int pointsDeDegat) {
        this.pointsDeDegat = pointsDeDegat;
    }

    @Override
    public void utiliserPotion(Monstre cible) {}
    
}
