package edu.northeastern.numad22fa_mrp.nextrent;

import java.util.List;

/**
 * Class that represents the preference of a particular user.
 */
public class Preference {

    private String UID;

    //Basic information.
    String fullName;
    String emailID;
    String phoneNumber;
    String legalSex;
    int age;

    //Information related to housing.
    List<String> locations;
    List<String> typeOfHouse;
    int numberOfBedrooms;
    int numberOfBathrooms;
    int minimumPrice;
    int maximumPrice;

    //Roommate Information.
    boolean roommates;
    int numberOfRoommates;
    List<String> typeOfRoommates;
    boolean pets;

    public Preference(String UID, String fullName, String emailID, String phoneNumber,
                      String legalSex, int age, List<String> locations, List<String> typeOfHouse,
                      int numberOfBedrooms, int numberOfBathrooms, int minimumPrice,
                      int maximumPrice, boolean roommates, int numberOfRoommates,
                      List<String> typeOfRoommates, boolean pets) {
        this.UID = UID;
        this.fullName = fullName;
        this.emailID = emailID;
        this.phoneNumber = phoneNumber;
        this.legalSex = legalSex;
        this.age = age;
        this.locations = locations;
        this.typeOfHouse = typeOfHouse;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.roommates = roommates;
        this.numberOfRoommates = numberOfRoommates;
        this.typeOfRoommates = typeOfRoommates;
        this.pets = pets;
    }

    public String getUID() {
        return UID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLegalSex() {
        return legalSex;
    }

    public void setLegalSex(String legalSex) {
        this.legalSex = legalSex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getTypeOfHouse() {
        return typeOfHouse;
    }

    public void setTypeOfHouse(List<String> typeOfHouse) {
        this.typeOfHouse = typeOfHouse;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public int getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(int minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public int getMaximumPrice() {
        return maximumPrice;
    }

    public void setMaximumPrice(int maximumPrice) {
        this.maximumPrice = maximumPrice;
    }

    public boolean isRoommates() {
        return roommates;
    }

    public void setRoommates(boolean roommates) {
        this.roommates = roommates;
    }

    public int getNumberOfRoommates() {
        return numberOfRoommates;
    }

    public void setNumberOfRoommates(int numberOfRoommates) {
        this.numberOfRoommates = numberOfRoommates;
    }

    public List<String> getTypeOfRoommates() {
        return typeOfRoommates;
    }

    public void setTypeOfRoommates(List<String> typeOfRoommates) {
        this.typeOfRoommates = typeOfRoommates;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }
}
