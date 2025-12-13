package src.com.esiea.monstre.poche.actions;

public class Attaque {
    private String nomAttaque;
    private int nbUtilisations;
    private int puissanceAttaque;
    private double probabiliteEchec;

    public Attaque(String nomAttaque, int nbUtilisations, int puissanceAttaque, double probabiliteEchec) {
        this.nomAttaque = nomAttaque;
        this.nbUtilisations = nbUtilisations;
        this.puissanceAttaque = puissanceAttaque;
        this.probabiliteEchec = probabiliteEchec;
    }

    public String getNomAttaque() {
        return nomAttaque;
    }

    public int getNbUtilisations() {
        return nbUtilisations;
    }

    public int getPuissanceAttaque() {
        return puissanceAttaque;
    }

    public double getProbabiliteEchec() {
        return probabiliteEchec;
    }

    public void setNomAttaque(String nomAttaque) {
        this.nomAttaque = nomAttaque;
    }

    public void setNbUtilisations(int nbUtilisations) {
        this.nbUtilisations = nbUtilisations;
    }

    public void setPuissanceAttaque(int puissanceAttaque) {
        this.puissanceAttaque = puissanceAttaque;
    }

    public void setProbabiliteEchec(double probabiliteEchec) {
        this.probabiliteEchec = probabiliteEchec;
    }
}
