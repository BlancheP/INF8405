package com.example.blanche.projetfinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast t = Toast.makeText(getApplicationContext(),"Home", Toast.LENGTH_SHORT);
                    t.show();
                    return true;
                case R.id.navigation_profile:
                    Toast r = Toast.makeText(getApplicationContext(),"Profile", Toast.LENGTH_SHORT);
                    r.show();
                    return true;
                case R.id.navigation_map:
                    Toast b = Toast.makeText(getApplicationContext(),"Map", Toast.LENGTH_SHORT);
                    b.show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginActivity());
        fragmentTransaction.commit();
    }
}
