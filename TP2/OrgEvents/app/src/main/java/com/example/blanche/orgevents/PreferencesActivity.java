package com.example.blanche.orgevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity extends AppCompatActivity {

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
                int interval = Integer.parseInt(etFrequency.getText().toString());
                setFrequency(interval);
                //MapsActivity.setUpdateInterval(interval);
                finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent menuAction = null;
        switch (item.getItemId()) {
            case R.id.preferences:
                menuAction = new Intent(getApplicationContext(), PreferencesActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (menuAction != null)
            startActivity(menuAction);
        return true;
    }

    public void setFrequency(int f) {
        frequency = f;
    }

    public int getFrequency() {
        return frequency;
    }
}
