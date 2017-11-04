package com.example.matthew.fogdemo;

/**
 * Created by Matt on 11/4/2017.
 */

public class SessionInfo {

    private static SessionInfo sessionInfo;
    private static String username;

    private SessionInfo() {
        username = "";
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

    public String getUsername() {
        return this.username;
    }
}
