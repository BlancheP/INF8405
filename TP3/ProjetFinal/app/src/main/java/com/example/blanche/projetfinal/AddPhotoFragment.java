package com.example.blanche.projetfinal;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class AddPhotoFragment extends Fragment {

    private final int MY_CAMERA_REQUEST_CODE = 101;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_photo, container, false);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CameraManager.checkIfAlreadyHavePermission(getActivity()))
            CameraManager.dispatchTakePictureIntent(getActivity());
        else
            CameraManager.requestForSpecificPermission(getActivity(), MY_CAMERA_REQUEST_CODE);
    }
}