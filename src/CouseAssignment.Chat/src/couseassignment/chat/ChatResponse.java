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
public class ChatResponse extends ParseResult {

    ChatResponse(String command, HashMap<String, Object> parameters) {
        super(command, parameters);
    }

}
