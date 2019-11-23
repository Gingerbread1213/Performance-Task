package org.erhs.stem.project.time_management.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import org.erhs.stem.project.time_management.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
