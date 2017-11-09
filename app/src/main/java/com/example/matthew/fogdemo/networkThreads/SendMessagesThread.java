package com.example.matthew.fogdemo.networkThreads;

import android.util.Log;

import com.example.matthew.fogdemo.RegMessageQueue;
import com.example.matthew.fogdemo.SessionInfo;
import com.example.matthew.fogdemo.messages.MessageInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private boolean killThread = false;

    public SendMessagesThread(String fogIP) {
        RegMessageQueue.getInstance().setFogIP(fogIP);
    }

    // Repopulates the RegMessageQueue with messages in order
    private void repopulateMessages(Queue<String> queue) {
        Log.d("MESSAGE QUEUE", "Repopulate Queue Starting With: " + queue.peek());
        while (!queue.isEmpty()) {
            RegMessageQueue.getInstance().addMessage(queue.remove());
        }
    }

    // Sends messages to the Fog node, whose IP we now know
    // Also send ENTER message, each time
    private void sendMessages(Queue<String> messages) throws IOException {
        String fogIp = RegMessageQueue.getInstance().getFogIP();
        InetAddress fogAddr = InetAddress.getByName(fogIp);
        Socket fogSocket = new Socket();
        fogSocket.connect(new InetSocketAddress(fogAddr, FOG_PORT), 500);
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fogSocket.getOutputStream()));

            //First send an ENTER message before all other messages
            String username = SessionInfo.getInstance().getUsername();
            String protocolForm = MessageInfo.createEnterMessageString(username);
            printWriter.write(protocolForm);
            printWriter.flush();
            Log.d("ENTER MESSAGE", "SUCCESSFULLY SENT ENTER MESSAGE FROM SENDING QUEUE");

            // Get the HBR response from the FogQueue - Format: HBR (fqid)
            // Every time an enter message is sent, the Fog Queue sends an HBR response on the same connection
            fogSocket.setSoTimeout(10);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fogSocket.getInputStream()));
            String hbr = reader.readLine();
            // Get the FQId from the HBR message
            if (hbr == null) {
                throw new Exception();
            }
            int fqid = Integer.parseInt(hbr.split(" ")[1]);
            SessionInfo.getInstance().setCurrFogQ(fqid);
            Log.d("HBR RESPONSE", "SUCCESSFULLY RECEIVED HBR RESPONSE FROM FOQ QUEUE WITH FQID #" + fqid);

            fogSocket.close();
            Log.d("SEND QUEUE", "FIRST ELEMENT OF MESSAGE QUEUE: "  + messages.peek());
            for (String message : messages) {
                fogSocket = new Socket();
                fogSocket.connect(new InetSocketAddress(fogAddr, FOG_PORT), 500);
                printWriter = new PrintWriter(new OutputStreamWriter(fogSocket.getOutputStream()));

                printWriter.write(message);
                printWriter.flush();

                fogSocket.close();
                Log.d("SEND QUEUE", "SENT OUT SENDTO MESSAGE");
            }
        } catch (Exception e) {
            fogSocket.close();
            Log.d("SEND QUEUE", "SOMETHING HAPPENED");
        }
        fogSocket.close();
    }

    public void kill() {
        this.killThread = true;
        Log.d("KILL SENDING THREAD", "KILL SENDING THREAD");
    }

    @Override
    public void run() {
        while (!killThread) {
            RegMessageQueue messageQueue = RegMessageQueue.getInstance();
            try {
                Thread.sleep(2000);
                Log.d("SEND MESSAGE THREAD", "ABOUT TO UNLOAD MESSAGE QUEUE");
                Queue<String> messagesWaiting = messageQueue.emptyOutQueue();
                try {
                    sendMessages(messagesWaiting);
                } catch (IOException e) {
                    Log.d("FOG IP", "CAN'T CREATE CONNECTION WITH FOG DEVICE WITH IP " + RegMessageQueue.getInstance().getFogIP());
                    repopulateMessages(messagesWaiting);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("SENDING DONE", "USER ISN'T SENDING ANY MORE MESSAGES");
    }
}
