package edu.northeastern.numad22fa_mrp;

import java.util.UUID;

public class User {
    private String userName;
    private String UID;
    private UUID UUID_1 = UUID.fromString("58e0a7d7-eebc-11d8-9669-0800200c9a66");

    public User(String userName) {
        this.userName = userName;
        this.UID = String.valueOf(UUID_1.timestamp());
    }

    public String getUserName() {
        return userName;
    }

    public String getUID() {
        return UID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
