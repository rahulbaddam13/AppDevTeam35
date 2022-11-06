package edu.northeastern.numad22fa_mrp;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Chat message object which represents a chat by the image ID, time stamp at which the message was sent and who sent it.
 */
public class ChatMessage implements Parcelable {


    /**
     * Implementing Parcelable for data persistence.
     */
    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    private long imageID;
    private String timestamp;
    private String sender;
    private String receiver;

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

    public ChatMessage(int imageID, String sender, String receiver) {
        this.imageID = imageID;
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.sender = sender;
        this.receiver = receiver;
    }

    protected ChatMessage(Parcel in) {
        imageID = in.readLong();
        timestamp = in.readString();
        sender = in.readString();
        receiver = in.readString();
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

    public String getReceiver() {
        return receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(imageID);
        parcel.writeString(timestamp);
        parcel.writeString(sender);
        parcel.writeString(receiver);
    }
}
