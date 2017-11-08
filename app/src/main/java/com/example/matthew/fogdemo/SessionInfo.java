package com.example.matthew.fogdemo;

import android.util.Log;

import com.example.matthew.fogdemo.messages.MuleMessage;

import java.util.ArrayList;

/**
 * Created by Matt on 11/4/2017.
 */

public class SessionInfo {

    private static SessionInfo sessionInfo;
    private static String username;
    private static int currFogQ;
    private String raspIP;
    private static ArrayList<MuleMessage> muleMessages;

    private SessionInfo() {
        username = "";
        currFogQ = -1;
        muleMessages = new ArrayList<>();
        raspIP = "";
    }

    public static SessionInfo getInstance() {
        if (sessionInfo == null) {
            sessionInfo = new SessionInfo();
        }
        return sessionInfo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrFogQ(int fogQId) {
        this.currFogQ = fogQId;
        Log.d("SESSION INFO", "CURRENT FQID IS " + fogQId);
    }

    public String getUsername() {
        return this.username;
    }

    public int getCurrFogQId() {
        return this.currFogQ;
    }

    public ArrayList<MuleMessage> getMuleMessages() {
        return muleMessages;
    }

    public void addMuleMessage(MuleMessage muleMessage) {
        muleMessages.add(muleMessage);
        Log.d("SESSION INFO MULE", "MULE MESSAGE ADDED TO LIST");
    }

    public void setRaspIP(String ip) {
        this.raspIP = ip;
    }

    public String getRaspIP() {
        return this.raspIP;
    }

}
