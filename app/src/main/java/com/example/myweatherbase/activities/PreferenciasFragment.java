package com.example.myweatherbase.activities;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myweatherbase.R;

public class PreferenciasFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        setPreferencesFromResource(R.xml.prefe, rootKey);
        ListPreference themePrefrence = getPreferenceManager().findPreference("themePreference");
        themePrefrence.setOnPreferenceChangeListener((p, v) -> {
            ThemeSetup.applyTheme(ThemeSetup.Mode.valueOf((String) v));
            themePrefrence.setSummary((String)v);
            return true;
        });

        EditTextPreference apiPreference = getPreferenceManager().findPreference("API");
        apiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            SelectCity.activity.initialize();
            return true;
        });

        ListPreference unitsPreference = getPreferenceManager().findPreference("listUnits");
        unitsPreference.setOnPreferenceChangeListener((p, v) -> {
            ImageSetUp.applyImage(ImageSetUp.Unit.valueOf((String) v));
            unitsPreference.setSummary((String)v);
            return true;
        });

        ListPreference langPreference = getPreferenceManager().findPreference("listLang");
        langPreference.setOnPreferenceChangeListener((p, v) -> {
            langPreference.setSummary((String)v);
            return true;
        });
    }
}
