package com.example.myweatherbase.activities;

import android.provider.ContactsContract;
import android.widget.ImageView;

import com.example.myweatherbase.R;

public class ImageSetUp {

    private ImageSetUp() {
    }
    public enum Unit {
        standard, metric, imperial;
    }

    public static void applyImage(Unit unit) {
        ImageView i = SelectCity.activity.findViewById(R.id.unitsImage);
        //ImageView i2 = MainActivity.activity.findViewById(R.id.imageUnit2);
        if (unit == Unit.metric) {
            i.setImageResource(R.mipmap.ic_celsius_foreground);
            //i2.setImageResource(R.mipmap.ic_celsius_foreground);
            SelectCity.activity.heatThreshold = 26;
        } else if(unit==Unit.imperial){
            i.setImageResource(R.mipmap.ic_farenheit_foreground);
            //i2.setImageResource(R.mipmap.ic_farenheit_foreground);
            SelectCity.activity.heatThreshold = 78.8f;
        }else{
            i.setImageResource(R.mipmap.ic_krlvin_foreground);
            SelectCity.activity.heatThreshold = 299.15f;
        }
    }

    public static void getThreshold(){
      Unit u = ImageSetUp.Unit.valueOf((String) MyPreferenceManager.getInstance(SelectCity.activity.getApplicationContext()).getUnits());
      if (u==Unit.metric)
          SelectCity.heatThreshold = 26;
      else if (u==Unit.imperial)
          SelectCity.heatThreshold = 76.8f;
      else
          SelectCity.heatThreshold = 299.15f;
    }
}
