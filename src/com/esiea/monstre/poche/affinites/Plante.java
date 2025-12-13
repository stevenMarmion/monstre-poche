package src.com.esiea.monstre.poche.affinites;

public class Plante extends Nature {
    private double chanceSoigner;

    public Plante() {
        this.labelType = "Plante";
    }

    public double getChanceSoigner() {
        return chanceSoigner;
    }

    public void setChanceSoigner(double chanceSoigner) {
        this.chanceSoigner = chanceSoigner;
    }

    public boolean soigner() {
        return false;
    }

    public void supprimerEffetStatut() {}
}
