package com.example.matthew.fogdemo.networkThreads;

import android.util.Log;

import com.example.matthew.fogdemo.ChatActivity;
import com.example.matthew.fogdemo.messages.MessageInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Matt on 11/3/2017.
 */

public class ReceiveMessagesThread extends Thread {

    private ChatActivity chatActivity;

    public ReceiveMessagesThread(ChatActivity main) {
        this.chatActivity = main;
    }


    private final int RECEIVE_PORT = 1500;

    @Override
    public void run() {
        // Create a socket listener
        ServerSocket listener = null;
        do {
            try {
                listener = new ServerSocket(RECEIVE_PORT);
            } catch (IOException e) {
                Log.d("RECEIVER SOCKET", "Error establishing listener socket");
            }
        } while (listener == null);
        // LOOP FOREVER:
        // Listen for messages on port RECEIVE_PORT
        while (true) {
            try {
                Socket clientConnection = listener.accept();
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
                    final String data = input.readLine();
                // When we have received the message completely, call MessageInfo.determineMessageType() to determine
                // if the message is either a TEXT message or a USERS number message
                    MessageInfo.MessageType messageType = MessageInfo.determineMessageType(data);

                // If the returned object is a TEXT message, display the contents in the main ui thread
                    if (messageType == MessageInfo.MessageType.TEXT) {
                        chatActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatActivity.onReceiveTextMessage(data);
                            }
                        });
                    }
                // If the returned object is a USERS message, change the number of users in the main ui thread
                    if (messageType == MessageInfo.MessageType.USERS_NUM) {
                        chatActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatActivity.onReceiveUserNumbersMessage(data);
                            }
                        });
                    }
                    clientConnection.close();

                } catch (IOException e) {
                    clientConnection.close();
                    e.printStackTrace();
                    continue;
                }
            } catch (IOException e) {
                Log.d("RECEIVER EXCEPTION", e.getMessage());
            }
        }
    }
}
