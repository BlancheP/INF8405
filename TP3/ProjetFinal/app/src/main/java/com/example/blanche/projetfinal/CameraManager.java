package com.example.blanche.projetfinal;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CameraManager {

    static final int REGISTER_CAMERA_REQUEST_CODE = 100;
    static final int ADDPHOTO_CAMERA_REQUEST_CODE = 101;
    static final int ADDPHOTO_PHOTOLIBRARY_REQUEST_CODE = 201;
    static final int HOME_CAMERA_REQUEST_CODE = 102;

    private CameraManager() {}

    public static  boolean checkIfAlreadyHavePermission(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void requestForSpecificPermission(Activity activity, int request_code, String permission) {
        ActivityCompat.requestPermissions(activity, new String[] {permission}, request_code);
    }

    public static void dispatchTakePictureIntent(Activity activity, int code) {

        if (checkIfAlreadyHavePermission(activity, Manifest.permission.CAMERA))
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(takePictureIntent, code);
            }
        }
        else
            requestForSpecificPermission(activity, code, Manifest.permission.CAMERA);
    }

    public static void accessPhotoLibrary(Activity activity, int code) {
        if (checkIfAlreadyHavePermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(intent, ADDPHOTO_PHOTOLIBRARY_REQUEST_CODE);
        }
        else
            requestForSpecificPermission(activity, code, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}