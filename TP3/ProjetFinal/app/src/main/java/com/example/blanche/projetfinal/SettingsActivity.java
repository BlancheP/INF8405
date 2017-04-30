package com.example.blanche.projetfinal;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    RadioButton wifiRadioButton;
    RadioButton anyNetworkRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        wifiRadioButton = (RadioButton) findViewById(R.id.wifiRadioButton);
        anyNetworkRadioButton = (RadioButton) findViewById(R.id.anyNetworkRadioButton);

        initRadioButtonsState();

        //To get the current battery level
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        Float batteryPct = new Float(level / scale);
        Float deltaPower = PowerLevelReceiver.initialPowerLevel - batteryPct;

        TextView tvBattPct = (TextView) findViewById(R.id.tvBattPct);
        tvBattPct.setText( deltaPower.toString());


        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(wifiRadioButton.isChecked()) {
                    DatabaseManager.getPreferencesManager().updateNetworkConnectivity("wifi");
                    Toast.makeText(SettingsActivity.this, "Connectivity set to Wi-Fi Only", Toast.LENGTH_SHORT).show();
                }
                else if (anyNetworkRadioButton.isChecked()) {
                    DatabaseManager.getPreferencesManager().updateNetworkConnectivity("any");
                    Toast.makeText(SettingsActivity.this, "Connectivity set to Any Network", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void initRadioButtonsState() {

        String currentAcceptedConnectivity = DatabaseManager.getPreferencesManager().getNetworkConnectivity();

        if(currentAcceptedConnectivity == null){
            //set connectivity to Any Network by default when the app is runned for the first time
            DatabaseManager.getPreferencesManager().updateNetworkConnectivity("any");
        }
        else if(currentAcceptedConnectivity.equals("wifi")) {
            wifiRadioButton.setChecked(true);
            anyNetworkRadioButton.setChecked(false);
        }
        else if(currentAcceptedConnectivity.equals("any")) {
            anyNetworkRadioButton.setChecked(true);
            wifiRadioButton.setChecked(false);
        }
    }
}
