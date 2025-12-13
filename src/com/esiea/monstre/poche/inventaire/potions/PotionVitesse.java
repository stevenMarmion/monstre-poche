package src.com.esiea.monstre.poche.inventaire.potions;

import src.com.esiea.monstre.poche.entites.Monstre;

public class PotionVitesse  extends Potion {
    private int pointsDeVitesse;
    
    public PotionVitesse(String nomObjet, int pointsDeVitesse) {
        super(nomObjet);
        this.pointsDeVitesse = pointsDeVitesse;
    }

    public int getPointsDeVitesse() {
        return pointsDeVitesse;
    }

    public void setPointsDeVitesse(int pointsDeVitesse) {
        this.pointsDeVitesse = pointsDeVitesse;
    }

    @Override
    public void utiliserPotion(Monstre cible) {}
    
}
