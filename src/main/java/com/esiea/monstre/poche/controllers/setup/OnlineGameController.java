package com.esiea.monstre.poche.controllers.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.esiea.monstre.poche.controllers.INavigationCallback;
import com.esiea.monstre.poche.models.battle.Combat;
import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.battle.modes.CombatEnLigne;
import com.esiea.monstre.poche.models.core.Attaque;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.core.Monstre;
import com.esiea.monstre.poche.models.game.resources.GameResourcesFactory;
import com.esiea.monstre.poche.models.game.resources.GameResourcesLoader;
import com.esiea.monstre.poche.models.network.OnlineClient;
import com.esiea.monstre.poche.models.network.OnlineConnection;
import com.esiea.monstre.poche.models.network.OnlineServer;
import com.esiea.monstre.poche.views.gui.battle.BattleView;
import com.esiea.monstre.poche.views.gui.setup.OnlineGameView;

import javafx.application.Platform;

/**
 * Controller pour le mode de jeu en ligne.
 * Gere la connexion, l'echange des donnees des joueurs et le combat en ligne.
 */
public class OnlineGameController {
    
    // ===== Phase de connexion =====
    private OnlineGameView setupView;
    private INavigationCallback navigationCallback;
    private OnlineConnection currentConnection;
    private boolean isHost = false;

    // TODO gameresources factory passe, mais  plusieurs instances définies dans l'app
    // ca devrait etre un singleton
    private final GameResourcesLoader resourcesLoader = new GameResourcesLoader();
    private final GameResourcesFactory resourcesFactory = new GameResourcesFactory(resourcesLoader);

    // ===== Phase de combat =====
    private BattleView battleView;
    private Combat combat;
    private Joueur joueurLocal;
    private Joueur joueurDistant;
    
    // Actions des joueurs
    private Object localAction = null;
    private Object remoteAction = null;
    private boolean localReady = false;
    private boolean remoteReady = false;
    
    // Thread d'ecoute reseau
    private Thread networkListenerThread;
    private boolean running = true;
    
    /**
     * Constructeur pour la phase de connexion.
     */
    public OnlineGameController(OnlineGameView view, INavigationCallback navigationCallback) {
        this.setupView = view;
        this.navigationCallback = navigationCallback;
        initializeSetupEventHandlers();
    }
    
    /**
     * Initialise les gestionnaires d'evenements pour la phase de connexion.
     */
    private void initializeSetupEventHandlers() {
        setupView.getBtnBackToMenu().setOnAction(e -> backToMenu());
        setupView.getBtnJoinServer().setOnAction(e -> handleJoinServer());
        setupView.getBtnCreateServer().setOnAction(e -> handleCreateServer());
    }

    private void backToMenu() {
        navigationCallback.showMainMenu();
    }

    private void handleJoinServer() {
        String joinPlayerName = setupView.getJoinPlayerName().getText().trim();
        String joinPort = setupView.getJoinPort().getText().trim();
        String address = setupView.getJoinHost().getText().trim();

        if (joinPlayerName.isEmpty()) {
            setupView.showError("Veuillez entrer votre nom de joueur");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(joinPort);
        } catch (NumberFormatException ex) {
            setupView.showError("Port invalide");
            return;
        }

        Joueur joiningPlayer = new Joueur(joinPlayerName);
        OnlineClient client = new OnlineClient(address, port);
        isHost = false;

        setupView.showJoinLoading();

        new Thread(() -> {
            OnlineConnection connectionClient = client.connecteToServer(joiningPlayer, address, port);
            Platform.runLater(() -> {
                if (connectionClient != null && connectionClient.getSocket().isConnected()) {
                    currentConnection = connectionClient;
                    CombatLogger.log("Connecte au serveur !");
                    startSelectionFlow(joiningPlayer, connectionClient, false);
                } else {
                    setupView.resetJoinLoading();
                    setupView.showError("Impossible de se connecter au serveur");
                }
            });
        }, "online-client-connect").start();
    }

