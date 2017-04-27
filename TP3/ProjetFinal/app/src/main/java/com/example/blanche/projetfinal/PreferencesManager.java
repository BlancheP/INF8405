package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private String PREFERENCES_FILE = "MyPrefsFile";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;
    private static float batteryPct;

    public PreferencesManager(Context context) {
        settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
        editor = settings.edit();
    }

    public void updateCurrentUser(String username, String password) {
        editor.putString("user", username);
        editor.putString("password", password);
        editor.commit();
    }

    public void updateNetworkConnectivity(String connectivity){
        editor.putString("connectivity", connectivity);
        editor.commit();
    }

    public String getNetworkConnectivity(){
        return settings.getString("connectivity", null);
    }

    public String getCurrentUser() {
        return settings.getString("user", null);
    }

    public String getCurrentPassword() {
        return settings.getString("password", null);
    }

    public void removeCurrentUser() {
        editor.putString("user", "");
        editor.putString("password", "");
        editor.commit();
    }

    public Float getBatteryPct() {
        return batteryPct;
    }

    public void setBatteryPct(Float pct) {
        batteryPct = pct;
    }

}
