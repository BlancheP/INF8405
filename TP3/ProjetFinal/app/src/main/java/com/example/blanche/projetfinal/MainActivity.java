package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final int MY_PHOTOLIBRARY_REQUEST_CODE = 201;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_map:
                    fragmentTransaction.replace(R.id.fragment_container, new MapFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    fragmentTransaction.replace(R.id.fragment_container, new SearchFragment());
                    fragmentTransaction.commit();
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
        BottomNavigationViewHelper.disableShiftMode(navigation);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MapFragment());
        fragmentTransaction.commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ImageView addPhoto = (ImageView) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AddPhotoFragment());
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent goToSettings = new Intent(this, SettingsActivity.class);
                startActivity(goToSettings);
                return true;
            case R.id.logout:
                Intent goToLogin = new Intent(this, LoginActivity.class);
                PreferencesManager pm = DatabaseManager.getPreferencesManager();
                pm.removeCurrentUser();
                startActivity(goToLogin);
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CameraManager.ADDPHOTO_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    CameraManager.dispatchTakePictureIntent(this, CameraManager.ADDPHOTO_CAMERA_REQUEST_CODE);
                break;
            case CameraManager.ADDPHOTO_PHOTOLIBRARY_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    CameraManager.accessPhotoLibrary(this, MY_PHOTOLIBRARY_REQUEST_CODE);
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CameraManager.REGISTER_CAMERA_REQUEST_CODE || requestCode == CameraManager.ADDPHOTO_CAMERA_REQUEST_CODE) && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            setImageView(bitmap);
        }
        else if (requestCode == CameraManager.ADDPHOTO_PHOTOLIBRARY_REQUEST_CODE && resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                setImageView(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CameraManager.HOME_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            DatabaseManager.addProfilePhotoToBD(DatabaseManager.getPreferencesManager().getCurrentUser(), bitmap);
            ImageView iv = (ImageView)findViewById(R.id.ivProfilePic);
            bitmap = resize(bitmap, iv);
            iv.setImageBitmap(bitmap);
        }
    }

    public void setImageView(Bitmap bitmap) {
        ImageView iv = (ImageView)findViewById(R.id.targetimage);
        bitmap = resize(bitmap, iv);
        iv.setImageBitmap(bitmap);
        iv.setVisibility(Button.VISIBLE);
        findViewById(R.id.takephoto).setVisibility(Button.GONE);
        findViewById(R.id.loadphoto).setVisibility(Button.GONE);
        findViewById(R.id.layout_upload).setVisibility(View.VISIBLE);
        TextView tv = (TextView) findViewById(R.id.tvDate);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        tv.setText(date);
    }

    public Bitmap resize(Bitmap image, ImageView iv) {
        int width = image.getWidth();
        int height = image.getHeight();
        float finalWidth;
        float finalHeight;
        if (width > height) {
            finalWidth = iv.getWidth();
            finalHeight = finalWidth * (float)height / (float)width;
        }
        else {
            finalHeight = iv.getHeight();
            finalWidth = finalHeight * (float)width / (float)height;
        }
        image = Bitmap.createScaledBitmap(image, (int)finalWidth, (int)finalHeight, true);
        return image;
    }
}
