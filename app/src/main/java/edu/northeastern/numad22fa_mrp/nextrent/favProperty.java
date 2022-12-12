package edu.northeastern.numad22fa_mrp.nextrent;

public class favProperty {
    String country, description, houseID, image, location, rooms, rent, state, type, userID,address,baths;

    public favProperty(String country, String description, String houseID, String image,
                       String location, String rooms, String rent, String state, String type, String userID,String address,String baths) {
        this.country = country;
        this.description = description;
        this.houseID = houseID;
        this.image = image;
        this.location = location;
        this.rooms = rooms;
        this.rent = rent;
        this.state = state;
        this.type = type;
        this.userID = userID;
        this.address = address;
        this.baths = baths;
    }

    public favProperty() {
    }

    public favProperty(String country, String description,
                       String image, String location, String rooms, String rent, String state, String type,String address, String baths) {
        this.country = country;
        this.description = description;
        this.image = image;
        this.location = location;
        this.rooms = rooms;
        this.rent = rent;
        this.state = state;
        this.type = type;
        this.address = address;
        this.baths =baths;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBaths() {
        return baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
//        String address = location + ", " + state + ", " + country;
        return this.address;
    }
}
