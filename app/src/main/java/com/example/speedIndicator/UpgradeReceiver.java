package com.example.speedIndicator;

import static com.example.speedIndicator.settings.SettingsActivity.KEY_INDICATOR_STARTED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.speedIndicator.settings.SettingsActivity;

public final class UpgradeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null ||
                intent.getAction() == null ||
                !intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) return;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getBoolean(KEY_INDICATOR_STARTED, false)) {
            IndicatorServiceHelper.startService(context);
        }
    }
}
