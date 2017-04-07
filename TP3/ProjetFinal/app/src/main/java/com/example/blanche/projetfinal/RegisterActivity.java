package com.example.blanche.projetfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(5, 5, conf);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final ImageButton bPhoto = (ImageButton) findViewById(R.id.bPhoto);

        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                dispatchTakePictureIntent();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                if (bitmap.sameAs(emptyBitmap)) {
                    Toast profilPicture = Toast.makeText(getApplicationContext(), "You must take a profile picture.", Toast.LENGTH_SHORT);
                    profilPicture.show();
                }
                else {
                    DatabaseManager.addUser(etPassword.getText().toString(), etUsername, RegisterActivity.this, ((BitmapDrawable)bPhoto.getDrawable()).getBitmap());
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap.eraseColor(Color.TRANSPARENT);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get("data");
            ImageButton b = (ImageButton) findViewById(R.id.bPhoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, b.getWidth(), b.getHeight(), true);
            b.setImageBitmap(bitmap);
        }
    }
}
