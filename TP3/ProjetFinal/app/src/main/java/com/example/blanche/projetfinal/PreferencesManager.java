package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private String PREFERENCES_FILE = "MyPrefsFile";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    public PreferencesManager(Context context) {
        settings = context.getSharedPreferences(PREFERENCES_FILE, 0);
        editor = settings.edit();
    }

    public void updateCurrentUser(String username, String password) {
        editor.putString("user", username);
        editor.putString("password", password);
        editor.commit();
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

}