    private void handleCreateServer() {
        String hostPlayerName = setupView.getHostPlayerName().getText().trim();
        String hostPort = setupView.getHostPort().getText().trim();

        if (hostPlayerName.isEmpty()) {
            setupView.showError("Veuillez entrer votre nom de joueur");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(hostPort);
        } catch (NumberFormatException ex) {
            setupView.showError("Port invalide");
            return;
        }

        Joueur hostPlayer = new Joueur(hostPlayerName);
        isHost = true;
        setupView.showHostLoading();

        new Thread(() -> {
            OnlineServer server = new OnlineServer(port, null);
            OnlineConnection connectionServer = server.demarrerServeur();
            Platform.runLater(() -> {
                if (connectionServer != null && connectionServer.getSocket().isConnected()) {
                    currentConnection = connectionServer;
                    CombatLogger.log("Client connecte !");
                    startSelectionFlow(hostPlayer, connectionServer, true);
                } else {
                    setupView.resetHostLoading();
                    setupView.showError("Erreur lors de la creation du serveur");
                }
            });
        }, "online-server-accept").start();
    }

    private void startSelectionFlow(Joueur joueur, OnlineConnection connection, boolean hosting) {
        this.joueurLocal = joueur;
        this.isHost = hosting;
        navigationCallback.showMonsterSelectionPlayer(joueur, () -> {
            // Apres la selection des monstres et attaques, echanger les donnees
            exchangePlayerDataAndStartBattle(joueur, connection, hosting);
        });
    }

