package com.example.blanche.projetfinal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private final int REGISTER_CAMERA_REQUEST_CODE = 100;

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
                CameraManager.dispatchTakePictureIntent(RegisterActivity.this, REGISTER_CAMERA_REQUEST_CODE);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {
                boolean hasPhoto = false;
                Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                if (!bitmap.sameAs(emptyBitmap)) {
                    hasPhoto = true;
                }

                if (etUsername.getText().toString().trim().length() <= 0) {
                    etUsername.setError("You need a username");
                }
                if (etPassword.getText().toString().trim().length() <= 0) {
                    etPassword.setError("You need a password");
                }

                if (etPassword.getText().toString().trim().length() > 0 && etUsername.getText().toString().length() > 0)
                    DatabaseManager.addUser(etPassword.getText().toString(), etUsername, RegisterActivity.this, hasPhoto, ((BitmapDrawable)bPhoto.getDrawable()).getBitmap());
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REGISTER_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    CameraManager.dispatchTakePictureIntent(this, REGISTER_CAMERA_REQUEST_CODE);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap.eraseColor(Color.TRANSPARENT);
        if (requestCode == CameraManager.REGISTER_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get("data");
            ImageButton b = (ImageButton) findViewById(R.id.bPhoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, b.getWidth(), b.getHeight(), true);
            b.setImageBitmap(bitmap);
        }
    }
}
