# POO Java TP Monstre - Maven Edition

## Membres

- MARMION Steven
- RIBEROU Kylian

Etudiants en 3A FISA à l'ESIEA


## Description du projet

Monstre Poche est un jeu de combat au tour par tour développé en Java avec JavaFX, inspiré de l'univers des monstres de poche. Le projet implémente une architecture MVC complète avec un système de combat stratégique incluant des types, des statuts et des objets.

---

## Démarrage rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Installation

1. **Cloner le projet**
```bash
git clone <url-du-repo>
cd monstre-poche
```

3. **Compiler le projet**
```bash
mvn clean compile
```

4. **Créer le JAR exécutable**
```bash
mvn package
```

5. **Exécuter l'application**
```bash
# Ou avec exec:java (choisir son mode de lancement)
mvn exec:java

# Ou lancer l'interface graphique directement Avec Maven
mvn javafx:run

# Ou directement avec le JAR
java -jar target/monstre-poche-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## Structure du projet

```
monstre-poche/
├── pom.xml                          # Configuration Maven
├── README.md                        
├── docs/
│   └── uml/                         # Diagrammes UML
│       └── diagramme_classe/src/
│           ├── 00_vue_ensemble.puml
│           ├── 01_models_core.puml
│           ├── 02_types.puml
│           ├── 03_statuts.puml
│           ├── 04_items.puml
│           ├── 05_combat.puml
│           └── 06_mvc.puml
│
├── src/
│   ├── main/
│   │   ├── java/com/esiea/monstre/poche/
│   │   │   ├── MonstrePoche.java           # Point d'entrée console
│   │   │   │
│   │   │   ├── controllers/                # Contrôleurs MVC
│   │   │   │   ├── INavigationCallback.java
│   │   │   │   ├── battle/
│   │   │   │   ├── menu/
│   │   │   │   ├── selection/
│   │   │   │   └── setup/
│   │   │   │
│   │   │   ├── models/                     # Modèles métier
│   │   │   │   ├── core/                   # Classes de base
│   │   │   │   ├── battle/                 # Système de combat
│   │   │   │   ├── types/                  # Types de monstres
│   │   │   │   ├── status/                 # Statuts
│   │   │   │   ├── items/                  # Objets
│   │   │   │   ├── game/                   # Gestion du jeu
│   │   │   │   └── network/                # Jeu en ligne
│   │   │   │
│   │   │   └── views/                      # Vues JavaFX
│   │   │       ├── MonstrePocheUI.java     # Point d'entrée GUI
│   │   │       └── gui/
│   │   │
│   │   └── resources/                      # Ressources
│   │       ├── attaques.txt
│   │       ├── monstres.txt
│   │       ├── objets.txt
│   │       ├── css/
│   │       └── images/
│   │
│   └── test/
│       └── java/                           # Tests unitaires
```

---

## Commandes Maven utiles

### Développement
```bash
# Nettoyer le projet
mvn clean

# Compiler uniquement
mvn compile

# Compiler et tester
mvn clean test

# Empaqueter (compile + test + JAR)
mvn package

# Installer dans le dépôt local
mvn install
```

### Exécution
```bash
# Exécuter l'interface graphique JavaFX
mvn javafx:run

# Exécuter le mode console
mvn exec:java

# Exécuter une classe spécifique
mvn exec:java -Dexec.mainClass="com.esiea.monstre.poche.MonstrePoche"

# Exécuter avec des arguments
mvn exec:java -Dexec.args="arg1 arg2"
```

### Tests
```bash
# Exécuter les tests unitaires
mvn test

# Exécuter un test spécifique
mvn test -Dtest=MonstreTest
```

### Documentation
```bash
# Générer la Javadoc
mvn javadoc:javadoc

