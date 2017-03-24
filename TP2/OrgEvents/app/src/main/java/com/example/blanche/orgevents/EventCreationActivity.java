package com.example.blanche.orgevents;

import android.content.Intent;

import java.text.ParseException;
import 	java.text.SimpleDateFormat;
import java.util.Date;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EventCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        final EditText etEvent = (EditText) findViewById(R.id.etEventName);
        final Spinner sLocNames = (Spinner) findViewById(R.id.sLocNames);
        final EditText tmDesc = (EditText) findViewById(R.id.tmDesc);
        final EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
        final EditText etStartTime = (EditText) findViewById(R.id.etStartTime);
        final EditText etEndDate = (EditText) findViewById(R.id.etEndDate);
        final EditText etEndTime = (EditText) findViewById(R.id.etEndTime);
        final Button bCreate = (Button) findViewById(R.id.bCreate);

        DatabaseManager.getLocationsSpinner(GroupSelectionActivity.getGroup(), this, sLocNames);


        bCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {

                DatabaseManager.addEventToBD(GroupSelectionActivity.getGroup(), etEvent.getText().toString(),
                        sLocNames.getSelectedItem().toString(), tmDesc.getText().toString(),
                        etStartDate.getText().toString(), etStartTime.getText().toString(),
                        etEndDate.getText().toString(), etEndTime.getText().toString());

                //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.fragment_container, new OrganizerDashboardFragment());
                //fragmentTransaction.commit();

                //Intent goToMap = new Intent(EventCreationActivity.this, MapsActivity.class);
                //EventCreationActivity.this.startActivity(goToMap);
                finish();
            }
        });




    }
}
