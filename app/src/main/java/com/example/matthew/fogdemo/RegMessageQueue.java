package com.example.matthew.fogdemo;

import android.util.Log;

import com.example.matthew.fogdemo.messages.MuleMessage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Matt on 10/18/2017.
 */

public class RegMessageQueue {

    private Object lock = new Object();
    private Queue<String> messages;
    private static RegMessageQueue messageQueue = null;
    private String fogIP = "";

    private RegMessageQueue() {
        messages = new LinkedList<String>();
    }

    // Singleton message queue
    public static RegMessageQueue getInstance() {
        if (messageQueue == null) {
            messageQueue = new RegMessageQueue();
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

    public String getFogIp() {
        return this.fogIP;
    }
}
