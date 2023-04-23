package com.example.myweatherbase.activities;

import java.io.Serializable;

//ObjectMapper om = new ObjectMapper();
//Root2[] root = om.readValue(myJsonString, Root[].class);
public class CiudadGuardada implements Serializable {
    public String name;
    //public LocalNames local_names;
    public double lat;
    public double lon;
    public String country;
    public String state;

   public CiudadGuardada() {
   }

   public CiudadGuardada(String name, double lat, double lon, String country, String state) {
      this.name = name;
      this.lat = lat;
      this.lon = lon;
      this.country = country;
      this.state = state;
   }
      @Override
   public String toString() {
      return name +", "+state+", "+country;
   }
}

