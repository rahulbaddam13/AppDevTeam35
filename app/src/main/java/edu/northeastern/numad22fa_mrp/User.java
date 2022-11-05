package edu.northeastern.numad22fa_mrp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class that represent an user object which includes the user name, UID, currentUserName and number of stickers sent.
 */
public class User {

    private final String userName;
    private final String UID;
    private String currentUserName;
    private Map<String, Long> stickerCountMap= new HashMap<>();


    /**
     * Constructs a user using the username.
     * @param userName name of the user.
     */
    public User(String userName) {
        this.userName = userName;
        this.UID = UUID.randomUUID().toString();
        stickerCountMap.put("2131165308", 0l);//happy fox
        stickerCountMap.put("2131165367", 0l);//sad fox
        stickerCountMap.put("2131165271", 0l);//angry fox
        stickerCountMap.put("2131165309", 0l);//hungry fox
        stickerCountMap.put("2131165325", 0l);//love fox
        stickerCountMap.put("2131165368", 0l);//sick fox
    }

    /**
     * Constructs a user object.
     * @param UID uid of the users.
     * @param userName username of the user.
     * @param currentUserName username of the user logged in at the app.
     */

    public User( String UID, String userName, String currentUserName, Map<String, Long> stickerCountMap) {
        this.userName = userName;
        this.UID = UID;
        this.currentUserName = currentUserName;
        this.stickerCountMap = stickerCountMap;
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

    public Map<String, Long> getStickerCountMap() {
        return stickerCountMap;
    }

    public void setStickerCountMap(Map<String, Long> stickerCountMap) {
        this.stickerCountMap = stickerCountMap;
    }
}
