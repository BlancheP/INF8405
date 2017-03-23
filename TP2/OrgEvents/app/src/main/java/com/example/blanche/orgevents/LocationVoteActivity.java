package com.example.blanche.orgevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationVoteActivity extends AppCompatActivity {

    public static List<String> locationsName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_vote);
        TextView tvLoc1 = (TextView) findViewById(R.id.tvLoc1);
        TextView tvLoc2 = (TextView) findViewById(R.id.tvLoc2);
        TextView tvLoc3 = (TextView) findViewById(R.id.tvLoc3);
        RatingBar rbLoc1 = (RatingBar) findViewById(R.id.rbLoc1);
        RatingBar rbLoc2 = (RatingBar) findViewById(R.id.rbLoc2);
        RatingBar rbLoc3 = (RatingBar) findViewById(R.id.rbLoc3);
        DatabaseManager.getLocationsName(GroupSelectionActivity.getGroup());

        if(!locationsName.isEmpty()) {
            tvLoc1.setText(locationsName.get(0));
            tvLoc2.setText(locationsName.get(1));
            tvLoc3.setText(locationsName.get(2));
        }



    }

    public  void setLocNames(List<String> locations ){
        locationsName = locations;
    }
}