    /**
     * Echange les donnees des joueurs et demarre le combat.
     */
    private void exchangePlayerDataAndStartBattle(Joueur joueurLocal, OnlineConnection connection, boolean hosting) {
        new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getSocket().getInputStream())
                );
                
                // Envoyer nos donnees de joueur
                String playerData = serializePlayer(joueurLocal);
                connection.sendInfo("PLAYER_DATA|" + playerData);
                CombatLogger.log("Donnees envoyees, en attente de l'adversaire...");
                
                // Attendre les donnees de l'adversaire
                String line;
                Joueur joueurDistantTemp = null;
                
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("INFO|PLAYER_DATA|")) {
                        String remoteData = line.substring(17);
                        joueurDistantTemp = deserializePlayer(remoteData);
                        CombatLogger.log("Donnees de l'adversaire recues !");
                        break;
                    }
                }
                
                if (joueurDistantTemp != null) {
                    final Joueur finalJoueurDistant = joueurDistantTemp;
                    Platform.runLater(() -> {
                        this.joueurDistant = finalJoueurDistant;
                        startBattle(joueurLocal, finalJoueurDistant, connection, hosting);
                    });
                } else {
                    throw new IOException("Donnees de l'adversaire non recues");
                }
            } catch (Exception e) {
                CombatLogger.error("Erreur d'echange de donnees : " + e.getMessage());
                Platform.runLater(navigationCallback::showMainMenu);
            }
        }, "online-data-exchange").start();
    }
    
    // ==========================================================================
    // SERIALISATION / DESERIALISATION
    // ==========================================================================
    
    /**
     * Serialise un joueur en chaine de caracteres.
     * Format: nomJoueur;monstre1Name:atk1,atk2,atk3,atk4;monstre2Name:atk1,...;monstre3Name:...;currentMonstreIndex
     */
    private String serializePlayer(Joueur joueur) {
        StringBuilder sb = new StringBuilder();
        sb.append(joueur.getNomJoueur()).append(";");
        
        for (int i = 0; i < joueur.getMonstres().size(); i++) {
            Monstre m = joueur.getMonstres().get(i);
            sb.append(m.getNomMonstre()).append(":");
            
            List<String> atkNames = new ArrayList<>();
            for (Attaque atk : m.getAttaques()) {
                atkNames.add(atk.getNomAttaque());
            }
            sb.append(String.join(",", atkNames));
            
            if (i < joueur.getMonstres().size() - 1) {
                sb.append(";");
            }
        }
        
        // Ajouter l'index du monstre actuel
        int currentIndex = joueur.getMonstres().indexOf(joueur.getMonstreActuel());
        sb.append(";").append(currentIndex);
        
        return sb.toString();
    }
    
    /**
     * Deserialise un joueur depuis une chaine de caracteres.
     */
    private Joueur deserializePlayer(String data) {
        CombatLogger.log("Deserialisation des donnees: " + data);
        String[] parts = data.split(";");
        if (parts.length < 2) {
            CombatLogger.error("Donnees invalides: moins de 2 parties");
            return null;
        }
        
        String nomJoueur = parts[0];
        Joueur joueur = new Joueur(nomJoueur);
        
        // Les dernieres parties sont les monstres, et le tout dernier est l'index
        int currentIndex = Integer.parseInt(parts[parts.length - 1]);
        CombatLogger.log("Index du monstre actuel: " + currentIndex);
        
        // Parser les monstres (de l'index 1 a length-2)
        for (int i = 1; i < parts.length - 1; i++) {
            String monstrePart = parts[i];
            String[] monstreData = monstrePart.split(":");
            if (monstreData.length < 2) {
                CombatLogger.error("Donnees monstre invalides: " + monstrePart);
                continue;
            }
            
            String nomMonstre = monstreData[0];
            String[] atkNames = monstreData[1].split(",");
            CombatLogger.log("Chargement monstre: " + nomMonstre + " avec " + atkNames.length + " attaques");
            
            // Trouver le monstre dans le loader
            Monstre monstre = findMonstreByName(this.resourcesFactory, nomMonstre);
            if (monstre != null) {
                // Cloner le monstre pour ne pas modifier l'original
                Monstre clonedMonstre = monstre.copyOf();
                
                // Ajouter les attaques
                for (String atkName : atkNames) {
                    Attaque attaque = findAttaqueByName(this.resourcesFactory, atkName);
                    if (attaque != null) {
                        clonedMonstre.ajouterAttaque(attaque);
                    } else {
                        CombatLogger.error("Attaque non trouvee: " + atkName);
                    }
                }
                
                joueur.ajouterMonstre(clonedMonstre);
                CombatLogger.log("Monstre ajoute: " + clonedMonstre.getNomMonstre());
            } else {
                CombatLogger.error("Monstre non trouve dans le loader: " + nomMonstre);
            }
        }
        
        CombatLogger.log("Nombre de monstres charges: " + joueur.getMonstres().size());
        
        // Definir le monstre actuel - TOUJOURS definir un monstre actuel si la liste n'est pas vide
        if (!joueur.getMonstres().isEmpty()) {
            if (currentIndex >= 0 && currentIndex < joueur.getMonstres().size()) {
                joueur.setMonstreActuel(joueur.getMonstres().get(currentIndex));
            } else {
                // Si l'index est invalide, prendre le premier monstre
                joueur.setMonstreActuel(joueur.getMonstres().get(0));
            }
            CombatLogger.log("Monstre actuel defini: " + joueur.getMonstreActuel().getNomMonstre());
        } else {
            CombatLogger.error("Aucun monstre charge pour le joueur " + nomJoueur);
        }
        
        return joueur;
    }
    
    private Monstre findMonstreByName(GameResourcesFactory resourcesFactory, String name) {
        if (resourcesFactory == null) return null;
        for (Monstre m : resourcesFactory.getTousLesMonstres()) {
            if (m.getNomMonstre().equals(name)) {
                return m;
            }
        }
        return null;
    }
    
    private Attaque findAttaqueByName(GameResourcesFactory resourcesFactory, String name) {
        if (resourcesFactory == null) return null;
        for (Attaque a : resourcesFactory.getToutesLesAttaques()) {
            if (a.getNomAttaque().equals(name)) {
                return a;
            }
        }
        return null;
    }
    
    /**
     * Demarre le combat en ligne.
     */
    private void startBattle(Joueur joueurLocal, Joueur joueurDistant, OnlineConnection connection, boolean hosting) {
        this.joueurLocal = joueurLocal;
        this.joueurDistant = joueurDistant;
        this.currentConnection = connection;
        this.isHost = hosting;
        
        // Creer une instance de Combat
        // L'hote est toujours joueur1, le client est joueur2 pour la coherence
        if (isHost) {
            this.combat = new CombatEnLigne(joueurLocal, joueurDistant, connection);
        } else {
            this.combat = new CombatEnLigne(joueurDistant, joueurLocal, connection);
        }
        
        // Creer la vue de combat
        battleView = new BattleView(joueurLocal, joueurDistant);
        navigationCallback.showBattleOnline(battleView);
        
        // Configurer le CombatLogger pour l'interface
        CombatLogger.setGuiCallback(message -> {
            Platform.runLater(() -> battleView.updateBattleLog(message));
        });
        CombatLogger.clear();
        
        // Log du debut de combat
        CombatLogger.logDebutCombat(joueurLocal, joueurDistant);
        
        initializeBattleEventHandlers();
        startNetworkListener();
        
        // Le joueur local peut jouer
        battleView.setTurn(true);
        CombatLogger.logTourJoueur(joueurLocal);
    }
    
    /**
     * Initialise les gestionnaires d'evenements pour le combat.
     */
    private void initializeBattleEventHandlers() {
        battleView.getBtnAttack().setOnAction(e -> handleAttackAction());
        battleView.getBtnSwitch().setOnAction(e -> handleSwitchAction());
        battleView.getBtnItem().setOnAction(e -> handleItemAction());
    }
    
    /**
     * Demarre le thread d'ecoute des messages reseau.
     */
    private void startNetworkListener() {
        networkListenerThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(currentConnection.getSocket().getInputStream())
                );
                
                String line;
                while (running && (line = in.readLine()) != null) {
                    processNetworkMessage(line);
                }
            } catch (IOException e) {
                if (running) {
                    Platform.runLater(() -> {
                        CombatLogger.error("Connexion perdue : " + e.getMessage());
                        navigationCallback.showMainMenu();
                    });
                }
            }
        }, "online-battle-listener");
        networkListenerThread.setDaemon(true);
        networkListenerThread.start();
    }
    
    /**
     * Traite un message recu du reseau.
     */
    private void processNetworkMessage(String line) {
        if (line.startsWith("INFO|ACTION|")) {
            // Format: INFO|ACTION|TYPE|DATA
            String actionData = line.substring(12);
            parseRemoteAction(actionData);
        } else if (line.startsWith("INFO|MONSTER_UPDATE|")) {
            // Mise a jour des donnees du monstre distant
            String updateData = line.substring(20);
            parseMonsterUpdate(updateData);
        } else if (line.startsWith("INFO|")) {
            // Message d'information general
            String message = line.substring(5);
            Platform.runLater(() -> battleView.updateBattleLog("[Distant] " + message));
        }
    }
    
    /**
     * Parse l'action recue de l'adversaire distant.
     */
    private void parseRemoteAction(String actionData) {
        String[] parts = actionData.split("\\|");
        if (parts.length < 2) return;
        
        String actionType = parts[0];
        String actionValue = parts[1];
        
        Platform.runLater(() -> {
            if ("ATTACK".equals(actionType)) {
                // Trouver l'attaque par son nom dans les attaques du monstre distant
                Monstre monstreDistant = joueurDistant.getMonstreActuel();
                for (Attaque atk : monstreDistant.getAttaques()) {
                    if (atk.getNomAttaque().equals(actionValue)) {
                        remoteAction = atk;
                        remoteReady = true;
                        battleView.updateBattleLog(joueurDistant.getNomJoueur() + " a choisi : " + actionValue);
                        tryExecuteTurn();
                        return;
                    }
                }
                CombatLogger.error("Attaque inconnue recue : " + actionValue);
            } else if ("SWITCH".equals(actionType)) {
                // Trouver le monstre par son nom
                for (Monstre m : joueurDistant.getMonstres()) {
                    if (m.getNomMonstre().equals(actionValue)) {
                        remoteAction = m;
                        remoteReady = true;
                        battleView.updateBattleLog(joueurDistant.getNomJoueur() + " change de monstre pour " + actionValue);
                        tryExecuteTurn();
                        return;
                    }
                }
                CombatLogger.error("Monstre inconnu recu : " + actionValue);
            }
        });
    }
    
    /**
     * Parse une mise a jour du monstre distant.
     */
    private void parseMonsterUpdate(String updateData) {
        // Format: nomMonstre|pv
        String[] parts = updateData.split("\\|");
        if (parts.length >= 2) {
            String nomMonstre = parts[0];
            double pv = Double.parseDouble(parts[1]);
            
            for (Monstre m : joueurDistant.getMonstres()) {
                if (m.getNomMonstre().equals(nomMonstre)) {
                    m.setPointsDeVie(pv);
                    break;
                }
            }
            Platform.runLater(() -> battleView.updatePokemonDisplay());
        }
    }
    
    /**
     * Envoie l'action locale a l'adversaire.
     */
    private void sendAction(String actionType, String actionValue) {
        currentConnection.sendInfo("ACTION|" + actionType + "|" + actionValue);
    }
    
    /**
     * Envoie la mise a jour du monstre local.
     */
    private void sendMonsterUpdate(Monstre monstre) {
        currentConnection.sendInfo("MONSTER_UPDATE|" + monstre.getNomMonstre() + "|" + (int) monstre.getPointsDeVie());
    }
    
    /**
     * Gere l'action Attaquer.
     */
    private void handleAttackAction() {
        if (localReady) {
            battleView.updateBattleLog("Vous avez deja choisi votre action. En attente de l'adversaire...");
            return;
        }
        
        List<Attaque> attaques = joueurLocal.getMonstreActuel().getAttaques();
        if (attaques == null || attaques.isEmpty()) {
            battleView.updateBattleLog("Pas d'attaques disponibles.");
            return;
        }
        
        battleView.displayAttackChoices(attaques, attaque -> {
            localAction = attaque;
            localReady = true;
            battleView.updateBattleLog("Vous avez choisi : " + attaque.getNomAttaque());
            battleView.setTurn(false);
            
            // Envoyer l'action au joueur distant
            sendAction("ATTACK", attaque.getNomAttaque());
            
            battleView.updateBattleLog("En attente de l'action de " + joueurDistant.getNomJoueur() + "...");
            tryExecuteTurn();
        });
    }
    
    /**
     * Gere l'action Changer de monstre.
     */
    private void handleSwitchAction() {
        if (localReady) {
            battleView.updateBattleLog("Vous avez deja choisi votre action. En attente de l'adversaire...");
            return;
        }
        
        List<Monstre> candidates = joueurLocal.getMonstres();
        Monstre current = joueurLocal.getMonstreActuel();
        List<Monstre> available = candidates.stream()
                .filter(m -> m.getPointsDeVie() > 0 && m != current)
                .toList();
        
        if (available.isEmpty()) {
            battleView.updateBattleLog("Aucun autre monstre disponible.");
            return;
        }
        
        battleView.displayMonsterChoices(available, selected -> {
            localAction = selected;
            localReady = true;
            battleView.updateBattleLog("Vous envoyez " + selected.getNomMonstre() + " !");
            battleView.setTurn(false);
            
            // Envoyer l'action au joueur distant
            sendAction("SWITCH", selected.getNomMonstre());
            
            battleView.updateBattleLog("En attente de l'action de " + joueurDistant.getNomJoueur() + "...");
            tryExecuteTurn();
        });
    }
    
    /**
     * Gere l'action Objet.
     */
    private void handleItemAction() {
        battleView.updateBattleLog("Fonctionnalite Objet non implementee pour le mode en ligne.");
    }
    
    /**
     * Tente d'executer le tour si les deux joueurs ont choisi.
     */
    private void tryExecuteTurn() {
        if (!localReady || !remoteReady) {
            return;
        }
        
        executeTurn();
    }
    
    /**
     * Execute le tour de combat.
     */
    private void executeTurn() {
        battleView.clearBattleLog();
        
        // Determiner qui est joueur1 et joueur2 pour la logique de combat
        Object action1, action2;
        if (isHost) {
            action1 = localAction;
            action2 = remoteAction;
        } else {
            action1 = remoteAction;
            action2 = localAction;
        }
        
        // Annonce des actions
        battleView.updateBattleLog("=== Actions annoncees ===");
        battleView.updateBattleLog(joueurLocal.getNomJoueur() + ": " + formatAction(localAction));
        battleView.updateBattleLog(joueurDistant.getNomJoueur() + ": " + formatAction(remoteAction));
        
        // Effacer uniquement les logs du tour actuel (pas tout l'historique)
        CombatLogger.clearCurrentTurn();
        combat.gereOrdreExecutionActions(action1, action2);
        
        // Afficher les logs du tour actuel
        String detailed = CombatLogger.getFormattedCurrentTurnLogs();
        if (detailed != null && !detailed.isEmpty()) {
            battleView.updateBattleLog(detailed);
        }
        
        // Mettre a jour l'affichage
        battleView.updatePokemonDisplay();
        
        // Envoyer la mise a jour des PV du monstre local
        sendMonsterUpdate(joueurLocal.getMonstreActuel());
        
        // Verifier la fin de combat
        if (joueurLocal.sontMonstresMorts()) {
            navigationCallback.showWinnerView(joueurDistant);
            cleanup();
            return;
        }
        if (joueurDistant.sontMonstresMorts()) {
            navigationCallback.showWinnerView(joueurLocal);
            cleanup();
            return;
        }
        
        // Reinitialiser pour le prochain tour
        localAction = null;
        remoteAction = null;
        localReady = false;
        remoteReady = false;
        
        battleView.setTurn(true);
        battleView.updateBattleLog("A vous de choisir votre action !");
    }
    
    /**
     * Formate une action pour l'affichage.
     */
    private String formatAction(Object action) {
        if (action == null) return "(aucune)";
        if (action instanceof Attaque) {
            return "Attaque: " + ((Attaque) action).getNomAttaque();
        }
        if (action instanceof Monstre) {
            return "Changement: " + ((Monstre) action).getNomMonstre();
        }
        return action.getClass().getSimpleName();
    }
    
    /**
     * Nettoie les ressources.
     */
    private void cleanup() {
        running = false;
        try {
            currentConnection.close();
        } catch (IOException e) {
            // Ignorer
        }
    }
}
