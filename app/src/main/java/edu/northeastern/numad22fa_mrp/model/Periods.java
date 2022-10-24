package edu.northeastern.numad22fa_mrp.model;

public class Periods {
    private int number;
    private String name;
    private int temperature;
    private String icon;
    private String detailedForecast;
    private boolean isDaytime;

    public boolean isDaytime() {
        return isDaytime;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getDetailedForecast() {
        return detailedForecast;
    }
}
