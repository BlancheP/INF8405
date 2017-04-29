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

        PreferencesManager pm = DatabaseManager.getPreferencesManager();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();

        //set connectivity to Any Network by default when the app is runned for the first time
        if(pm.getNetworkConnectivity() == null){
            pm.updateNetworkConnectivity("any");
        }

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
