/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author priva_000
 */
class ChatConnection implements Runnable {

    private final Socket socket;
    private final ChatServer server;
    private String username;
    private final BufferedReader socketReader;
    private final PrintWriter socketWriter;

    ChatConnection(Socket socket, ChatServer server) throws IOException {
        this.socket = socket;
        this.server = server;

        this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public void run() {
        try {
            try {
                String message;
                while ((message = this.socketReader.readLine()) != null) {
                    ParseResult response = ChatMessageInputParser.parse(message);

                    if (response == null){
                        send("ERROR#Wrong command");
                        continue;
                    }
                    
                    if (getUsername() == null && !response.getCommand().equals("USER")) {
                        send("ERROR#USER#{name] must be called for initialization");
                        continue;
                    }

                    switch (response.getCommand()) {
                        case "USER":
                            this.username = (String) response.getParameter("user");
                            this.server.onUserConnected(this, this.username);
                            break;
                        case "MSG":
                            this.server.onMessageReceived(this, (List<String>) response.getParameter("receivers"), (String) response.getParameter("message"));
                            break;
                        case "STOP":
                            this.server.onDisconnected(this);
                            break;
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ChatConnection.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                socketReader.close();
                socketWriter.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String message) {
        socketWriter.println(message);
    }

}
