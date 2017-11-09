package com.example.matthew.fogdemo.messages;

/**
 * Created by Matt on 11/6/2017.
 */

public class MuleMessage {

    private int fqId;
    private String srcUser;
    private String destUser;
    private String timestamp;
    private String message;

    public MuleMessage(int fqId, String srcUser, String destUser, String timestamp, String message) {
        this.fqId = fqId;
        this.srcUser = srcUser;
        this.destUser = destUser;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getFqId() {
        return fqId;
    }

    public String getSrcUser() {
        return srcUser;
    }

    public String getDestUser() {
        return destUser;
    }

    public String getMessage() {
        return message;
    }

    public void setFqId(int fqId) {
        this.fqId = fqId;
    }

    public void setSrcUser(String srcUser){
        this.srcUser = srcUser;
    }

    public void setDestUser(String destUser) {
        this.destUser = destUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    //MULE fqid srcUser destUser timestamp message
    public static MuleMessage buildFromProtocolMessage(String muleText) {
        muleText = muleText.substring(5);
        String[] parts = muleText.split(" ");
        int fqid = Integer.parseInt(parts[0]);
        String srcUser = parts[1];
        String destUser = parts[2];
        String timeStamp = parts[3];
        int length = parts[0].length() + parts[1].length() + parts[2].length() + parts[3].length() + 4;
        String message = muleText.substring(length);
        return new MuleMessage(fqid, srcUser, destUser, timeStamp, message);

    }

    public String toProtocolForm() {
        return String.format("MULE %s %s %s %s %s", Integer.toString(fqId), srcUser, destUser, timestamp, message);
    }
}
