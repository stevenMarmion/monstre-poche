package com.esiea.monstre.poche.models.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.poche.models.battle.logs.CombatLogger;
import com.esiea.monstre.poche.models.core.Joueur;
import com.esiea.monstre.poche.models.network.enums.EnumEvent;

/**
 * Client simple qui relaie les messages du serveur dans le terminal et envoie les reponses utilisateur.
 */
public class OnlineClient {
    private final String host;
    private final int port;

    public OnlineClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void lancer() {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner scanner = new Scanner(System.in)) {

            CombatLogger.network("Connecte au serveur " + host + ":" + port);
            CombatLogger.log("");
            
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(EnumEvent.INFO.toString())) {
                    CombatLogger.network(line.substring(5));
                } else if (line.startsWith(EnumEvent.ASK.toString())) {
                    CombatLogger.network(line.substring(4));
                    String response = scanner.nextLine().trim();
                    out.println(EnumEvent.ANSWER.toString() + response);
                } else if (line.startsWith(EnumEvent.END.toString())) {
                    CombatLogger.network(line.substring(4));
                    break;
                } else {
                    CombatLogger.network(line);
                }
            }
        } catch (IOException e) {
            CombatLogger.error("Connexion interrompue ou impossible : " + e.getMessage());
        }
    }

    /** Partie interface, connexion en mode interface */

    public OnlineConnection connecteToServer(Joueur joueur, String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            OnlineConnection connection = new OnlineConnection(socket);
            connection.sendInfo("Le joueur " + joueur.getNomJoueur() + " s'est connecte au serveur.");
            if (socket.isConnected()) {
                CombatLogger.network("Connecté au serveur " + host + ":" + port);
                return connection;
            }
            CombatLogger.error("Échec de la connexion au serveur " + host + ":" + port);
            socket.close();
        } catch (IOException e) {
            CombatLogger.error("Connexion interrompue ou impossible : " + e.getMessage());
        }
        return null;
    }
}
