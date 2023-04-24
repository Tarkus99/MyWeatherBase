package com.example.myweatherbase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherbase.API.Connector;
import com.example.myweatherbase.R;
import com.example.myweatherbase.base.BaseActivity;
import com.example.myweatherbase.base.CallInterface;
import com.example.myweatherbase.base.ImageDownloader;
import com.example.myweatherbase.base.Parameters;

public class MainActivity extends BaseActivity implements CallInterface, OnItemListener {
    private boolean reciboCiudad;
    private PrediccionAdapter prediccionAdapter;
    private RecyclerView recyclerView;
    private Prediccion prediccion;
    private CurrentData currentData;
    private CiudadGuardada ciudadGuardada;
    private TextView titulo, estado, pais, temperatura, desc, rain, humidity, wind;
    private ImageView image;
    private ImageButton update, addFavorite;
    private String latitudRecibida, longitudRecibida;

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titulo = findViewById(R.id.initTitulo);
        estado = findViewById(R.id.initEstado);
        pais=findViewById(R.id.initPais);
        temperatura=findViewById(R.id.initTempValue);
        desc =findViewById(R.id.initDescValue);
        rain=findViewById(R.id.initRain);
        humidity=findViewById(R.id.initHumidity);
        wind=findViewById(R.id.initWind);
        image=findViewById(R.id.initImage);
        update=findViewById(R.id.update);
        addFavorite=findViewById(R.id.addFavorite);
        recyclerView = findViewById(R.id.myRecyclerView2);

        prediccionAdapter = new PrediccionAdapter(this, this);
        recyclerView.setAdapter(prediccionAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState==null){
            if(getIntent().getExtras().containsKey("ciudadGuardada")) {
                reciboCiudad = true;
                ciudadGuardada= (CiudadGuardada) getIntent().getExtras().getSerializable("ciudadGuardada");
                latitudRecibida = String.valueOf(ciudadGuardada.lat);
                longitudRecibida = String.valueOf(ciudadGuardada.lon);
            } else {
                latitudRecibida = (String) getIntent().getExtras().getSerializable("enviarLat");
                longitudRecibida = (String) getIntent().getExtras().getSerializable("enviarLon");
            }
            showProgress();
            executeCall(this);

        }else{
            prediccion = (Prediccion)savedInstanceState.getSerializable("Root");
            titulo.setText((String)savedInstanceState.getSerializable("Title"));
            prediccionAdapter.setPrediccion(prediccion);
        }

        update.setOnClickListener(view -> {
            currentData=null;
            prediccion=null;
            showProgress();
            executeCall(new CallInterface() {
                @Override
                public void doInBackground() {
                    currentData = Connector.getConector().get(CurrentData.class,
                            Parameters.CURRENT_1 + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
                    prediccion = Connector.getConector().get(Prediccion.class,
                            Parameters.URL + Parameters.URL_OPTIONS + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
                }
                @Override
                public void doInUI() {
                    hideProgress();
                    if (currentData!=null && prediccion!=null) {
                        rellenarDatos();
                        prediccionAdapter.setPrediccion(prediccion);
                    }else{
                        Toast.makeText(MainActivity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        addFavorite.setOnClickListener(view -> {
            CityRepository.getInstance().addCity(ciudadGuardada);
            Toast.makeText(this, ciudadGuardada.name + " se ha añadido a lugares guardados.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Root", prediccion);
        outState.putSerializable("Title", titulo.getText().toString());
    }

    @Override
    public void doInBackground() {
        if (!reciboCiudad)
            ciudadGuardada = Connector.getConector().get(CiudadGuardada.class,
                    Parameters.BY_CITY_1_REVERSE + latitudRecibida + "&lon="+longitudRecibida);
        currentData = Connector.getConector().get(CurrentData.class,
                    Parameters.CURRENT_1 + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
        prediccion = Connector.getConector().get(Prediccion.class,
                Parameters.URL + Parameters.URL_OPTIONS + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
    }

    @Override
    public void doInUI() {
        hideProgress();
        if (currentData!=null && prediccion!=null && ciudadGuardada!=null) {
            rellenarDatos();
            prediccionAdapter.setPrediccion(prediccion);
        }else{
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
        }
    }

    public void rellenarDatos(){
        titulo.setText(ciudadGuardada.name);
        estado.setText(ciudadGuardada.state+", ");
        pais.setText(ciudadGuardada.country);
        temperatura.setText(currentData.main.temp + "º");
        if (currentData.main.temp > 26)
            temperatura.setTextColor(getColor(R.color.RED));
        desc.setText(Tools.primeraMayu(currentData.weather.get(0).description));
        wind.setText(currentData.wind.speed + "km/h");
        humidity.setText(currentData.main.humidity + "%");
        if (currentData.rain != null)
            rain.setText(currentData.rain.lluvia1hora + "");
        else
            rain.setText("0%");
        ImageDownloader.downloadImage(currentData.weather.get(0).icon, image);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, position+"", Toast.LENGTH_SHORT).show();
    }
}