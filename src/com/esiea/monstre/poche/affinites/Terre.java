package src.com.esiea.monstre.poche.affinites;

public class Terre extends Type {
    private double chanceFuite;

    public Terre() {
        this.labelType = "Terre";
    }

    public double getChanceFuite() {
        return chanceFuite;
    }

    public void setChanceFuite(double chanceFuite) {
        this.chanceFuite = chanceFuite;
    }

    public boolean fuit() {
        return false;
    }
}
