package com.example.speedIndicator.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.speedIndicator.IndicatorServiceHelper;
import com.example.speedIndicator.R;

public final class SettingsFragment extends PreferenceFragment {
    private SharedPreferences mSharedPref;
    private Context mContext;

    private final SharedPreferences.OnSharedPreferenceChangeListener mSettingsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(SettingsActivity.KEY_INDICATOR_ENABLED)) {
                if (mSharedPref.getBoolean(SettingsActivity.KEY_INDICATOR_ENABLED, true)) {
                    startIndicatorService();
                } else {
                    stopIndicatorService();
                }
            } else if (!key.equals(SettingsActivity.KEY_START_ON_BOOT)
                    && !key.equals(SettingsActivity.KEY_INDICATOR_STARTED)) {
                startIndicatorService();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        addPreferencesFromResource(R.xml.preferences);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);

        if (mSharedPref.getBoolean(SettingsActivity.KEY_INDICATOR_ENABLED, true)) {
            startIndicatorService();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPref.registerOnSharedPreferenceChangeListener(mSettingsListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPref.unregisterOnSharedPreferenceChangeListener(mSettingsListener);
    }

    private void startIndicatorService() {
        IndicatorServiceHelper.startService(mContext);
    }

    private void stopIndicatorService() {
        IndicatorServiceHelper.stopService(mContext);
    }

}