# Ouvrir la Javadoc générée
open target/site/apidocs/index.html
```

---

## Génération des diagrammes UML

Les diagrammes de classes sont disponibles dans `docs/uml/diagramme_classe/src/` et organisés en modules :

- `00_vue_ensemble.puml` : Vue d'ensemble simplifiée
- `01_models_core.puml` : Classes de base (Monstre, Joueur, Attaque, Terrain)
- `02_types.puml` : Hiérarchie des types
- `03_statuts.puml` : Statuts de monstre et terrain
- `04_items.puml` : Système d'objets
- `05_combat.puml` : Système de combat
- `06_mvc.puml` : Architecture MVC

### Générer les diagrammes

#### Avec PlantUML en ligne de commande
```bash
cd docs/uml/diagramme_classe/src
plantuml *.puml
```

#### Avec Docker
```bash
docker run --rm -v $(pwd)/docs/uml/diagramme_classe/src:/data plantuml/plantuml *.puml
```

#### Avec l'extension VSCode
1. Installer l'extension "PlantUML"
2. Ouvrir un fichier `.puml`
3. Appuyer sur `Alt+D` pour prévisualiser

---

## Technologies utilisées

- **Langage** : Java 17
- **Build Tool** : Maven 3.6+
- **Interface graphique** : JavaFX 21.0.2
- **Tests** : JUnit 5.9.3
- **Architecture** : MVC (Model-View-Controller)

---

## Fonctionnalités implémentées

### Système de combat

#### Combat au tour par tour
- Système de combat stratégique avec gestion des tours
- Ordre d'attaque basé sur la statistique de vitesse des monstres
- Gestion automatique du changement de monstre en cas de KO
- Vérification de fin de combat (tous les monstres d'une équipe KO)

#### Modes de jeu
- **Mode Local** : Combat entre deux joueurs sur la même machine
- **Mode Bot** : Combat contre une intelligence artificielle
- **Mode En Ligne** : Combat en réseau entre deux joueurs (système client-serveur)

### Système de types

#### Types élémentaires
- **Eau** : Fort contre Feu, Faible contre Foudre
  - Capacité spéciale : Inondation du terrain (probabilité configurable)
  - Effet : Fait chuter les monstres non-Eau
- **Feu** : Fort contre Nature, Faible contre Eau
  - Capacité spéciale : Brûlure de la cible
  - Effet : Dégâts continus à chaque tour
- **Foudre** : Fort contre Eau, Faible contre Terre
  - Capacité spéciale : Paralysie de la cible
  - Effet : Risque de rater son attaque
- **Terre** : Fort contre Foudre, Faible contre Nature
  - Capacité spéciale : Fuite sous terre
  - Effet : Augmentation temporaire de la défense
- **Nature** : Classe abstraite pour Plante et Insecte
  - Capacité spéciale : Récupération de PV sur terrain inondé
- **Plante** : Fort contre Eau, Faible contre Feu
  - Capacité spéciale : Soigne les altérations de statut
- **Insecte** : Fort contre Plante, Faible contre Feu
  - Capacité spéciale : Empoisonnement tous les 3 tours
- **Normal** : Fort contre Terre, Faible contre Nature
  - Type équilibré sans capacité spéciale

#### Système d'avantages de types
- Multiplicateur de dégâts x2 si type fort contre adversaire
- Multiplicateur de dégâts x0.5 si type faible contre adversaire
- Affichage des informations de type pendant le combat

### Système de statuts

#### Statuts de monstre
- **Brûlé** : Perd des PV à chaque tour (1/10 de l'attaque)
- **Paralysé** :
  - Durée maximale de 6 tours
  - Probabilité croissante de sortir de la paralysie
- **Empoisonné** : Perd des PV à chaque tour (1/10 de l'attaque)
- **Sous Terre** :
  - Défense doublée pendant 1 à 3 tours
  - Retour automatique à la normale après la durée
- **Normal** : Aucune altération

#### Statuts de terrain
- **Inondé** :
  - Déclenché par les monstres de type Eau
  - Durée de 1 à 3 tours
  - Effets sur les monstres non-Eau :
    - Soigne les statuts Brûlé et Empoisonné
    - Risque de chute (dégâts et attaque ratée)
  - Permet aux types Nature de récupérer des PV
  - Retrait automatique quand le monstre source quitte le terrain
- **Asséché** : État normal du terrain

### Système d'attaques

#### Mécaniques d'attaque
- **Attaques spéciales** :
  - 4 attaques maximum par monstre
  - PP (Points de Pouvoir) limités par attaque
  - Puissance et type personnalisables
  - Probabilité d'échec configurable
- **Attaque à mains nues** :
  - Disponible quand toutes les attaques sont épuisées
  - PP illimités
  - Puissance faible basée sur les statistiques
- **Calcul des dégâts** :
  - Formule prenant en compte attaque, défense et puissance
  - Coefficient aléatoire pour la variance
  - Multiplicateur de type (avantage/désavantage)

### Système d'objets

#### Potions
- **Potion de Santé** : Restaure les points de vie (max = PV max)
- **Potion de Dégât** : Augmente l'attaque du monstre
- **Potion de Vitesse** : Augmente la vitesse du monstre

#### Médicaments
- **Médicament Anti-Brûlure** : Soigne le statut Brûlé
- **Médicament Anti-Paralysie** : Soigne le statut Paralysé
- **Médicament Anti-Poison** : Soigne le statut Empoisonné

#### Gestion d'inventaire
- Maximum 5 objets par joueur
- Utilisation d'objet compte comme une action de tour
- Suppression automatique après utilisation

### Système de joueurs

#### Gestion d'équipe
- Maximum 3 monstres par équipe
- Sélection du monstre actif
- Changement de monstre pendant le combat
- Détection automatique de défaite (tous monstres KO)

#### Intelligence Artificielle (Bot)
- Choix automatique de la meilleure attaque disponible
- Stratégie basée sur les PP restants
- Gestion autonome des tours de combat

### Interface utilisateur

#### Interface graphique JavaFX
- **Menu principal** :
  - Sélection du mode de jeu (Local, Bot, En Ligne)
  - Navigation claire entre les écrans
- **Écran de sélection** :
  - Sélection de 3 monstres parmi les disponibles
  - Sélection de 4 attaques par monstre (filtrées par type)
  - Sélection de 5 objets pour l'inventaire
- **Écran de combat** :
  - Affichage des deux monstres en combat
  - Barres de PV avec code couleur (vert, orange, rouge)
  - Informations de statut en temps réel
  - Système de logs détaillés dans un panneau dédié
  - Boutons d'action : Attaque, Sac, Pokémon, Fuite
  - Sélection d'attaque avec affichage des PP
  - Changement de monstre (filtrage des monstres KO)
  - Utilisation d'objets
- **Écran de victoire** :
  - Affichage du vainqueur
  - Retour au menu principal

#### Design moderne
- Image de fond personnalisée pour le combat
- Cartes colorées par type de monstre
- Animations de transition
- Effets de survol sur les boutons
- Panneau de logs avec scroll automatique
- Interface responsive

### Système de logs

#### Logger de combat
- Enregistrement de toutes les actions de combat
- Formatage des messages pour l'interface graphique
- Séparation des logs par tour
- Affichage des informations importantes :
  - Début et fin de combat
  - Actions des joueurs
  - Dégâts infligés
  - Changements de statut
  - Effets de type
  - Utilisation d'objets

### Système de ressources

#### Chargement des données
- **GameResourcesFactory** : Pattern Singleton pour l'accès aux ressources
- **GameResourcesLoader** : Chargement depuis fichiers texte
- Fichiers de configuration :
  - `monstres.txt` : Définition des monstres (nom, stats, type)
  - `attaques.txt` : Définition des attaques (nom, PP, puissance, type)
  - `objets.txt` : Définition des objets (nom, type, effet)

#### Persistance
- Sérialisation des objets pour le jeu en ligne
- Copie profonde des monstres et attaques pour éviter les modifications croisées

### Architecture logicielle

#### Pattern MVC (Model-View-Controller)
- **Models** : Logique métier et données
- **Views** : Interface utilisateur JavaFX
- **Controllers** : Coordination entre Models et Views
- **INavigationCallback** : Interface de navigation entre les vues

#### Patterns de conception
- **Template Method** : Classe abstraite `Combat` avec implémentations spécifiques
- **Factory** : `GameResourcesFactory` pour la création de ressources
- **Singleton** : Instance unique de `GameResourcesFactory`
- **Strategy** : Différentes stratégies de combat (Local, Bot, En Ligne)

#### Principes SOLID
- Séparation des responsabilités (SRP)
- Hiérarchie de types extensible (OCP)
- Interfaces bien définies (ISP)
- Injection de dépendances dans les contrôleurs (DIP)

### Fonctionnalités techniques

#### Gestion du réseau
- Serveur de jeu en ligne avec sockets
- Client de connexion au serveur
- Protocole de communication par événements
- Sérialisation des objets pour l'envoi réseau

#### Gestion des erreurs
- Validation des entrées utilisateur
- Gestion des cas limites (PP épuisés, monstres KO)
- Messages d'erreur clairs dans l'interface
- Logs détaillés pour le debugging

#### Performance
- Utilisation de JavaFX pour une interface fluide
- Gestion efficace de la mémoire avec copie d'objets
- Cache des ressources chargées

---

## Auteurs

Projet réalisé dans le cadre du cours de POO Java à l'ESIEA.
