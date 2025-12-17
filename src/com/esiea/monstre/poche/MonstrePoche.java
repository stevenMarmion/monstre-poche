package src.com.esiea.monstre.poche;

// import src.com.esiea.monstre.poche.actions.Attaque;
import src.com.esiea.monstre.poche.loader.AttaqueLoader;

public class MonstrePoche {
    public static void main(String[] args) {
        AttaqueLoader attaqueLoader = new AttaqueLoader("/home/steven/workspace/ESIEA/monstre-poche/resources/attacks.txt");
        attaqueLoader.charger();
    }
}
