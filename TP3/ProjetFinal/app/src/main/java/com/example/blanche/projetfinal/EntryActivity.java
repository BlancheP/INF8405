package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

public class EntryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.Init(getApplicationContext());
        PreferencesManager pm = DatabaseManager.getPreferencesManager();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        pm.setBatteryPct(level / (float)scale);

        if (pm.getCurrentUser() != null && !pm.getCurrentUser().equals("") && NetworkManager.hasValidConnectivity(getApplicationContext())) {
            DatabaseManager.userIsValid(pm.getCurrentUser(), pm.getCurrentPassword(), this, true);
        } else {
            Intent goToLogin = new Intent(this, LoginActivity.class);
            startActivity(goToLogin);
            finish();
        }
    }
}