    package main.java.com.esiea.monstre.poche.loader;

    import com.esiea.monstre.poche.actions.Attaque;
    import com.esiea.monstre.poche.affinites.*;
    import com.esiea.monstre.poche.affinites.utils.AffinitesUtils;

    /**
     * Loader pour charger les attaques depuis le fichier attaques.txt
     * Format attendu:
     *
     * Attack
     *   Name Eclair
     *   Type Electric
     *   Power 40
     *   NbUse 10
     *   Fail 0.07
     * EndAttack
     */
    public class AttaqueLoader extends ResourceLoader<Attaque> {

        private static final String SEPARATEUR = ";";
        private static final int NB_CHAMPS_ATTENDUS = 5;

        public AttaqueLoader(String cheminFichier) {
            super(cheminFichier);
        }

        @Override
        protected Attaque parseLigne(String ligne, int numeroLigne) throws ParseException {
            String[] parties = ligne.split(SEPARATEUR);

            // Vérifier le nombre de champs
            if (parties.length != NB_CHAMPS_ATTENDUS) {
                throw new ParseException(
                        String.format("Nombre de champs invalide. Attendu: %d, Trouvé: %d",
                                NB_CHAMPS_ATTENDUS, parties.length)
                );
            }

            try {
                String nomAttaque = parties[0].trim();
                int nbUtilisations = Integer.parseInt(parties[1].trim());
                int puissance = Integer.parseInt(parties[2].trim());
                double probabiliteEchec = Double.parseDouble(parties[3].trim());
                String typeStr = parties[4].trim();

                // Validation des valeurs
                if (nomAttaque.isEmpty()) {
                    throw new ParseException("Le nom de l'attaque ne peut pas être vide");
                }
                if (nbUtilisations < 0) {
                    throw new ParseException("Le nombre d'utilisations ne peut pas être négatif");
                }
                if (puissance < 0) {
                    throw new ParseException("La puissance ne peut pas être négative");
                }
                if (probabiliteEchec < 0.0 || probabiliteEchec > 1.0) {
                    throw new ParseException("La probabilité d'échec doit être entre 0.0 et 1.0");
                }

                Type type = AffinitesUtils.getTypeFromString(typeStr);

                return new Attaque(nomAttaque, nbUtilisations, puissance, probabiliteEchec, type);

            } catch (NumberFormatException e) {
                throw new ParseException("Erreur de format numérique: " + e.getMessage(), e);
            }
        }

        /**
         * Retourne une attaque par son nom
         */
        public Attaque getAttaqueParNom(String nom) {
            return ressources.stream()
                    .filter(a -> a.getNomAttaque().equalsIgnoreCase(nom))
                    .findFirst()
                    .orElse(null);
        }
    }