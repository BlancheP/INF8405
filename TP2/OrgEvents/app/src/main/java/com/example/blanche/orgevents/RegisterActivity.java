package com.example.blanche.orgevents;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final ImageButton bPhoto = (ImageButton) findViewById(R.id.bPhoto);
        final TextView tvHelpPhoto = (TextView) findViewById(R.id.tvHelpPhoto);

        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                dispatchTakePictureIntent();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w) {

                if (!DatabaseManager.userExists(etUsername.getText().toString())) {
                    DatabaseManager.addUser(etUsername.getText().toString(),
                            etPassword.getText().toString());
                    Intent refresh = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                    Toast done = Toast.makeText(getApplicationContext(), "You have been successfully registered!", Toast.LENGTH_SHORT);
                    done.show();
                }
                else {
                    etUsername.setError("A user with the same username already exists!");
                    Toast done = Toast.makeText(getApplicationContext(), "Some fields are invalid!", Toast.LENGTH_SHORT);
                    done.show();
                }
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            ImageButton b = (ImageButton) findViewById(R.id.bPhoto);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, b.getWidth(), b.getHeight(), true);
            b.setImageBitmap(imageBitmap);
        }
    }
}
