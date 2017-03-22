package com.example.blanche.orgevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationVoteActivity extends AppCompatActivity {

    final public static List<String> myFkingLocationsName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_vote);
        TextView tvLoc1 = (TextView) findViewById(R.id.tvLoc1);
        TextView tvLoc2 = (TextView) findViewById(R.id.tvLoc2);
        TextView tvLoc3 = (TextView) findViewById(R.id.tvLoc3);
        DatabaseManager.getLocationsName(GroupSelectionActivity.getGroup());

        if(!myFkingLocationsName.isEmpty()) {
            tvLoc1.setText(myFkingLocationsName.get(0));
            tvLoc2.setText(myFkingLocationsName.get(1));
            tvLoc3.setText(myFkingLocationsName.get(2));
        }

    }
}
