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

public class MainActivity extends BaseActivity implements CallInterface, OnPrediccionListener {

    private boolean reciboCiudadGuardada;
    private PrediccionAdapter prediccionAdapter;
    private RecyclerView recyclerView;
    private Prediccion prediccion;
    private CurrentData currentData;
    private CiudadGuardada ciudadGuardada;
    private TextView titulo, pais, temperatura, desc, rain, humidity, wind;
    private ImageView image;
    private ImageButton update, addFavorite;
    private String latitudRecibida, longitudRecibida;
    private ImageButton volverPaginaInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        pais=findViewById(R.id.initPais);
        titulo = findViewById(R.id.initTitulo);
        temperatura=findViewById(R.id.initTempValue);
        desc =findViewById(R.id.initDescValue);
        rain=findViewById(R.id.initRain);
        humidity=findViewById(R.id.initHumidity);
        wind=findViewById(R.id.initWind);
        image=findViewById(R.id.initImage);
        update=findViewById(R.id.update);
        addFavorite=findViewById(R.id.addFavorite);
        volverPaginaInicio =findViewById(R.id.imageButton2);
        recyclerView = findViewById(R.id.myRecyclerView2);

        prediccionAdapter = new PrediccionAdapter(this, this);
        recyclerView.setAdapter(prediccionAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState==null){
            reciboCiudadGuardada = getIntent().getExtras().containsKey("ciudadGuardada");
            if(reciboCiudadGuardada) {
                ciudadGuardada= (CiudadGuardada) getIntent().getExtras().getSerializable("ciudadGuardada");
                titulo.setText(ciudadGuardada.name);
                latitudRecibida = String.valueOf(ciudadGuardada.lat);
                longitudRecibida = String.valueOf(ciudadGuardada.lon);
            } else if (getIntent().getExtras().containsKey("currentData")) {
                currentData = (CurrentData) getIntent().getExtras().getSerializable("currentData");
                latitudRecibida = String.valueOf(currentData.coord.lat);
                longitudRecibida = String.valueOf(currentData.coord.lon);
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
            showProgress();
            executeCall(new CallInterface() {
                @Override
                public void doInBackground() {
                    currentData = Connector.getConector().get(CurrentData.class,
                            Parameters.CURRENT_1 + Parameters.CURRENT_2 + "&q=" + titulo.getText().toString());
                    prediccion = Connector.getConector().get(Prediccion.class,
                            Parameters.URL + Parameters.URL_OPTIONS + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
                }
                @Override
                public void doInUI() {
                    rellenarDatos();
                    prediccionAdapter.setPrediccion(prediccion);
                    hideProgress();
                }
            });
        });
        addFavorite.setOnClickListener(view -> {
            CityRepository.getInstance().addCity(ciudadGuardada);
            Toast.makeText(this, currentData.name + " se ha añadido a lugares guardados.", Toast.LENGTH_SHORT).show();
        });

        volverPaginaInicio.setOnClickListener(view -> {
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Root", prediccion);
        outState.putSerializable("Title", titulo.getText().toString());
    }

    // Realizamos la llamada y recogemos los datos en un objeto Root
    @Override
    public void doInBackground() {
        currentData = Connector.getConector().get(CurrentData.class,
                    Parameters.CURRENT_1 + Parameters.CURRENT_2 + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
        prediccion = Connector.getConector().get(Prediccion.class,
                Parameters.URL + Parameters.URL_OPTIONS + "&lat=" + latitudRecibida + "&lon=" + longitudRecibida);
    }

    // Una vez ya se ha realizado la llamada, ocultamos la barra de progreso y presentamos los datos
    @Override
    public void doInUI() {
        hideProgress();
        prediccionAdapter.setPrediccion(prediccion);
        rellenarDatos();
    }

    public void rellenarDatos(){
        pais.setText(currentData.sys.country);
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
    public void onPrediccionClick(int position) {
        Toast.makeText(this, position+"", Toast.LENGTH_SHORT).show();
    }
}