package edu.northeastern.numad22fa_mrp;

import java.util.UUID;

/**
 * Class that represent an user object which includes the user name, UID, currentUserName and number of stickers sent.
 */
public class User {

    private final String userName;
    private final String UID;
    private String currentUserName;
    private long stickersSent;

    /**
     * Constructs a user using the username.
     * @param userName name of the user.
     */
    public User(String userName) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
        this.stickersSent = 0; //Initially when the user is created, stickers sent is set to 0.
    }

    /**
     * Constructs a user object.
     * @param UID uid of the users.
     * @param userName username of the user.
     * @param currentUserName username of the user logged in at the app.
     * @param stickersSent number of stickers the current user has used.
     */
    public User(String UID, String userName, String currentUserName, long stickersSent) {
        this.userName = userName;
        this.UID = UID;
        this.currentUserName = currentUserName;
        this.stickersSent = stickersSent;
    }

    public String getUserName() {
        return userName;
    }

    public String getUID() {
        return UID;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public long getStickersSent() {
        return stickersSent;
    }

    public void setStickersSent(long stickersSent) {
        this.stickersSent = stickersSent;
    }
}
