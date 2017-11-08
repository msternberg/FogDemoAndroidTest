package com.example.matthew.fogdemo.messages;

/**
 * Created by Matt on 11/4/2017.
 */

public class MessageInfo {

    public enum MessageType {
        TEXT, USERS_NUM, MULE
    }

    public static MessageType determineMessageType(String data) {
        if (data == null) {
            return null;
        }
        if (data.substring(0, 4).equals("TEXT")) {
            return MessageType.TEXT;
        } else if (data.substring(0, 4).equals("MULE")) {
            return MessageType.MULE;
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
        return createTextMessageString(message.getUsername(), message.getDestName(), message.getText());
    }

    public static String createTextMessageString(String username, String destName, String contents) {
        String res = String.format("SENDTO %s %s %s", username, destName, contents);
        return res;
    }

    public static int getFQIDFromMuleString(String message) {
        message = message.substring(5);
        int FQID = Integer.parseInt(message.split(" ")[0]);
        return FQID;
    }
}
