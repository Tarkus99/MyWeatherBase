package com.example.myweatherbase.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    public static SelectCity activity;
    public static float heatThreshold;
    private ImageView image, imageUnit1;
    private LocationManager managerloc;
    private String proveedor;
    private TextView titulo, temp, desc, pais, wind, humidity, rain, estado;
    private TextInputLayout buscar;
    private ImageButton btnBuscar, update, actualUbi, addCiudad, settings;
    private CurrentData currentData;
    private CiudadGuardada ciudadGuardada, ciudadAux;
    private RecyclerView lugaresGuardadosRecycler;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        activity = this;
        titulo = findViewById(R.id.initTitulo);
        estado = findViewById(R.id.initEstado);
        pais = findViewById(R.id.initPais);
        image = findViewById(R.id.initImage);
        imageUnit1 = findViewById(R.id.unitsImage);
        temp = findViewById(R.id.initTempValue);
        desc = findViewById(R.id.initDescValue);
        wind = findViewById(R.id.initWind);
        humidity = findViewById(R.id.initHumidity);
        rain = findViewById(R.id.initRain);
        buscar = findViewById(R.id.buscador);
        btnBuscar = findViewById(R.id.btnBuscar);
        update = findViewById(R.id.update);
        settings = findViewById(R.id.settingsMain);
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
                    if (result.getResultCode() == RESULT_OK)
                        lugaresGuardadosAdapter.notifyDataSetChanged();
                });

        initialize();

        update.setOnClickListener(view -> {
            temp.setTextColor(getResources().getColor(R.color.miamarillo));
            initialize();
        });
        settings.setOnClickListener(vie -> {
            Intent intent = new Intent(this, PreferencesActivities.class);
            startActivity(intent);
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
                            llamadaCiudadGuardada();
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
                                buscar.setError("La ciudad/población introducida no ha ofrecido resultados./" + R.string.no_network);
                            }
                        }
                    });
                }
            }
        });

        addCiudad.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialogo_personalizado, null);
            dialogBuilder.setView(v);
            ////
            TextView txLat = v.findViewById(R.id.diaLat);
            TextView txLon = v.findViewById(R.id.diaLon);
            ImageButton diaDelete = v.findViewById(R.id.diaDelete);
            ImageButton diaBuscar = v.findViewById(R.id.diaBuscar);
            TextInputLayout diaBuscador = v.findViewById(R.id.diaBuscador);
            ////

            dialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialogBuilder.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ciudadAux != null) {
                        CityRepository.getInstance().addCity(ciudadAux);
                        lugaresGuardadosAdapter.notifyDataSetChanged();
                        Toast.makeText(SelectCity.this, "Ciudad guardada.", Toast.LENGTH_LONG).show();
                        dialogInterface.cancel();
                    }
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();

            diaDelete.setOnClickListener(view1 -> {
                diaBuscador.getEditText().setText("");
                diaBuscador.setEnabled(true);
                txLat.setText("Latitud:");
                txLon.setText("Longitud:");
                ciudadAux=null;
            });
            diaBuscar.setOnClickListener(view1 -> {
                if (!diaBuscador.getEditText().getText().toString().matches("")) {
                    executeCall(new CallInterface() {
                        @Override
                        public void doInBackground() {
                            ciudadAux = Connector.getConector().get(CiudadGuardada.class,
                                    Parameters.BY_CITY_1 +
                                            "q=" + diaBuscador.getEditText().getText().toString() +
                                            "&appid=" + MyPreferenceManager.getInstance(getApplicationContext()).getApi() +
                                            "&lang=" + MyPreferenceManager.getInstance(getApplicationContext()).getLang());
                        }

                        @Override
                        public void doInUI() {
                            if (ciudadAux != null) {
                                diaBuscador.getEditText().setText(ciudadAux.name +
                                        ", " + ciudadAux.state +
                                        ", " + ciudadAux.country);
                                txLat.setText("Latitud: " + ciudadAux.lat);
                                txLon.setText("Longitud: " + ciudadAux.lon);
                                diaBuscador.setErrorEnabled(false);
                                diaBuscador.setEnabled(false);
                            }else{
                                diaBuscador.setErrorEnabled(true);
                                diaBuscador.setError("Sin resultados.");
                            }
                        }
                    });
                }
            });

        });


        actualUbi.setOnClickListener(vi -> {
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


    public void initialize() {
        ImageSetUp.getThreshold();
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
                    llamadaCiudadReverse(location);
                    llamadaCurrentInit(location);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void doInUI() {
                    if (currentData!=null) {
                        rellenarDatos();
                    } else
                        Toast.makeText(SelectCity.this, R.string.no_network, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mostrarCiudadGuardada(0);
            Toast.makeText(SelectCity.this, R.string.no_ubi, Toast.LENGTH_SHORT).show();
        }
        hideProgress();
    }

    private void mostrarCiudadGuardada(int position) {
        ciudadGuardada = CityRepository.getInstance().getCiudades().get(position);
        showProgress();
        executeCall(new CallInterface() {
            @Override
            public void doInBackground() {
                llamadaCurrentDataNoUbi();
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
        temp.setText(String.valueOf(currentData.main.temp));
        if (currentData.main.temp > heatThreshold)
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
    private void llamadaCurrentDataNoUbi() {
        currentData = Connector.getConector().get(CurrentData.class,
                Parameters.CURRENT_1 +
                        "lat=" + ciudadGuardada.lat +
                        "&lon=" + ciudadGuardada.lon +
                        "&appid=" + MyPreferenceManager.getInstance(getApplicationContext()).getApi() +
                        "&units=" + MyPreferenceManager.getInstance(getApplicationContext()).getUnits()+
                        "&lang=" + MyPreferenceManager.getInstance(getApplicationContext()).getLang());
    }
    private void llamadaCurrentInit(Location location) {
        currentData = Connector.getConector().get(CurrentData.class,
                Parameters.CURRENT_1 +
                        "lat=" + location.getLatitude() +
                        "&lon=" + location.getLongitude() +
                        "&appid=" + MyPreferenceManager.getInstance(getApplicationContext()).getApi() +
                        "&units=" + MyPreferenceManager.getInstance(getApplicationContext()).getUnits()+
                        "&lang=" + MyPreferenceManager.getInstance(getApplicationContext()).getLang());
    }

    private void llamadaCiudadReverse(Location location) {
        ciudadGuardada =  Connector.getConector().get(CiudadGuardada.class,
                Parameters.BY_CITY_1_REVERSE +
                        "lat=" + location.getLatitude() +
                        "&lon=" + location.getLongitude() +
                        "&appid=" + MyPreferenceManager.getInstance(getApplicationContext()).getApi() +
                        "&units=" + MyPreferenceManager.getInstance(getApplicationContext()).getUnits()+
                        "&lang=" + MyPreferenceManager.getInstance(getApplicationContext()).getLang());
    }
    private void llamadaCiudadGuardada() {
        ciudadGuardada = Connector.getConector().get(CiudadGuardada.class,
                Parameters.BY_CITY_1 +
                        "q=" +buscar.getEditText().getText().toString()+
                        "&appid=" + MyPreferenceManager.getInstance(getApplicationContext()).getApi() +
                        "&units=" + MyPreferenceManager.getInstance(getApplicationContext()).getUnits()+
                        "&lang=" + MyPreferenceManager.getInstance(getApplicationContext()).getLang());
    }

}