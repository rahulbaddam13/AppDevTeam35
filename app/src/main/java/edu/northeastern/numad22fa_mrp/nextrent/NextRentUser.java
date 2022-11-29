package edu.northeastern.numad22fa_mrp.nextrent;

import java.util.UUID;

/**
 * Class that represent an user object which includes the user name and UID.
 */
public class NextRentUser {

    private String UID;
    private String userName;
    private String userType;

    /**
     *
     * @param userName
     * @param userType
     */
    public NextRentUser(String userName, String userType) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
        this.userType = userType;
    }

    public String getUID() {
        return UID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }
}

