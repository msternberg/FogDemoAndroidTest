package com.example.matthew.fogdemo;

import android.util.Log;

import com.example.matthew.fogdemo.messages.TextMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Matt on 10/18/2017.
 */

public class MessageQueue {

    private Object lock = new Object();
    private Queue<String> messages;
    private static MessageQueue messageQueue = null;
    private String fogIP = "";

    private MessageQueue() {
        messages = new LinkedList<String>();
    }

    // Singleton message queue
    public static MessageQueue getInstance() {
        if (messageQueue == null) {
            messageQueue = new MessageQueue();
        }
        return messageQueue;
    }

    public void setFogIP(String fogIP) {
        this.fogIP = fogIP;
        Log.d("MESSAGE QUEUE FOG IP", "FOG IP SET TO: " + fogIP);
    }

    public String getFogIP() {
        return fogIP;
    }

    public void addMessage(String message) {
        synchronized (lock) {
            messages.add(message);
        }
    }

    public String popMessage() {
        synchronized (lock) {
            return messages.remove();
        }
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return messages.isEmpty();
        }
    }

    public Queue<String> emptyOutQueue() {
        Queue<String> retQueue = new LinkedList<>();
        synchronized (lock) {
            while (!messages.isEmpty()) {
                retQueue.add(messages.remove());
            }
        }
        return retQueue;
    }
}
