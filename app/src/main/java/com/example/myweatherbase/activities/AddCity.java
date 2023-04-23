//package com.example.myweatherbase.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//
//import com.example.myweatherbase.API.Connector;
//import com.example.myweatherbase.R;
//import com.example.myweatherbase.base.BaseActivity;
//import com.example.myweatherbase.base.CallInterface;
//import com.example.myweatherbase.base.Parameters;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class AddCity extends BaseActivity {
//
//    private Ciudad ciudad;
//    private Button añadir, cancelar;
//    private TextInputLayout nombre, latitude, longitude, url;
//    private CiudadGuardada ciudadGuardada;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_city);
//
//        añadir = findViewById(R.id.addCity);
//        cancelar = findViewById(R.id.cancelCity);
//        nombre = findViewById(R.id.cityName);
//        latitude = findViewById(R.id.cityLat);
//        longitude = findViewById(R.id.cityLong);
//        url = findViewById(R.id.cityUrl);
//
//        latitude.getEditText().setFocusable(false);
//        longitude.getEditText().setFocusable(false);
//
//        nombre.getEditText().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && !nombre.getEditText().getText().toString().matches("")) {
//                    showProgress();
//                    executeCall(new CallInterface() {
//                        @Override
//                        public void doInBackground() {
//                            ciudadGuardada = Connector.getConector().get(CiudadGuardada.class,
//                                    Parameters.BY_CITY_1 + nombre.getEditText().getText().toString() +
//                                    Parameters.BY_CITY_2);
//                        }
//
//                        @Override
//                        public void doInUI() {
//                            hideProgress();
//                            if (ciudadGuardada !=null){
//                                nombre.getEditText().setText(ciudadGuardada.name+", "+ ciudadGuardada.state+", "+ ciudadGuardada.country);
//                                latitude.getEditText().setText(String.valueOf(ciudadGuardada.lat));
//                                longitude.getEditText().setText(String.valueOf(ciudadGuardada.lon));
//                            }else{
//                                nombre.setError("No se ha encontrado la ciudad especificada.");
//                                latitude.getEditText().getText().clear();
//                                longitude.getEditText().getText().clear();
//                            }
//                        }
//                    });
//                    return true;
//                }
//                return false;
//            }
//        });
//
//
//        if (savedInstanceState!=null){
//            ciudadGuardada = (CiudadGuardada) savedInstanceState.getSerializable("root2");
//            nombre.getEditText().setText((String) savedInstanceState.getSerializable("Name"));
//            latitude.getEditText().setText((String) savedInstanceState.getSerializable("Lat"));
//            longitude.getEditText().setText((String) savedInstanceState.getSerializable("Lon"));
//            url.getEditText().setText((String) savedInstanceState.getSerializable("Url"));
//        }else{
//            nombre.requestFocus();
//        }
//
//        nombre.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (String.valueOf(charSequence).matches("")) {
//                    nombre.setErrorEnabled(true);
//                    nombre.setError("Debes completar el campo.");
//                } else {
//                    nombre.setErrorEnabled(false);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {}
//
//        });
//
//        cancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SelectCity.class);
//                setResult(RESULT_CANCELED, intent);
//                finish();
//            }
//        });
//
//        añadir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean flag;
//                flag = latitude.isErrorEnabled() || longitude.isErrorEnabled();
//
//                if (nombre.getEditText().getText().toString().matches("")){
//                    nombre.setErrorEnabled(true);
//                    nombre.setError("Debes completar el campo.");
//                    flag = true;
//                }
//
//                if (ciudadGuardada ==null)
//                    flag = true;
//
//                if (!flag) {
//                    Intent i = new Intent();
//                    i.putExtra("root2", ciudadGuardada);
//                    setResult(RESULT_OK, i);
//                    finish();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable("root2", ciudadGuardada);
//        outState.putSerializable("Name", nombre.getEditText().getText().toString());
//        outState.putSerializable("Lat", latitude.getEditText().getText().toString());
//        outState.putSerializable("Lon", longitude.getEditText().getText().toString());
//        outState.putSerializable("Url", url.getEditText().getText().toString());
//    }
//}
