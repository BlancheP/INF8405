package com.example.blanche.projetfinal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class MainActivity extends AppCompatActivity {

    private final int MY_CAMERA_REQUEST_CODE = 101;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.fragment_container, new DashboardFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_profile:
                    fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_map:
                    fragmentTransaction.replace(R.id.fragment_container, new MapFragment());
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new DashboardFragment());
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
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    CameraManager.dispatchTakePictureIntent(this, MY_CAMERA_REQUEST_CODE);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraManager.getRequestImageCapture() && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            ImageView iv = (ImageView) findViewById(R.id.targetimage);
            iv.setVisibility(ImageView.VISIBLE);
            iv.setImageBitmap(bitmap);
            Button b = (Button)findViewById(R.id.takephoto);
            b.setVisibility(Button.GONE);
            Button c = (Button)findViewById(R.id.cancelphoto);
            c.setVisibility(Button.VISIBLE);
            Button t = (Button)findViewById(R.id.loadphoto);
            t.setVisibility(Button.GONE);
            Button n = (Button)findViewById(R.id.next);
            n.setVisibility(Button.VISIBLE);
        }
        else if (resultCode == RESULT_OK){
            /*Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
        }
    }
}
