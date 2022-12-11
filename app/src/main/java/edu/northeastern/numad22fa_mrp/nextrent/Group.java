package edu.northeastern.numad22fa_mrp.nextrent;

public class Group {
    private String GroupID, name, description;

    public Group(String groupID, String name, String description) {
        GroupID = groupID;
        this.name = name;
        this.description = description;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
