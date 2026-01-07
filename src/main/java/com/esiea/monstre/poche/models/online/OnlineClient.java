package com.esiea.monstre.poche.models.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.esiea.monstre.poche.models.combats.CombatLogger;
import com.esiea.monstre.poche.models.entites.Joueur;

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

            CombatLogger.log("Connecte au serveur " + host + ":" + port);
            CombatLogger.log("");
            
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("INFO|")) {
                    CombatLogger.log(line.substring(5));
                } else if (line.startsWith("ASK|")) {
                    CombatLogger.log(line.substring(4));
                    String response = scanner.nextLine().trim();
                    out.println("ANS|" + response);
                } else if (line.startsWith("END|")) {
                    CombatLogger.log(line.substring(4));
                    break;
                } else {
                    CombatLogger.log(line);
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
                CombatLogger.log("Connecté au serveur " + host + ":" + port);
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
