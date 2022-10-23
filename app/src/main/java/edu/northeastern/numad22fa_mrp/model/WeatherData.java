package edu.northeastern.numad22fa_mrp.model;

public class WeatherData {

    private String latitude;
    private String longitude;
    private Daily daily;

    public WeatherData(String latitude, String longitude, Daily daily) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.daily = daily;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }
}
