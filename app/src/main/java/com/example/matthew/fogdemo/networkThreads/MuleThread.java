package com.example.matthew.fogdemo.networkThreads;

import android.util.Log;
import android.webkit.ConsoleMessage;

import com.example.matthew.fogdemo.RegMessageQueue;
import com.example.matthew.fogdemo.SessionInfo;
import com.example.matthew.fogdemo.messages.Message;
import com.example.matthew.fogdemo.messages.MessageInfo;
import com.example.matthew.fogdemo.messages.MuleMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Matt on 11/6/2017.
 */

public class MuleThread extends Thread {

    private final int FOG_PORT = 3000;
    private boolean kill = false;

    private void sendMessage(MuleMessage muleMessage) throws IOException {

        String fogIp = RegMessageQueue.getInstance().getFogIP();
        InetAddress fogAddr = InetAddress.getByName(fogIp);
        Socket fogSocket = new Socket();
        fogSocket.connect(new InetSocketAddress(fogAddr, FOG_PORT), 500);

        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(fogSocket.getOutputStream()));

            //Send the MULE message in the correct protocol form
            String protocolForm = muleMessage.toProtocolForm();
            printWriter.write(protocolForm);
            printWriter.flush();
            Log.d("MULE MESSAGE", "SUCCESSFULLY SENT MULE MESSAGE FROM MULE MESSAGE LIST");


            fogSocket.close();
        } catch (Exception e) {
            fogSocket.close();
            e.printStackTrace();
        }
    }

    public void kill() {
        kill = true;
    }

    @Override
    public void run() {
        /* Loop every 2 seconds to try to send out messages from the mule list
         Individually check each MuleMessage to see if its FQID equals that of the
         current Fog Queue. If it doesn't, then send, otherwise put it back in the SessionInfo
         MuleMessages list */
        while (!kill) {
            if (isInterrupted()) {
                break;
            }
            ArrayList<MuleMessage> muleList= SessionInfo.getInstance().getMuleMessages();
            try {
                Thread.sleep(2000);
                Log.d("SEND MULE THREAD", "ABOUT TO SEND MULE MESSAGES");
                for (MuleMessage muleMessage : muleList) {
                    if (muleMessage.getFqId() != SessionInfo.getInstance().getCurrFogQId()) {
                        try {
                            sendMessage(muleMessage); //send the message
                        } catch (IOException e) {
                            Log.d("MULE THREAD", "ERROR SETTING UP FOG CONNECTION");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
