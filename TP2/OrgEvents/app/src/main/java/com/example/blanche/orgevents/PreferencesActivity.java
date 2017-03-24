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

    private static int frequency = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        final Button bSave = (Button) findViewById(R.id.bSavePreferences);
        final Button bReset = (Button) findViewById(R.id.bResetPreferences);
        final EditText etFrequency = (EditText) findViewById(R.id.etFrequency);

        etFrequency.setText(String.valueOf(frequency));

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int interval = Integer.parseInt(etFrequency.getText().toString());
                setFrequency(interval);
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

    public static void setFrequency(int f) {
        frequency = f;
    }

    public static int getFrequency() {
        return frequency;
    }
}
