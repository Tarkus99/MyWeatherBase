package com.example.myweatherbase.activities;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class MyTextWatcher implements TextWatcher {

    private TextInputLayout itemView;

    public MyTextWatcher(TextInputLayout view){
        this.itemView = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (itemView.isErrorEnabled()){
            itemView.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
