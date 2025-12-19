package com.esiea.monstre.poche.models.online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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

            System.out.println("Connecte au serveur " + host + ":" + port + " (CTRL+C pour quitter)\n");
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("INFO|")) {
                    System.out.println(line.substring(5));
                } else if (line.startsWith("ASK|")) {
                    String prompt = line.substring(4);
                    System.out.print(prompt + " ");
                    String response = scanner.nextLine().trim();
                    out.println("ANS|" + response);
                } else if (line.startsWith("END|")) {
                    System.out.println(line.substring(4));
                    break;
                } else {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERREUR] Connexion interrompue ou impossible : " + e.getMessage());
        }
    }
}
