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

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        pm.setBatteryPct(level / (float)scale);

        if (pm.getCurrentUser() != null && !pm.getCurrentUser().equals("")) {
            DatabaseManager.userIsValid(pm.getCurrentUser(), pm.getCurrentPassword(), this, true);
        } else {
            Intent goToLogin = new Intent(this, LoginActivity.class);
            startActivity(goToLogin);
            finish();
        }

        /*
        if (currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            Toast.makeText(getApplicationContext(), "You are using Wi-Fi!", Toast.LENGTH_SHORT).show();
        }

        else if (currentNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            Toast.makeText(getApplicationContext(), "You are using Mobile Network!", Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getApplicationContext(), "Not currently connected to a supported network type", Toast.LENGTH_SHORT).show();
        }
        */

        /*
        if(pm.getNetworkConnectivity() == "wifi" &&
                currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        }
        else {
            Toast.makeText(getApplicationContext(), "Not currently connected to a supported network type", Toast.LENGTH_SHORT).show();
        }
        */
    }
}