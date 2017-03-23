package com.example.blanche.orgevents;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alain.trandang on 2017-03-09.
 */

public class OrganizerDashboardActivity extends ActivityWithMenu{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizerdashboard);

        TextView tvDashLoc1 = (TextView) findViewById(R.id.tvDashLoc1);
        TextView tvDashLoc2 = (TextView) findViewById(R.id.tvDashLoc2);
        TextView tvDashLoc3 = (TextView) findViewById(R.id.tvDashLoc3);

        TextView  locNote1 = (TextView) findViewById(R.id.locNote1);
        TextView  locNote2 = (TextView) findViewById(R.id.locNote2);
        TextView  locNote3 = (TextView) findViewById(R.id.locNote3);

        DatabaseManager.showLocNote(GroupSelectionActivity.getGroup(), locNote1, locNote2, locNote3);

        DatabaseManager.getLocationsName(GroupSelectionActivity.getGroup(),tvDashLoc1, tvDashLoc2, tvDashLoc3);

        Button bMap = (Button) findViewById(R.id.bMap);
        Button bCreateEvent = (Button) findViewById(R.id.bCreateEvent);
        Button bVote = (Button) findViewById(R.id.bVote);



        bMap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View w){
            Intent goToMap = new Intent(OrganizerDashboardActivity.this, MapsActivity.class);
            OrganizerDashboardActivity.this.startActivity(goToMap);
            finish();
        }
        });

        bVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Intent goToVote = new Intent(OrganizerDashboardActivity.this, LocationVoteActivity.class);
                OrganizerDashboardActivity.this.startActivity(goToVote);
                finish();
            }
        });


        if (savedInstanceState == null) {
            Fragment mapFrag = new MapFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.map_frag, mapFrag).commit();
        }


    }

}
