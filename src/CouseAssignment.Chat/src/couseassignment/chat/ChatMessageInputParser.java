/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author priva_000
 */
class ChatMessageInputParser<T> {

    static ParseResult parse(String message) {
        ChatMessageInputParser parser = new ChatMessageInputParser(message);
        return parser.parse();
    }

    private final String message;

    private ChatMessageInputParser(String message) {
        this.message = message;
    }

    private ParseResult parse() {
        String[] parsedMessage = this.message.split("#");

        if (parsedMessage.length <= 0) {
            throw new IllegalArgumentException("message does not contain a #");
        }

        String command = parsedMessage[0];
        HashMap<String, Object> parameters;
        switch (command) {
            case "USER":
                if (parsedMessage.length != 2) {
                    throw new IllegalArgumentException("Invalid message");
                }

                parameters = new HashMap<>();
                parameters.put("user", parsedMessage[1]);

                return new ParseResult(command, parameters);
            case "MSG":
                if (parsedMessage.length != 3) {
                    throw new IllegalArgumentException("Invalid message");
                }

                List<String> receivers = new LinkedList<>();
                for (String receiver : parsedMessage[1].split(",")) {
                    if (receiver.equals("*")) {
                        receivers = null;
                        break;
                    }
                    receivers.add(receiver);
                }

                parameters = new HashMap<>();
                parameters.put("receivers", receivers);
                parameters.put("message", parsedMessage[2]);

                return new ParseResult(command, parameters);
            case "STOP":
                return new ParseResult(command, null);
        }
        
        return null;
    }

}
