package com.example.matthew.fogdemo.networkThreads;

import android.util.Log;

import com.example.matthew.fogdemo.MessageQueue;
import com.example.matthew.fogdemo.messages.TextMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Queue;

/**
 * Created by Matt on 10/18/2017.
 */

public class SendMessagesThread extends Thread {

    private final int FOG_PORT = 3000;

    public SendMessagesThread(String fogIP) {
        MessageQueue.getInstance().setFogIP(fogIP);
    }

    // Repopulates the MessageQueue with messages in order
    private void repopulateMessages(Queue<String> queue) {
        Log.d("MESSAGE QUEUE", "Repopulate Queue Starting With: " + queue.peek());
        while (!queue.isEmpty()) {
            MessageQueue.getInstance().addMessage(queue.remove());
        }
    }

    // Sends messages to the Fog node, whose IP we now know
    private void sendMessages(Queue<String> messages) throws IOException {
        String fogIp = MessageQueue.getInstance().getFogIP();
        InetAddress fogAddr = InetAddress.getByName(fogIp);
        Socket fogSocket = new Socket();
        fogSocket.connect(new InetSocketAddress(fogAddr, FOG_PORT), 500);;
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fogSocket.getOutputStream()));
            for (String message : messages) {
                printWriter.write(message);
            }
            printWriter.flush();
        } catch (Exception e) {
            fogSocket.close();
            throw e;
        }
        fogSocket.close();
    }

    @Override
    public void run() {
        MessageQueue messageQueue = MessageQueue.getInstance();
        while (true) {
            try {
                Thread.sleep(2000);
                Log.d("SEND MESSAGE THREAD", "ABOUT TO UNLOAD MESSAGE QUEUE");
                Queue<String> messagesWaiting = messageQueue.emptyOutQueue();
                if (messagesWaiting.isEmpty()) {
                    continue;
                }
                try {
                    sendMessages(messagesWaiting);
                } catch (IOException e) {
                    Log.d("FOG IP", "CAN'T CREATE CONNECTION WITH FOG DEVICE");
                    repopulateMessages(messagesWaiting);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
