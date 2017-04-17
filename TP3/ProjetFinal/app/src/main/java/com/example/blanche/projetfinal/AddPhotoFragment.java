package com.example.blanche.projetfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class AddPhotoFragment extends Fragment {

    private final int MY_CAMERA_REQUEST_CODE = 101;

    public AddPhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        final Button buttonLoadPhoto = (Button)view.findViewById(R.id.loadphoto);
        final Button buttonTakePhoto = (Button)view.findViewById(R.id.takephoto);
        final Button buttonCancelPhoto = (Button)view.findViewById(R.id.cancelphoto);
        final Button buttonNext = (Button)view.findViewById(R.id.next);

        buttonLoadPhoto.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
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
                buttonCancelPhoto.setVisibility(Button.GONE);
                buttonNext.setVisibility(Button.GONE);
                buttonTakePhoto.setVisibility(Button.VISIBLE);
                buttonLoadPhoto.setVisibility(Button.VISIBLE);
                ImageView iv = (ImageView)getView().findViewById(R.id.targetimage);
                iv.setVisibility(ImageView.GONE);
            }
        });

        buttonNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }
}