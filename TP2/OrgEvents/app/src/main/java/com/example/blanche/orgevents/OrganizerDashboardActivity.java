package com.example.blanche.orgevents;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.StringCharacterIterator;

/**
 * Created by alain.trandang on 2017-03-09.
 */

public class OrganizerDashboardActivity extends ActivityWithMenu{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizerdashboard);

        Button bCreateEvent = (Button) findViewById(R.id.bCreateEvent);
        Button bVote = (Button) findViewById(R.id.bVote);

        bVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                AlertDialog.Builder voteBuilder = new AlertDialog.Builder(OrganizerDashboardActivity.this);
                voteBuilder.setTitle("Vote for a location");
                voteBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                    }
                });
                voteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
                voteBuilder.show();
            }
        });


    }

}
