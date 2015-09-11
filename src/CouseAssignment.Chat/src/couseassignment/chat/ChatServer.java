/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author priva_000
 */
public class ChatServer {

    private final String address;
    private final int port;

    private final Object lock = new Object();
    private final List<ChatConnection> connections = new ArrayList<>();
    private ServerSocket serverSocket;
    private boolean isRunning;
    private Logger logger;

    public ChatServer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(this.address, this.port));
            //serverSocket.setSoTimeout(200);

            isRunning = true;

            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    ChatConnection chatConnection = new ChatConnection(socket, this);
                    Thread chatConnectionThread = new Thread(chatConnection);
                    chatConnectionThread.start();

                    synchronized (lock) {
                        connections.add(chatConnection);
                    }
                } catch (SocketException socketEx) {
                    // Do nothing
                }
            }
        } catch (IOException ex) {
            if (logger != null) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    void onUserConnected(ChatConnection sender, String username) {
        synchronized (lock) {
            String users = String.join(",", getUsers());

            connections.stream().forEach((connection) -> {
                connection.send("USERLIST#" + users);
            });
        }
    }

    void onMessageReceived(ChatConnection sender, List<String> receivers, String message) {
        synchronized (lock) {
            for (ChatConnection connection : connections) {
                if (receivers == null || sender.getUsername().equals(connection.getUsername()) || receivers.contains(connection.getUsername())) {
                    connection.send("MSG#" + sender.getUsername() + "#" + message);
                }
            }
        }
    }

    void onDisconnected(ChatConnection sender) {
        synchronized (lock) {
            connections.remove(sender);

            String users = String.join(",", getUsers());

            connections.stream().forEach((connection) -> {
                connection.send("USERLIST#" + users);
            });
        }
    }

    private String[] getUsers() {
        String[] users = new String[connections.size()];

        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getUsername() != null) {
                users[i] = connections.get(i).getUsername();
            }
        }

        return users;
    }

    public void stop() {
        if (serverSocket != null && isRunning) {
            try {
                for (ChatConnection connection : connections) {
    //                connection.stop();
                }

                isRunning = false;
                serverSocket.close();
            } catch (IOException ex) {
                if (logger != null) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void attachLogger(Logger logger) {
        this.logger = logger;
    }

    Logger getLogger() {
        return this.logger;
    }
}
