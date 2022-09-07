package com.example.speedIndicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.speedIndicator.settings.SettingsActivity;

public final class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null ||
                intent.getAction() == null ||
                !intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) return;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .putBoolean(SettingsActivity.KEY_INDICATOR_STARTED, false)
                .apply();

        if (!sharedPreferences.getBoolean(SettingsActivity.KEY_START_ON_BOOT, true)
                || !sharedPreferences.getBoolean(SettingsActivity.KEY_INDICATOR_ENABLED, true)) {
            return;
        }

        IndicatorServiceHelper.startService(context);
    }
}
