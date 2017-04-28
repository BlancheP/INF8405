package com.example.blanche.projetfinal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by alain.trandang on 2017-04-26.
 */

public class NetworkManager {

    private NetworkManager() {}

    public static boolean hasValidConnectivity(Context context){

        //DatabaseManager.Init(context.getApplicationContext());
        PreferencesManager pm = DatabaseManager.getPreferencesManager();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if ((currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) && (pm.getNetworkConnectivity().equals("wifi")))
        {
            //Toast.makeText(context.getApplicationContext(), "You are using Wi-Fi!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else if (pm.getNetworkConnectivity().equals("any"))
        {
            //Toast.makeText(context.getApplicationContext(), "You are using any Network!", Toast.LENGTH_SHORT).show();
            return true;
        }

        else
        {
            //Toast.makeText(context.getApplicationContext(), "Not currently connected to a supported network type", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context.getApplicationContext(), "Current network info: " + pm.getNetworkConnectivity(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
