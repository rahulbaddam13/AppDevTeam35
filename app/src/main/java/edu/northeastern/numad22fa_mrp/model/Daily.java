package edu.northeastern.numad22fa_mrp.model;

import java.util.List;

public class Daily {

    private List<String> time;
    private List<String> temperature_2m_max;
    private List<String> temperature_2m_min;

    public Daily(List<String> time, List<String> temperature_2m_max, List<String> temperature_2m_min) {
        this.time = time;
        this.temperature_2m_max = temperature_2m_max;
        this.temperature_2m_min = temperature_2m_min;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<String> getTemperature_2m_max() {
        return temperature_2m_max;
    }

    public void setTemperature_2m_max(List<String> temperature_2m_max) {
        this.temperature_2m_max = temperature_2m_max;
    }

    public List<String> getTemperature_2m_min() {
        return temperature_2m_min;
    }

    public void setTemperature_2m_min(List<String> temperature_2m_min) {
        this.temperature_2m_min = temperature_2m_min;
    }
}
