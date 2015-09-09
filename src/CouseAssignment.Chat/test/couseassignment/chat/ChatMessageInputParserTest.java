/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package couseassignment.chat;

import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author priva_000
 */
public class ChatMessageInputParserTest {

    @Test
    public void testShouldReturnNullIfCommandNotFound() {
        String message = "FailureCommand#Stuff";

        ParseResult result = ChatMessageInputParser.parse(message);

        Assert.assertNull(result);
    }

    @Test
    public void testIsAbleToParseAUserConnected() {
        String message = "USER#Oliver";

        ParseResult result = ChatMessageInputParser.parse(message);

        Assert.assertEquals("USER", result.getCommand());
        Assert.assertEquals("Oliver", result.getParameter("user"));
    }

    @Test
    public void testIsAbleToParseNewMessageToMultipleUsers() {
        String message = "MSG#Oliver,Brian#Hej";

        ParseResult result = ChatMessageInputParser.parse(message);

        Assert.assertEquals("MSG", result.getCommand());

        List<String> receivers = (List<String>) result.getParameter("receivers");
        Assert.assertEquals(2, receivers.size());
        Assert.assertTrue(receivers.contains("Oliver"));
        Assert.assertTrue(receivers.contains("Brian"));

        Assert.assertEquals("Hej", result.getParameter("message"));
    }

    @Test
    public void testIsAbleToParseAUserDisconnect() {
        String message = "STOP#";

        ParseResult result = ChatMessageInputParser.parse(message);

        Assert.assertEquals("STOP", result.getCommand());
    }
}
