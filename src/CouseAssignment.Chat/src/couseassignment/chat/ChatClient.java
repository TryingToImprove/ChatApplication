/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author priva_000
 */
public class ChatClient extends Observable implements Closeable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Thread readerThread;
    private final String address;
    private final int port;

    public ChatClient(String address, int port) {
        this.address = address;
        this.port = port;

    }

    public void send(String message) {
        if (message == null || "".equals(message)) {
            throw new IllegalArgumentException();
        }

        writer.println(message);
    }

    @Override
    public void close() throws IOException {
        this.writer.println("STOP#");
    }

    public void connect() throws IOException {
        this.socket = new Socket(address, port);
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.readerThread = new Thread(() -> {
            while (true) {
                try {
                    ChatResponse response = ChatMessageOutputParser.parse(reader.readLine());

                    setChanged();
                    notifyObservers(response);
                } catch (IOException ex) {
                    Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        this.readerThread.start();
    }
}
