package com.example.blanche.orgevents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent registerIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(registerIntent);
        //dispatchTakePictureIntent();
    }
}
