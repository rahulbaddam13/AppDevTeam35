package edu.northeastern.numad22fa_mrp.nextrent;

public class Comment {
    String uID, cID, time, comment, userName;

    public Comment() {
    }

    public Comment(String uID, String cID, String time, String comment, String userName) {
        this.uID = uID;
        this.cID = cID;
        this.time = time;
        this.comment = comment;
        this.userName = userName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
