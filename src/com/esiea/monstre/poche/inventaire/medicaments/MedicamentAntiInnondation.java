// package src.com.esiea.monstre.poche.inventaire.medicaments;

// import src.com.esiea.monstre.poche.entites.Monstre;
// import src.com.esiea.monstre.poche.entites.Terrain;
// import src.com.esiea.monstre.poche.etats.Normal;

// public class MedicamentAntiInnondation extends Medicament {

//     public MedicamentAntiInnondation(String nomObjet) {
//         super(nomObjet);
//     }

//     @Override
//     public void utiliserMedicament(Terrain terrain) {
//         if (terrain.getStatutTerrain().getLabelStatut().equals("Innonde")) {
//             System.out.println("Utilisation de " + this.nomObjet + " sur le terrain " + terrain.getNomTerrain());
//             terrain.setStatutTerrain(new Normal());
//         }
//     }
// }