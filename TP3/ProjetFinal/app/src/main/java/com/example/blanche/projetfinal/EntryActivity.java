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

        if (pm.getCurrentUser() != null && !pm.getCurrentUser().equals("") && NetworkManager.hasValidConnectivity(getApplicationContext())) {
            DatabaseManager.userIsValid(pm.getCurrentUser(), pm.getCurrentPassword(), this, true);
        } else {
            Intent goToLogin = new Intent(this, LoginActivity.class);
            startActivity(goToLogin);
            finish();
        }
    }
}