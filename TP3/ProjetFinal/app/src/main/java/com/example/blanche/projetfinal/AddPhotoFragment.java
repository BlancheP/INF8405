package com.example.blanche.projetfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class AddPhotoFragment extends Fragment {

    private final int MY_CAMERA_REQUEST_CODE = 101;
    private final int MY_PHOTOLIBRARY_REQUEST_CODE = 201;

    public AddPhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        final Button buttonLoadPhoto = (Button)view.findViewById(R.id.loadphoto);
        final Button buttonTakePhoto = (Button)view.findViewById(R.id.takephoto);
        final Button buttonCancelPhoto = (Button)view.findViewById(R.id.cancelphoto);
        final LinearLayout l = (LinearLayout) view.findViewById(R.id.layout_upload);
        l.setVisibility(View.GONE);

        buttonLoadPhoto.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                CameraManager.accessPhotoLibrary(getActivity(), MY_PHOTOLIBRARY_REQUEST_CODE);
            }});

        buttonTakePhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.dispatchTakePictureIntent(getActivity(), MY_CAMERA_REQUEST_CODE);
            }
        });

        buttonCancelPhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTakePhoto.setVisibility(Button.VISIBLE);
                buttonLoadPhoto.setVisibility(Button.VISIBLE);
                ImageView iv = (ImageView)getView().findViewById(R.id.targetimage);
                iv.setVisibility(ImageView.GONE);
                getView().findViewById(R.id.layout_upload).setVisibility(View.GONE);
            }
        });

        return view;
    }
}