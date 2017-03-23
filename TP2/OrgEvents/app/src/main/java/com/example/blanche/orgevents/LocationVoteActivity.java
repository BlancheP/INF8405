package com.example.blanche.orgevents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        final RatingBar rbLoc1 = (RatingBar) findViewById(R.id.rbLoc1);
        final RatingBar rbLoc2 = (RatingBar) findViewById(R.id.rbLoc2);
        final RatingBar rbLoc3 = (RatingBar) findViewById(R.id.rbLoc3);

        Button bSendVote = (Button) findViewById(R.id.bSendVote);

        DatabaseManager.getLocationsName(GroupSelectionActivity.getGroup(),tvLoc1, tvLoc2, tvLoc3);

        bSendVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                DatabaseManager.computeNote(GroupSelectionActivity.getGroup(),rbLoc1, rbLoc2, rbLoc3);
                Intent goToMap = new Intent(LocationVoteActivity.this, MapsActivity.class);
                LocationVoteActivity.this.startActivity(goToMap);
                finish();
            }
        });


    }

    public  void setLocNames(List<String> locations ){
        locationsName = locations;
    }
}

