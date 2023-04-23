package com.example.myweatherbase.activities;

import java.io.Serializable;

//ObjectMapper om = new ObjectMapper();
//Root2[] root = om.readValue(myJsonString, Root[].class);
 class LocalNames implements Serializable{
    public String wo;
    public String fj;
    public String wa;
    public String zh;
    public String vo;
    public String af;
    public String or;
    public String et;
    public String kl;
    public String sl;
    public String gd;
    public String ba;
    public String ms;
    public String zu;
    public String no;
    public String it;
    public String mn;
    public String kw;
    public String feature_name;
    public String na;
    public String ay;
    public String ps;
    public String sc;
    public String ml;
    public String hr;
    public String sk;
    public String lv;
    public String an;
    public String am;
    public String sd;
    public String ne;
    public String so;
    public String tk;
    public String gn;
    public String bh;
    public String ro;
    public String ky;
    public String ca;
    public String ln;
    public String kk;
    public String hy;
    public String ia;
    //@JsonProperty("to")
    public String myto;
    public String st;
    public String eo;
    public String ka;
    public String en;
    public String ff;
    public String ko;
    public String el;
    public String pl;
    public String gl;
    public String ce;
    public String fo;
    public String fa;
    public String tl;
    public String ku;
    public String mg;
    public String de;
    public String ascii;
    public String io;
    public String tt;
    public String he;
    public String sv;
    public String id;
    public String cy;
    public String eu;
    public String li;
    public String bm;
    public String hu;
    public String ny;
    public String hi;
    public String bo;
    public String yo;
    public String uk;
    public String my;
    public String lo;
    public String bg;
    public String sm;
    public String ja;
    public String tw;
    public String mt;
    public String fr;
    public String nl;
    public String om;
    public String su;
    public String yi;
    public String ug;
    public String uz;
    public String vi;
    public String gv;
    public String nn;
    public String sa;
    public String pt;
    public String gu;
    public String cs;
    public String ha;
    public String ab;
    public String sh;
    public String is;
    public String mi;
    public String kn;
    public String fy;
    public String cv;
    public String mr;
    public String lt;
    public String ta;
    public String th;
    public String sw;
    public String ur;
    public String sn;
    public String ee;
    public String fi;
    public String qu;
    public String nv;
    public String br;
    public String cu;
    public String tg;
    public String bi;
    public String tr;
    public String da;
    public String jv;
    public String av;
    public String mk;
    public String ru;
    public String co;
    public String kv;
    public String km;
    public String oc;
    public String si;
    public String sr;
    public String ar;
    public String pa;
    public String be;
    public String es;
    public String ig;
    public String bn;
    public String se;
    public String lb;
    public String ht;
    public String az;
    public String te;
    public String os;
    public String ie;
    public String sq;
    public String ga;
    public String bs;
    public String rm;
    public String iu;
    public String oj;
    public String cr;
}

public class CiudadGuardada implements Serializable {
    public String name;
    public LocalNames local_names;
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

