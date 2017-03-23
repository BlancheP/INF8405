package com.example.blanche.orgevents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        long t = cal.getTimeInMillis();
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("description", "Reminder description");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "Un event");
        intent.putExtra("eventLocation", "ici");
        startActivity(intent);
    }
}
