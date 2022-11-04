package edu.northeastern.numad22fa_mrp;

import java.sql.Timestamp;
import java.util.UUID;

public class User {
    private String userName;
    private String UID;

    public User(String userName) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
    }

    public User(String UID, String userName) {
        this.userName = userName;
        this.UID = UID;
    }

    public User() {
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

}
