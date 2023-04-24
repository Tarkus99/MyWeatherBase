package com.example.myweatherbase.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweatherbase.API.Connector;
import com.example.myweatherbase.R;
import com.example.myweatherbase.base.BaseActivity;
import com.example.myweatherbase.base.CallInterface;
import com.example.myweatherbase.base.ImageDownloader;
import com.example.myweatherbase.base.Parameters;
import com.google.android.material.textfield.TextInputLayout;

public class SelectCity extends BaseActivity implements OnItemListener {
    private ImageView image;
    private LocationManager managerloc;
    private String proveedor;
    private TextView titulo, temp, desc, pais, wind, humidity, rain, estado;
    private TextInputLayout buscar;
    private ImageButton btnBuscar, update, actualUbi, addCiudad;
    private CurrentData currentData;
    private CiudadGuardada ciudadGuardada, ciudadAux;
    private RecyclerView lugaresGuardadosRecycler;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        titulo = findViewById(R.id.initTitulo);
        estado = findViewById(R.id.initEstado);
        pais = findViewById(R.id.initPais);
        image = findViewById(R.id.initImage);
        temp = findViewById(R.id.initTempValue);
        desc = findViewById(R.id.initDescValue);
        wind = findViewById(R.id.initWind);
        humidity = findViewById(R.id.initHumidity);
        rain = findViewById(R.id.initRain);
        buscar = findViewById(R.id.buscador);
        btnBuscar = findViewById(R.id.btnBuscar);
        update = findViewById(R.id.update);
        actualUbi = findViewById(R.id.ubi);
        addCiudad = findViewById(R.id.addCiudad);
        lugaresGuardadosRecycler = findViewById(R.id.myFavorites);

        LugaresGuardadosAdapter lugaresGuardadosAdapter = new LugaresGuardadosAdapter(this, this);
        lugaresGuardadosRecycler.setAdapter(lugaresGuardadosAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lugaresGuardadosRecycler.setLayoutManager(linearLayoutManager);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) lugaresGuardadosAdapter.notifyDataSetChanged();
                });

        initialize();

        update.setOnClickListener(view -> {
            initialize();
        });

        buscar.getEditText().addTextChangedListener(new MyTextWatcher(buscar));
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buscar.getEditText().getText().toString().matches("")) {
                    ciudadGuardada = null;
                    showProgress();
                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {
                            ciudadGuardada = Connector.getConector().get(CiudadGuardada.class,
                                    Parameters.BY_CITY_1 + buscar.getEditText().getText().toString());
                        }
                        @Override
                        public void doInUI() {
                            if (ciudadGuardada != null) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                i.putExtra("ciudadGuardada", ciudadGuardada);
                                someActivityResultLauncher.launch(i);
                                hideProgress();
                            } else {
                                hideProgress();
                                buscar.setErrorEnabled(true);
                                buscar.setError("La ciudad/población introducida no ha ofrecido resultados./"+R.string.no_network);
                            }
                        }
                    });
                }
            }
        });

        addCiudad.setOnClickListener(view -> {
        });

        actualUbi.setOnClickListener(view -> {
            managerloc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);
            criteria.setAltitudeRequired(true);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            proveedor = managerloc.getBestProvider(criteria, true);
            if (proveedor != null) {
                @SuppressLint("MissingPermission")
                Location location = managerloc.getLastKnownLocation(proveedor);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("enviarLat", String.valueOf(location.getLatitude()));
                i.putExtra("enviarLon", String.valueOf(location.getLongitude()));
                someActivityResultLauncher.launch(i);
            } else {
                Toast.makeText(this, "No se ha podido acceder a la ubicación.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialize() {
        managerloc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAltitudeRequired(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = managerloc.getBestProvider(criteria, true);
        showProgress();
        if (proveedor != null) {
            @SuppressLint("MissingPermission")
            Location location = managerloc.getLastKnownLocation(proveedor);
            executeCall(new CallInterface() {
                @Override
                public void doInBackground() {
                    ciudadGuardada =  Connector.getConector().get(CiudadGuardada.class,
                            Parameters.BY_CITY_1_REVERSE + location.getLatitude() +
                                    "&lon=" + location.getLongitude());
                    currentData = Connector.getConector().get(CurrentData.class,
                            Parameters.CURRENT_1 + "&lat=" + location.getLatitude() +
                                    "&lon=" + location.getLongitude());
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void doInUI() {
                    if (currentData!=null) {
                        rellenarDatos();
                        Toast.makeText(SelectCity.this, ciudadGuardada.name, Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(SelectCity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mostrarCiudadGuardada(0);
        }
        hideProgress();
    }

    private void mostrarCiudadGuardada(int position) {
        ciudadGuardada = CityRepository.getInstance().getCiudades().get(position);
        showProgress();
        executeCall(new CallInterface() {
            @Override
            public void doInBackground() {
                currentData = Connector.getConector().get(CurrentData.class,
                        Parameters.CURRENT_1 + "&lat=" + ciudadGuardada.lat +
                                "&lon=" + ciudadGuardada.lon);
            }
            @Override
            public void doInUI() {
                if (currentData!=null) {
                    rellenarDatos();
                }else
                    Toast.makeText(SelectCity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
            }
        });
        hideProgress();
    }

    private void rellenarDatos() {
        titulo.setText(ciudadGuardada.name);
        estado.setText(ciudadGuardada.state);
        pais.setText(currentData.sys.country);
        temp.setText(currentData.main.temp + "º");
        if (currentData.main.temp > 26)
            temp.setTextColor(getColor(R.color.RED));
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
        ciudadAux = CityRepository.getInstance().getCiudad(position);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("ciudadGuardada", ciudadAux);
        someActivityResultLauncher.launch(i);
    }
}