package com.example.matthew.fogdemo.messages;

/**
 * Created by Matt on 11/3/2017.
 */

public class TextMessage {

    private String username;
    private String text;

    private TextMessage(String user, String text) {
        this.username = user;
        this.text = text;
    }

    public String getUsername() {
        return this.username;
    }

    public String getText() {
        return this.text;
    }

    // com.example.matthew.fogdemo.messages.TextMessage is a string of the form - username : text
    public String toString() {
        String res = "";
        res += username + " : ";
        res += text + "\n";
        return res;
    }

    // Returns a message parsed from the FogQueue message protocol
    public static TextMessage parseMessage(String networkMessage) {
        networkMessage = networkMessage.substring(5);
        String username = networkMessage.split(" ")[0];
        String text = networkMessage.substring(username.length());
        return buildMessage(username, text);
    }

    // Creates and returns a message given a username and text
    public static TextMessage buildMessage(String username, String text) {
        return new TextMessage(username, text);
    }

}
