package com.example.myweatherbase.base;

public class Parameters {
    public final static String API = "cc7ea74c6101f3d5ba6ee2b6e186b5be";
    public final static String BASE_URL = "https://api.openweathermap.org";

    public final static String ICON_URL_PRE = "http://openweathermap.org/img/wn/";
    public static final String ICON_URL_POST = "@2x.png";

    public final static String PREDICCION = "https://api.openweathermap.org/data/2.5/forecast?";
    public final static String BY_CITY_1 = "http://api.openweathermap.org/geo/1.0/direct?limit=1&";//appid=" + API+"&q=";
    public final static String BY_CITY_1_REVERSE = "http://api.openweathermap.org/geo/1.0/reverse?limit=1&";//appid="+API+"&lat=";
    public final static String CURRENT_1 = "https://api.openweathermap.org/data/2.5/weather?";//units="+UNITS+"&lang="+LANG+"&appid="+API;

}
