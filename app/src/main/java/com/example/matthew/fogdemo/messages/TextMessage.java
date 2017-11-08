package com.example.matthew.fogdemo.messages;

/**
 * Created by Matt on 11/3/2017.
 */

public class TextMessage {

    private String username;
    private String text;
    private String destName;

    private TextMessage(String user, String text, String destName) {
        this.username = user;
        this.text = text;
        this.destName = destName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getText() {
        return this.text;
    }

    public String getDestName() {
        return this.destName;
    }

    // com.example.matthew.fogdemo.messages.TextMessage is a string of the form - username : text
    public String toString() {
        String res = String.format("%s to %s : %s", username, destName, text);
        return res;
    }

    // TEXT username destName message
    // Returns a message parsed from the FogQueue message protocol
    public static TextMessage parseMessage(String networkMessage) {
        networkMessage = networkMessage.substring(5);
        String username = networkMessage.split(" ")[0];
        String destName = networkMessage.split(" ")[1];
        int length = username.length() + destName.length() + 2;
        String text = networkMessage.substring(length);
        return buildMessage(username, destName, text);
    }

    // Creates and returns a message given a username and text
    public static TextMessage buildMessage(String username, String destName, String text) {
        return new TextMessage(username, destName, text);
    }

}
