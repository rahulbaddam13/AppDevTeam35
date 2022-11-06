package edu.northeastern.numad22fa_mrp;

import java.sql.Timestamp;

/**
 * Chat message object which represents a chat by the image ID, time stamp at which the message was sent and who sent it.
 */
public class ChatMessage {

    private long imageID;


    private String timestamp;
    private String sender;

    public ChatMessage(int imageID, String sender) {
        this.imageID = imageID;
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.sender = sender;
    }

    public ChatMessage(long imageID, String timestamp, String sender) {
        this.imageID = imageID;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public long getImageID() {
        return imageID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }
}
