package com.example.blanche.orgevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etGroup = (EditText) findViewById(R.id.etGroup);
        final TextView welcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);
    }
}
