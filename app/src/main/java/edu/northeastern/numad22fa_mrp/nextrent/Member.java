package edu.northeastern.numad22fa_mrp.nextrent;

public class Member {
    String id, username;

    public Member(String username, String id) {
        this.username = username;
        this.id  = id;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
