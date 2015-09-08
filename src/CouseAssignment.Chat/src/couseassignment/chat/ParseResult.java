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
public class ParseResult {

    private final String command;
    private final HashMap<String, Object> parameters;

    public ParseResult(String command, HashMap<String, Object> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public String getCommand() {
        return this.command;
    }

    public Object getParameter(String parameterName) {
        return this.parameters.get(parameterName);
    }

    HashMap<String, Object> getParameters() {
        return parameters;
    }
}
