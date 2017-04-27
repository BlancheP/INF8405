package com.example.blanche.projetfinal;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    RadioButton wifiRadioButton;
    RadioButton anyNetworkRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wifiRadioButton = (RadioButton) findViewById(R.id.wifiRadioButton);
        anyNetworkRadioButton = (RadioButton) findViewById(R.id.anyNetworkRadioButton);

        initRadioButtonsState();

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(wifiRadioButton.isChecked()) {
                    DatabaseManager.getPreferencesManager().updateNetworkConnectivity("wifi");
                }
                else if (anyNetworkRadioButton.isChecked()) {
                    DatabaseManager.getPreferencesManager().updateNetworkConnectivity("any");
                }

                finish();
            }
        });

    }


    private void initRadioButtonsState() {

        String currentAcceptedConnectivity = DatabaseManager.getPreferencesManager().getNetworkConnectivity();

        if(currentAcceptedConnectivity == "wifi") {
            wifiRadioButton.setChecked(true);
            anyNetworkRadioButton.setChecked(false);
        }
        else if(currentAcceptedConnectivity == "any") {
            anyNetworkRadioButton.setChecked(true);
            wifiRadioButton.setChecked(false);
        }

        else{
            wifiRadioButton.setChecked(true);
        }

    }
}
