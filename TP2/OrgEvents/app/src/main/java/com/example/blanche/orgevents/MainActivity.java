package com.example.blanche.orgevents;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    ExampleDBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ExampleDBHelper(this);

        dbHelper.insertPerson("Blanche", "Female","MonGroupe");
        Cursor c = dbHelper.getAllPersons();
        c.moveToFirst();
        String a = c.getString(1);
        String b = c.getString(2);
        String d = c.getString(3);
    }
}
