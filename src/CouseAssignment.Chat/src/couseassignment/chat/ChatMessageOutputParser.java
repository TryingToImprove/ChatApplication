/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.util.HashMap;

/**
 *
 * @author priva_000
 */
class ChatMessageOutputParser<T> {

    static ChatResponse parse(String message) {
        ChatMessageOutputParser parser = new ChatMessageOutputParser(message);
        return parser.parse();
    }

    private final String message;

    private ChatMessageOutputParser(String message) {
        this.message = message;
    }

    private ChatResponse parse() {
        String[] parsedMessage = this.message.split("#");

        if (parsedMessage.length <= 0) {
            throw new IllegalArgumentException("message does not contain a #");
        }

        String command = parsedMessage[0];
        HashMap<String, Object> parameters;
        switch (command) {
            case "USERLIST":
                if (parsedMessage.length != 2) {
                    throw new IllegalArgumentException("Invalid message");
                }

                parameters = new HashMap<>();
                parameters.put("users", parsedMessage[1].split(","));

                return new ChatResponse(command, parameters);
            case "MSG":
                if (parsedMessage.length != 3) {
                    throw new IllegalArgumentException("Invalid message");
                }

                parameters = new HashMap<>();
                parameters.put("sender", parsedMessage[1]);
                parameters.put("message", parsedMessage[2]);

                return new ChatResponse(command, parameters);
        }

        return null;
    }

}
