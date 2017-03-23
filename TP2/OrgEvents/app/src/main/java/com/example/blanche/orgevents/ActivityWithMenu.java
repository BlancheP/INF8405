package com.example.blanche.orgevents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.security.acl.Group;

/**
 * Created by Blanche on 3/14/2017.
 */

public class ActivityWithMenu extends AppCompatActivity {
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
                menuAction = new Intent(getApplicationContext(), LoginActivity.class);
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
            getApplicationContext().startActivity(menuAction);
        return true;
    }
}
