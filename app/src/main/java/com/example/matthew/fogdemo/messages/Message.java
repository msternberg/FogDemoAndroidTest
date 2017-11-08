package com.example.matthew.fogdemo.messages;

/**
 * Created by Jack on 10/30/2017.
 */

public class Message {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mDestName;

    private Message() {}

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getDestName() {
        return mDestName;
    }

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mDestName;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder destName(String destName) {
            mDestName = destName;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mDestName = mDestName;
            return message;
        }
    }
}