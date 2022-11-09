package edu.northeastern.numad22fa_mrp;

import java.sql.Timestamp;

public class SentItem {
    private String imageID;
    private String count;

    public SentItem(String imageID, String count) {
        this.imageID = imageID;
        this.count = count;
    }

    public String getImageID() {
        return imageID;
    }

    public String getCount() {
        return count;
    }
}
