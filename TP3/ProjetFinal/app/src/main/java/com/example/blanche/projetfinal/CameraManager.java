package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CameraManager {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private CameraManager() {}

    public static  boolean checkIfAlreadyHavePermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void requestForSpecificPermission(Activity activity, int MY_CAMERA_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
    }

    public static void dispatchTakePictureIntent(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static int getRequestImageCapture() {
        return REQUEST_IMAGE_CAPTURE;
    }
}