# POO Java TP Monstre - Maven Edition

## 👥 Membres

- MARMION Steven
- RIBEROU Kylian

## 🎓 Promo

- 3A

---

## 🚀 Démarrage rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur

### Installation

1. **Cloner le projet**
```bash
git clone <url-du-repo>
cd monstre-poche
```

2. **Migrer vers Maven** (si ce n'est pas déjà fait)
```bash
chmod +x migrate-to-maven.sh
./migrate-to-maven.sh
```

3. **Compiler le projet**
```bash
mvn clean compile
```

5. **Créer le JAR exécutable**
```bash
mvn package
```

6. **Exécuter l'application**
```bash
# Avec Maven
mvn exec:java

# Ou directement avec le JAR
java -jar target/monstre-poche-1.0-SNAPSHOT.jar
```

---

## 📁 Structure du projet

```
monstre-poche/
├── pom.xml                          # Configuration Maven
├── README.md                        # Ce fichier
├── src/
│   ├── main/
│   │   ├── java/                    # Code source
│   │   │   └── com/esiea/monstre/poche/
│   │   │       ├── Combat.java
│   │   │       ├── actions/         # Attaques et actions
│   │   │       ├── affinites/       # Types (Feu, Eau, etc.)
│   │   │       ├── entites/         # Joueur et Monstre
│   │   │       ├── etats/           # Statuts des monstres
│   │   │       ├── inventaire/      # Objets, potions, médicaments
│   │   │       └── loader/          # Chargement des ressources
│   │   │
│   │   └── resources/               # Fichiers de données
│   │       ├── attaques.txt
│   │       └── monstres.txt
│   │
│   └── test/
│       └── java/                    # Tests unitaires
│           └── com/esiea/monstre/poche/
│
└── target/                          # Fichiers générés (ignorés par git)
```

---

## 🛠️ Commandes Maven utiles

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
# Exécuter la classe principale
mvn exec:java

# Exécuter une classe spécifique
mvn exec:java -Dexec.mainClass="com.esiea.monstre.poche.loader.ExempleUtilisation"

# Exécuter avec des arguments
mvn exec:java -Dexec.args="arg1 arg2"
```