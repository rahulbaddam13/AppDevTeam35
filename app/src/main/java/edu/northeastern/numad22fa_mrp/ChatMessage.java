package edu.northeastern.numad22fa_mrp;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.Objects;

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
    private String readStatus;


    public ChatMessage(long imageID, String timestamp, String sender, String receiver) {
        this.imageID = imageID;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ChatMessage(int imageID, String sender, String receiver, String readStatus) {
        this.imageID = imageID;
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.sender = sender;
        this.receiver = receiver;
        this.readStatus = readStatus;
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

    public String getReadStatus() {
        return readStatus;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(imageID);
        parcel.writeString(timestamp);
        parcel.writeString(sender);
        parcel.writeString(receiver);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ChatMessage))
            return false;

        ChatMessage that = (ChatMessage) o;

        return getImageID() == that.getImageID()
                && getTimestamp().equals(that.getTimestamp())
                && getSender().equals(that.getSender())
                && getReceiver().equals(that.getReceiver());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImageID(), getTimestamp(), getSender(), getReceiver());
    }
}
