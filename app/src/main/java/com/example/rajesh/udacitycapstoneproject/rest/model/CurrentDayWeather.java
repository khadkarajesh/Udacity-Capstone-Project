package com.example.rajesh.udacitycapstoneproject.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CurrentDayWeather {

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("base")
    public String base;

    @SerializedName("main")
    public Main main;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("rain")
    public Rain rain;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("dt")
    public int dt;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;

    public static class Coord {
        @SerializedName("lon")
        public double lon;

        @SerializedName("lat")
        public double lat;
    }

    public static class Weather {
        @SerializedName("id")
        public int id;

        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public static class Main {
        @SerializedName("temp")
        public double temp;

        @SerializedName("pressure")
        public double pressure;

        @SerializedName("humidity")
        public int humidity;

        @SerializedName("temp_min")
        public double temp_min;

        @SerializedName("temp_max")
        public double temp_max;

        @SerializedName("sea_level")
        public double sea_level;

        @SerializedName("grnd_level")
        public double grnd_level;
    }

    public static class Wind {
        @SerializedName("speed")
        public double speed;
        @SerializedName("deg")
        public double deg;
    }

    public static class Rain {
        @SerializedName("3h")
        public double m3h;
    }

    public static class Clouds {
        @SerializedName("all")
        public int all;
    }

    public static class Sys {
        @SerializedName("message")
        public double message;

        @SerializedName("country")
        public String country;

        @SerializedName("sunrise")
        public int sunrise;

        @SerializedName("sunset")
        public int sunset;
    }
}
