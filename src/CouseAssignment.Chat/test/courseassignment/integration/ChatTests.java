/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseassignment.integration;

import couseassignment.chat.ChatServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author priva_000
 */
public class ChatTests {

    private ChatServer chatServer;
    private int port = 45612;
    private String address = "localhost";

    private Socket createConnection() throws IOException {
        return new Socket(this.address, this.port);
    }

    @Before
    public void SetupTests() {
        this.chatServer = new ChatServer(this.address, this.port);
        new Thread(() -> this.chatServer.start()).start();
    }

    @After
    public void TearDownTests() {
        this.chatServer.stop();
    }

    @Test
    public void testIsAbleToConnectUser() throws Exception {
        try (Socket socket = createConnection()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                    boolean isConnected = socket.isConnected();
                    Assert.assertTrue(isConnected);

                    writer.println("USER#Oliver");

                    String response = reader.readLine();
                    Assert.assertEquals("USERLIST#Oliver", response);
                }
            }
        }
    }

    @Test
    public void testIsAbleToSendMessageToAll() throws Exception {
        int numberOfConnections = 5;
        Thread[] connections = new Thread[numberOfConnections];
        for (int i = 0; i < 5; i++) {
            final String connectionName = "Oliver" + i;

            Thread connection = new Thread(() -> {
                try (Socket socket = createConnection()) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                            writer.println("USER#" + connectionName);
                            String userList = reader.readLine(); // Returns the user list

                            String response = reader.readLine();
                            Assert.assertEquals("MSG#*#tesddt", response);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ChatTests.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            connection.start();
            connections[i] = connection;
        }

        try (Socket socket = createConnection()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                    writer.println("USER#Oliver");
                    reader.readLine();

                    writer.println("MSG#*#test");
                }
            }
        }
        
        for (Thread connection : connections) {
            connection.join();
        }
    }

    @Test
    public void testShouldReturnErrorIfNotCorrectInitialize() throws Exception {
        try (Socket socket = createConnection()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                    boolean isConnected = socket.isConnected();
                    Assert.assertTrue(isConnected);

                    writer.println("Wrong#");

                    String response = reader.readLine();
                    Assert.assertEquals("ERROR#", response.substring(0, 6));
                }
            }
        }
    }
}
