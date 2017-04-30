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

    //check if phone is connected to right network to be able to download feed
    public static boolean hasValidConnectivity(Context context){

        PreferencesManager pm = DatabaseManager.getPreferencesManager();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (((currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) && (pm.getNetworkConnectivity().equals("wifi")))) {
            return true;
        }

        else if (pm.getNetworkConnectivity().equals("any")) {
            return true;
        }

        else {
            return false;
        }
    }
}
