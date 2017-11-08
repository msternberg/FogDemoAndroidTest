package com.example.matthew.fogdemo.networkThreads;

import android.util.Log;

import com.example.matthew.fogdemo.RegMessageQueue;
import com.example.matthew.fogdemo.SessionInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Matt on 10/20/2017.
 */

public class FogIPThread extends Thread {


    private String raspberryIP = "";
    private int raspPort = 4000;
    private final String FOG_IP_REQUEST = "ANDROID REQ";
    private final String endMessage = "\r\n";

    public FogIPThread() {
        super();
        initializeRaspberryConnection();
    }

    private void initializeRaspberryConnection() {
        // Uses UDP broadcast discovery to find the IP address of the Raspberry Pi
        /* This MUST be called when the thread is created to make sure that the Android
           device can connect to the Raspberry Pi, which will be used to obtain the Fog Ip
            addresses */
        raspberryIP = SessionInfo.getInstance().getRaspIP();
    }


    // Pull the IP address of the Fog Node from the Raspberry Pi
    // Update the RegMessageQueue FogIP field to the pull value

    /* If we can't connect to the Raspberry Pi, call initializeRaspberryConnection()
       wait 3 seconds, and then skip to the next iteration of the loop
     */
    @Override
    public void run() {
        // Cycle every 3 seconds:
        while(true) {
            try {
                sleep(3000);
                // Establish a connection with the raspberry pi
                InetAddress raspAddress = Inet4Address.getByName(raspberryIP);
                Socket raspSocket = new Socket();
                Log.d("RASPBERRY CONNECT", "CONNECTING TO " + raspAddress.getHostName());
                try {
                    raspSocket.connect(new InetSocketAddress(raspAddress, raspPort), 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    raspSocket.close();
                    continue;
                }

                // Send a request for the fog IP
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(raspSocket.getOutputStream()));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(raspSocket.getInputStream()));
                printWriter.print(FOG_IP_REQUEST);
                printWriter.flush();

                // Read back the response from the raspberry pi, which should indicate the IP of the Fog device
                String raspResponse = bufferedReader.readLine();
                if (!raspResponse.equals("")) {
                    RegMessageQueue.getInstance().setFogIP(raspResponse);
                }
                raspSocket.close();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                initializeRaspberryConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
