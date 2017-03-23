package com.example.blanche.orgevents;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by alain.trandang on 2017-03-09.
 */

public class OrganizerDashboardActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizerdashboard);

        Button bCreateEvent = (Button) findViewById(R.id.bCreateEvent);
        Button bVote = (Button) findViewById(R.id.bVote);

        bVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Intent goToVote = new Intent(OrganizerDashboardActivity.this, LocationVoteActivity.class);
                OrganizerDashboardActivity.this.startActivity(goToVote);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent menuAction = null;
        switch (item.getItemId()) {
            case R.id.preferences:
                menuAction = new Intent(getApplicationContext(), PreferencesActivity.class);
                break;
            case R.id.leave_group:
                DatabaseManager.quitGroup(getApplicationContext());
                break;
            case R.id.logout:
                menuAction = new Intent(getApplicationContext(), LoginActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (menuAction != null)
            startActivity(menuAction);
        return true;
    }

}
