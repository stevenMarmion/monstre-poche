package src.com.esiea.monstre.poche.entites;

public class Joueur {
    private String nomJoueur;
    private boolean enAttenteAtion;
    private Monstre[] monstres;
    private Monstre monstreActuel;

    public Joueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.enAttenteAtion = false;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public boolean isEnAttenteAtion() {
        return enAttenteAtion;
    }

    public Monstre[] getMonstres() {
        return monstres;
    }

    public Monstre getMonstreActuel() {
        return monstreActuel;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setEnAttenteAtion(boolean enAttenteAtion) {
        this.enAttenteAtion = enAttenteAtion;
    }

    public void setMonstres(Monstre[] monstres) {
        this.monstres = monstres;
    }

    public void setMonstreActuel(Monstre monstreActuel) {
        this.monstreActuel = monstreActuel;
    }

    public void ajouterMonstre(Monstre monstre) {
        if (monstres.length >= 3) {
            return;
        }

        boolean estMonstrePresentDansListe = false;
        int cptMonstres = 0;
        for (Monstre m : monstres) {
            if (m == monstre) {
                estMonstrePresentDansListe = true;
                break;
            }
            cptMonstres++;
        }
        if (!estMonstrePresentDansListe ) {
            monstres[cptMonstres] = monstre;
        }
    }

    public void choisirAttaque() {
        // Logique pour choisir une attaque
    }

    public void choisirObjet() {
        // Logique pour choisir un objet
    }

    public void changerMonstre() {
        // Logique pour changer de monstre
    }

    public boolean sontMonstresMorts() {
        // Logique pour vérifier si tous les monstres du joueur sont morts
        return false; // Valeur par défaut, à remplacer par la logique réelle
    }
}
