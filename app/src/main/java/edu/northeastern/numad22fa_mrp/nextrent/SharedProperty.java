package edu.northeastern.numad22fa_mrp.nextrent;

public class SharedProperty {
    String houseId, ownerId, likes, comments;

    public SharedProperty() {
    }


    public SharedProperty(String houseId, String ownerId, String likes, String comments) {
        this.houseId = houseId;
        this.ownerId = ownerId;
        this.likes = likes;
        this.comments = comments;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
