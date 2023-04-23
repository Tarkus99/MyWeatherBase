package com.example.myweatherbase.activities;

// Class obtained from https://json2csharp.com/code-converters/json-to-pojo
// and modified by adding Serializable interface

import java.io.Serializable;
import java.util.ArrayList;

class Main implements Serializable{
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public int pressure;
    public int sea_level;
    public int grnd_level;
    public int humidity;
    public double temp_kf;
}

class Weather implements Serializable{
    public int id;
    public String main;
    public String description;
    public String icon;
}

class Clouds implements Serializable{
    public int all;
}

class Wind implements Serializable{
    public double speed;
    public int deg;
    public double gust;
}

class Sys implements Serializable{
    public String pod;
}

class Rain implements Serializable{
    public double lluvia;
}

class List implements Serializable{
    public int dt;
    public Main main;
    public ArrayList<Weather> weather;
    public Clouds clouds;
    public Wind wind;
    public int visibility;
    public double pop;
    public Sys sys;
    public String dt_txt;
    public Rain rain;
}

class Coord implements Serializable{
    public double lat;
    public double lon;
}

class City implements Serializable {
    public int id;
    public String name;
    public Coord coord;
    public String country;
    public int population;
    public int timezone;
    public int sunrise;
    public int sunset;

}

public class Prediccion implements Serializable {
    public String cod;
    public int message;
    public int cnt;
    public ArrayList<List> list;
    public City city;

}

