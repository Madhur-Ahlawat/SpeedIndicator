package com.example.speedIndicator.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.speedIndicator.R;

public final class SettingsActivity extends AppCompatActivity {
    // Keys used by preferences
    public static final String KEY_INDICATOR_ENABLED = "indicatorEnabled";
    public static final String KEY_START_ON_BOOT = "startOnBoot";

    public static final String KEY_SHOW_SETTINGS_BUTTON = "showSettingsButton";
    public static final String KEY_INDICATOR_SPEED_TO_SHOW = "indicatorSpeedToShow";
    public static final String KEY_NOTIFICATION_PRIORITY = "notificationPriority";
    public static final String KEY_NOTIFICATION_ON_LOCK_SCREEN = "notificationOnLockScreen";

    public static final String KEY_INDICATOR_SPEED_UNIT = "indicatorSpeedUnit";

    // Other keys used with SharedPreferences
    public static final String KEY_INDICATOR_STARTED = "indicatorStarted";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

}
