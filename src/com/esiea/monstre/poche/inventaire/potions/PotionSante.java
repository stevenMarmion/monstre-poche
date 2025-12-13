package src.com.esiea.monstre.poche.inventaire.potions;

import src.com.esiea.monstre.poche.entites.Monstre;

public class PotionSante extends Potion {
    private int pointsDeSoin;

    public PotionSante(String nomObjet, int pointsDeSoin) {
        super(nomObjet);
        this.pointsDeSoin = pointsDeSoin;
    }

    public int getPointsDeSoin() {
        return pointsDeSoin;
    }

    public void setPointsDeSoin(int pointsDeSoin) {
        this.pointsDeSoin = pointsDeSoin;
    }

    @Override
    public void utiliserPotion(Monstre cible) {}
}
