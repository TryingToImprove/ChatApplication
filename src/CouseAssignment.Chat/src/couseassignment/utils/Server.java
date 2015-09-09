/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.utils;

import couseassignment.chat.ChatServer;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author priva_000
 */
public class Server {

    public static void main(String[] args) {
        try {
            // Setup logger
            Logger logger = Logger.getLogger(Server.class.getName());
            FileHandler fileHandler = new FileHandler("server-logs.txt");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Start server
            ChatServer server = new ChatServer(args[1], Integer.parseInt(args[0]));
            server.attachLogger(logger);
            server.start();

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
