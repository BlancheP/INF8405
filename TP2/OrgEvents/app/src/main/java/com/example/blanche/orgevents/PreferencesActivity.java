package com.example.blanche.orgevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreferencesActivity extends ActivityWithMenu {

    private static int frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        frequency = 5;

        final Button bSave = (Button) findViewById(R.id.bSavePreferences);
        final Button bReset = (Button) findViewById(R.id.bResetPreferences);
        final EditText etFrequency = (EditText) findViewById(R.id.etFrequency);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency(Integer.parseInt(etFrequency.getText().toString()));
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency(5);
                etFrequency.setText(String.valueOf(frequency));
            }
        });
    }

    public void setFrequency(int f) {
        frequency = f;
    }

    public int getFrequency() {
        return frequency;
    }
}
