package com.esiea.monstre.poche.models.game.resources;

import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.items.Objet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton - Factory pour accéder aux ressources du jeu (monstres, attaques, objets).
 * Les ressources sont chargées une seule fois au démarrage de l'application.
 */
public class GameResourcesFactory {

    private static volatile GameResourcesFactory instance;

    private final Map<String, Attaque> attaquesTemplate;
    private final Map<String, Monstre> monstresTemplate;
    private final Map<String, Objet> objetsTemplate;

    /**
     * Constructeur privé - Pattern Singleton.
     * Charge toutes les ressources depuis les fichiers de configuration.
     */
    private GameResourcesFactory() {
        GameResourcesLoader loader = new GameResourcesLoader();

        this.attaquesTemplate = loader.getListeAttaquesDeBase().stream()
                .collect(Collectors.toMap(
                        a -> a.getNomAttaque().toLowerCase(),
                        a -> a
                ));

        this.monstresTemplate = loader.getListeMonstresDeBase().stream()
                .collect(Collectors.toMap(
                        m -> m.getNomMonstre().toLowerCase(),
                        m -> m
                ));

        this.objetsTemplate = loader.getListeObjetsDeBase().stream()
                .collect(Collectors.toMap(
                        o -> o.getNomObjet().toLowerCase(),
                        o -> o
                ));
    }

    /**
     * Retourne l'instance unique de GameResourcesFactory.
     * Thread-safe avec double-check locking pattern.
     */
    public static GameResourcesFactory getInstance() {
        if (instance == null) {
            synchronized (GameResourcesFactory.class) {
                if (instance == null) {
                    instance = new GameResourcesFactory();
                }
            }
        }
        return instance;
    }

    // =====================================================
    //                ATTAQUES
    // =====================================================

    public Attaque getAttaque(String nom) {
        Attaque base = getTemplate(attaquesTemplate, nom);
        return base.copyOf();
    }

    public List<Attaque> getToutesLesAttaques() {
        return attaquesTemplate.values().stream()
                .map(Attaque::copyOf)
                .toList();
    }

    // =====================================================
    //                MONSTRES
    // =====================================================

    public Monstre getMonstre(String nom) {
        Monstre base = getTemplate(monstresTemplate, nom);
        return base.copyOf();
    }

    public List<Monstre> getTousLesMonstres() {
        return monstresTemplate.values().stream()
                .map(Monstre::copyOf)
                .toList();
    }

    // =====================================================
    //                OBJETS
    // =====================================================

    public Objet getObjet(String nom) {
        Objet base = getTemplate(objetsTemplate, nom);
        return base.copyOf();
    }

    public List<Objet> getTousLesObjets() {
        return objetsTemplate.values().stream()
                .map(Objet::copyOf)
                .toList();
    }

    // =====================================================
    //                UTILITAIRE
    // =====================================================

    private <T> T getTemplate(Map<String, T> map, String nom) {
        T t = map.get(nom.toLowerCase());
        if (t == null) {
            throw new IllegalArgumentException("Ressource inconnue : " + nom);
        }
        return t;
    }
}
