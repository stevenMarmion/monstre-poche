package com.esiea.monstre.poche.online;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Connexion utilitaire pour envoyer/recevoir des messages simples entre le serveur et le client.
 */
public class OnlineConnection implements Closeable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public OnlineConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public void sendInfo(String message) {
        out.println("INFO|" + message);
        out.flush();
    }

    public void sendEnd(String message) {
        out.println("END|" + message);
    }

    public String ask(String prompt) throws IOException {
        out.println("ASK|" + prompt);
        out.flush();
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

    public void sendRaw(String message) {
        out.println(message);
    }

    public String readRaw() throws IOException {
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
