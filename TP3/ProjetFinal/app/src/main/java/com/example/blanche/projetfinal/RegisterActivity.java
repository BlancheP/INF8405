package com.example.blanche.projetfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class RegisterActivity extends Fragment {

    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflating the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_register, null);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(5, 5, conf);

        final EditText etUsername = (EditText) view.findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
        final Button bRegister = (Button) view.findViewById(R.id.bRegister);
        final ImageButton bPhoto = (ImageButton) view.findViewById(R.id.bPhoto);

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
                    Toast profilPicture = Toast.makeText(getActivity(), "You must take a profile picture.", Toast.LENGTH_SHORT);
                    profilPicture.show();
                }
                else {
                    DatabaseManager.addUser(etPassword.getText().toString(), etUsername, getActivity(), ((BitmapDrawable)bPhoto.getDrawable()).getBitmap());
                }
            }
        });

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap.eraseColor(Color.TRANSPARENT);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap)extras.get("data");
            ImageButton b = (ImageButton) getActivity().findViewById(R.id.bPhoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, b.getWidth(), b.getHeight(), true);
            b.setImageBitmap(bitmap);
        }
    }
}
