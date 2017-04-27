package com.example.blanche.projetfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static com.example.blanche.projetfinal.NetworkActivity.currentNetworkPreference;

/**
 * Created by alain.trandang on 2017-04-22.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Answers queries about the state of network connectivity.
        // It also notifies applications when network connectivity changes
        ConnectivityManager connectivityManager =  (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Describes the status of a network interface of a given type
        // (currently either Mobile or Wi-Fi)
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Checks the user prefs and the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
        if (NetworkActivity.WIFI.equals(currentNetworkPreference)
                && networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            // If device has its Wi-Fi connection, sets refreshDisplay
            // to true. This causes the display to be refreshed when the user
            // returns to the app.
            NetworkActivity.refreshDisplay = true;
            Toast.makeText(context, "WiFi Connected!", Toast.LENGTH_SHORT).show();

            // If the setting is ANY network and there is a network connection
            // (which by process of elimination would be mobile), sets refreshDisplay to true.
        } else if (NetworkActivity.ANY.equals(currentNetworkPreference) && networkInfo != null) {

            NetworkActivity.refreshDisplay = true;

            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi), or because the pref setting is WIFI, and there
            // is no Wi-Fi connection.
            // Sets refreshDisplay to false.
        } else {

            NetworkActivity.refreshDisplay = false;
            Toast.makeText(context, "Lost Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
