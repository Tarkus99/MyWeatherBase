package com.example.myweatherbase.activities;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myweatherbase.R;

public class PreferenciasFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.prefe, rootKey);
        ListPreference themePrefrence = getPreferenceManager().findPreference("themePreference");
        themePrefrence.setOnPreferenceChangeListener((p,v)->{
            ThemeSetup.applyTheme(ThemeSetup.Mode.valueOf((String) v));
            return true;
        });
    }
}
