package com.example.myweatherbase.activities;

import java.io.Serializable;
import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
 class CurrentClouds implements Serializable{
    public int all;
}

 class CurrentCoord implements Serializable{
    public double lon;
    public double lat;
}

 class CurrentMain implements Serializable{
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public int pressure;
    public int humidity;
    public int sea_level;
    public int grnd_level;
}

 class CurrentRain implements Serializable{
    public double lluvia1hora;
}

public class CurrentData implements Serializable {
    public CurrentCoord coord;
    public ArrayList<CurrentWeather> weather;
    public String base;
    public CurrentMain main;
    public int visibility;
    public CurrentWind wind;
    public CurrentRain rain;
    public CurrentClouds clouds;
    public int dt;
    public CurrentSys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;
}

 class CurrentSys implements Serializable{
    public int type;
    public int id;
    public String country;
    public int sunrise;
    public int sunset;
}

 class CurrentWeather implements Serializable{
    public int id;
    public String main;
    public String description;
    public String icon;
}

 class CurrentWind implements Serializable{
    public double speed;
    public int deg;
    public double gust;
}

