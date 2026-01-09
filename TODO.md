TODO

- Gérer utilisation objet par interface en mode online (pas implémenté)
- corriger combatlocal gereChoixAction , avec gestion d'erreur. Actuellement si erreur ca passe le tour. Pas obligé de fix !!!
- Faire README complet -- fait
- Faire javadoc -- fait # commenter dans le README comment la générer
- Corriger entrée pour client quand lancement online par terminal
- Refactoring controllers --> à refactoriser --> BatlleController et OnlineGameController
- Refactoring views -- fait
- Note spéciale pour showBattleOnline qui pue sa mère, à refaire, on doit faire comme le reste des vues, c-a-dire on met dans le constructeur un controller (voir monstrePocheUI.java)
- Corriger singleton, demander pourquoi volatile en attribut et pourquoi synchronized en fonction getInstance ??? -- fait
- Ajouter images monstre
- Regénérer fichier attacks et monsters avec les vrais pokémons -- fait
- Ajouter exception personnalisés
- Régler bug interface changement type de monstre quand on switch, le type ne change pas
- Régler bug models : -- fait
    - Si on applique potion ou capacité spéciale --> vérifier si monstre mort car sinon pour monstre plante --> régénération infinie -- fait
    - Si on attaque un monstre est qu'il est mort, son attaque nous touche pas -- fait