package com.example.myweatherbase.API;


import android.util.Log;

import com.example.myweatherbase.base.Parameters;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

//import okhttp3.MediaType;
//import okhttp3.RequestBody;


public class Connector{

    private static Connector connector;
    private static Conversor conversor;
    private static CallMethods callMethodsObject;

    public static Connector getConector(){
        if(connector == null){
            connector = new Connector();
            conversor = Conversor.getConversor();
            callMethodsObject = CallMethods.getCallMethodsObject();
        }
        return connector;
    }

//    public <T> List<T> getAsList(Class<T> clazz, String path){
//        String url = Parameters.PREDICCION + Parameters.URL_OPTIONS + path;
//        String jsonResponse = callMethodsObject.get(url);
//        if(jsonResponse != null)
//            return conversor.fromJsonList(jsonResponse, clazz);
//        return null;
//    }


    public <T> T get(Class<T> clazz, String path){
        Log.d("url", path);
        String jsonResponse = callMethodsObject.get(path);
        String aux;
        if (jsonResponse!=null) {
            if (jsonResponse.charAt(0) == '[') {
                aux = jsonResponse.substring(1, jsonResponse.length() - 1);
            } else {
                aux = jsonResponse;
            }
            return conversor.fromJson(aux, clazz);
        }else{
            return null;
        }
    }

//    public <T> T post(Class<T> clazz, T data, String path){
//        String url = Parameters.PREDICCION + Parameters.URL_OPTIONS + path;
//        String jsonObject = conversor.toJson(data);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject);
//        String jsonResponse = callMethodsObject.post(url, body);
//        if(jsonResponse != null)
//            return conversor.fromJson(jsonResponse, clazz);
//        return null;
//    }
//
//    public <T> T put(Class<T> clazz, T data, String path){
//        String url = Parameters.PREDICCION + Parameters.URL_OPTIONS + path;
//        String jsonObject = conversor.toJson(data);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject);
//        String jsonResponse = callMethodsObject.put(url, body);
//        if(jsonResponse != null)
//            return conversor.fromJson(jsonResponse, clazz);
//        return null;
//    }
//
//    public <T> T delete(Class<T> clazz, String path){
//        String url = Parameters.PREDICCION + Parameters.URL_OPTIONS + path;
//        String jsonResponse = callMethodsObject.delete(url);
//        if(jsonResponse != null)
//            return conversor.fromJson(jsonResponse, clazz);
//        return null;
//    }

}

