package edu.northeastern.numad22fa_mrp.model;

public class WeatherRecyclerViewItem {
    public static final int LOCATION=0;
    public static final int DATE=1;
    public static final int INFORMATION=2;
    public int type;

    public WeatherRecyclerViewItem(int type) {
        this.type = type;
    }

    public static class Header extends WeatherRecyclerViewItem {
         private final String city;
         private final String state;


        public Header(int type, String city, String state) {
            super(type);
            this.city = city;
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }


    }

    public static class Date extends WeatherRecyclerViewItem {
        private final String date;

        public Date(int type, String date) {
            super(type);
            this.type = type;
            this.date = date;
        }

        public String getDate() {
            return date;
        }
    }

    public static class Period extends WeatherRecyclerViewItem {
        private String name;
        private int temperature;
        private String icon;
        private String description;

        public Period(int type, String name, int temperature, String icon, String description) {
            super(type);
            this.name = name;
            this.temperature = temperature;
            this.icon = icon;
            this.description = description;
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

        public String getDescription() {
            return description;
        }
    }

}
