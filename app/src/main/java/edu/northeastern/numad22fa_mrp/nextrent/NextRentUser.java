package edu.northeastern.numad22fa_mrp.nextrent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.northeastern.numad22fa_mrp.Property;

/**
 * Class that represent an user object which includes the user name and UID.
 */
public class NextRentUser {

    private String UID;
    private String userName;
    private String userType;
    private Preference myPreference;
    private List<String> favorites;
    private int propPoints;

    /**
     *
     * @param userName
     * @param userType
     */
    public NextRentUser(String userName, String userType) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
        this.userType = userType;
        this.myPreference = new Preference();
        this.favorites = new ArrayList<>();
        this.propPoints = 0;
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

    public Preference getMyPreference() {
        return myPreference;
    }

    public void setMyPreference(Preference myPreference) {
        this.myPreference = myPreference;
    }

    public List<String> getFavoritePropertyList() {
        return favorites;
    }

    public void setFavoritePropertyList(List<String> favoritePropertyList) {
        this.favorites = favoritePropertyList;
    }

    public int getPropPoints() {
        return propPoints;
    }

    public void setPropPoints(int propPoints) {
        this.propPoints = propPoints;
    }
}

