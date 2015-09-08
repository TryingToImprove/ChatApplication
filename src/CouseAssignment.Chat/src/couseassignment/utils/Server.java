/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.utils;

import couseassignment.chat.ChatServer;

/**
 *
 * @author priva_000
 */
public class Server {

    public static void main(String[] args) {
        ChatServer server = new ChatServer(args[1], Integer.parseInt(args[0]));
        server.start();
    }

}
