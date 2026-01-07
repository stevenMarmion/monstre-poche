package com.esiea.monstre.poche.models.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.esiea.monstre.poche.models.network.enums.EnumEvent;

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
        out.println(EnumEvent.INFO + message);
    }

    public void sendEnd(String message) {
        out.println(EnumEvent.END + message);
    }

    public String ask(String prompt) throws IOException {
        out.println(EnumEvent.ASK + prompt);
        return waitForAnswer();
    }

    private String waitForAnswer() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith(EnumEvent.ANSWER.toString())) {
                return line.substring(EnumEvent.ANSWER.getLabelLength());
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
}
