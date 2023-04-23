package com.example.myweatherbase.activities;

import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    private List<CiudadGuardada> ciudades;
    private static CityRepository instance;

    private CityRepository() {
        ciudades = new ArrayList<>();
        ciudades.add(new CiudadGuardada("Valencia", 39.4697500, -0.3773900, "ES", "Valencian Community"));
        ciudades.add(new CiudadGuardada("El Puig", 39.5930300, -0.3154100, "ES", "Valencian Community"));
        ciudades.add(new CiudadGuardada("Alicante", 38.3451700, -0.4814900, "ES", "Valencian Community"));
    }

    public static CityRepository getInstance() {
        if (instance == null)
            instance = new CityRepository();
        return instance;
    }
    public List<CiudadGuardada> getCiudades() {
        return ciudades;
    }

    public void addCity(CiudadGuardada city) {
        ciudades.add(city);
    }
    public CiudadGuardada fixedValencia(){
        return ciudades.get(0);
    }
    public int getSize(){return ciudades.size();}
    public CiudadGuardada getCiudad(int pos){
        return ciudades.get(pos);
    }
}