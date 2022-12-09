package edu.northeastern.numad22fa_mrp.nextrent;

import java.util.List;

/**
 * Class that represents the preference of a particular user.
 */
public class Preference {

    //Basic information.
    String fullName;
    String emailID;
    String phoneNumber;
    String legalSex;
    int age;

    //Information related to housing.
    List<String> locations;
    List<String> typeOfHouse;
    String numberOfBedrooms;
    String numberOfBathrooms;
    int minimumPrice;
    int maximumPrice;

    public Preference() {
    }

    public Preference(String fullName, String emailID, String phoneNumber,
                      String legalSex, int age, List<String> locations, List<String> typeOfHouse,
                      String numberOfBedrooms, String numberOfBathrooms, int minimumPrice,
                      int maximumPrice) {
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

    public String getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(String numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public String getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(String numberOfBathrooms) {
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

}
