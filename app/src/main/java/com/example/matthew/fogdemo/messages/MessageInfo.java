package com.example.matthew.fogdemo.messages;

/**
 * Created by Matt on 11/4/2017.
 */

public class MessageInfo {

    public enum MessageType {
        TEXT, USERS_NUM,
    }

    public static MessageType determineMessageType(String data) {
        if (data == null) {
            return null;
        }
        if (data.substring(0, 4).equals("TEXT")) {
            return MessageType.TEXT;
        } else {
            return MessageType.USERS_NUM;
        }
    }

    public static String createEnterMessageString(String username) {
        String res = String.format("ENTER %s", username);
        return res;
    }

    public static String createLeaveMessage(String username) {
        String res = String.format("DISC %s", username);
        return res;
    }

    public static String createTextMessageString(TextMessage message) {
        return createTextMessageString(message.getUsername(), message.getText());
    }

    public static String createTextMessageString(String username, String contents) {
        String res = String.format("SENDTO %s %s", username, contents);
        return res;
    }
}
