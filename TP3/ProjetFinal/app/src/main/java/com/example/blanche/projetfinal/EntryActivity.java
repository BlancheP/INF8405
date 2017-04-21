package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EntryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.Init(getApplicationContext());
        PreferencesManager pm = DatabaseManager.getPreferencesManager();
        if (pm.getCurrentUser() != null && !pm.getCurrentUser().equals("")) {
            DatabaseManager.userIsValid(pm.getCurrentUser(), pm.getCurrentPassword(), this, true);
        }
        else {
            Intent goToLogin = new Intent(this, LoginActivity.class);
            startActivity(goToLogin);
            finish();
        }
    }
}