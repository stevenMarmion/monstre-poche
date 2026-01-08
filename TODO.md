TODO

- Gérer utilisation objet par interface en mode online (pas implémenté)
- corriger combatlocal gereChoixAction , avec gestion d'erreur. Actuellement si erreur ca passe le tour. Pas obligé de fix !!!
- Faire README complet
- Corriger entrée pour client quand lancement online par terminal
- Refactoring controllers
- Refactoring views
- Note spéciale pour objectSelectionView qui pue sa mère, à refaire, on doit faire comme le reste des vues, c-a-dire on met dans le constructeur un controller (voir monstrePocheUI.java)
- Corriger singleton, demander pourquoi volatile en attribut et pourquoi synchronized en fonction getInstance ???
- Mettre en image docker la dernière version de monstre poche pour la faire tester à tout le monde (mettre sur un repo docker hub, ajouter Dockerfile, ajouter README étape pour publication)
- Ajouter images monstre
- Regénérer fichier attacks et monsters avec les vrais pokémons
- Ajouter exception personnalisés
- Régler bug interface changement type de monstre quand on switch, le type ne change pas
- Régler bug models : 
    - Si on applique potion ou capacité spéciale --> vérifier si monstre mort car sinon pour monstre plante --> régénération infinie
    - Si on attaque un monstre est qu'il est mort, son attaque nous touche pas