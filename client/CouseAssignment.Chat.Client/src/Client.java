/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import couseassignment.chat.ChatClient;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author priva_000
 */
public class Client {

    public static void main(String[] args) throws IOException {
        try (ChatClient client = new ChatClient(args[1], Integer.parseInt(args[0]))) {
            client.addObserver(new OutputWriter());
            client.connect();
            
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    client.send(scanner.nextLine());
                }
            }
        }
    }

}
