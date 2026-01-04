package com.esiea.monstre.pochebis.models.online;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.esiea.monstre.pochebis.models.entites.Joueur;

/**
 * Connexion utilitaire pour envoyer/recevoir des messages simples entre le serveur et le client.
 */
public class OnlineConnection implements Closeable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    private Joueur joueur;

    public OnlineConnection(Socket socket, Joueur joueur) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.joueur = joueur;
    }

    public void sendInfo(String message) {
        out.println("INFO|" + message);
        // out.flush();
    }

    public void sendEnd(String message) {
        out.println("END|" + message);
        // out.flush();
    }

    public String ask(String prompt) throws IOException {
        out.println("ASK|" + prompt);
        // out.flush();
        return waitForAnswer();
    }

    private String waitForAnswer() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("ANS|")) {
                return line.substring(4);
            }
        }
        throw new IOException("Connexion interrompue avant reponse");
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public Joueur getJoueur() {
        return joueur;
    }
}
