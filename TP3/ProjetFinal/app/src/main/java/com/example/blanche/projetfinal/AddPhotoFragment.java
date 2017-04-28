package com.example.blanche.projetfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class AddPhotoFragment extends Fragment {

    private final int ADDPHOTO_CAMERA_REQUEST_CODE = 101;
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
        final Button buttonUploadPhoto = (Button)view.findViewById(R.id.bUploadPhoto);
        final EditText etFileName = (EditText)view.findViewById(R.id.etFileName);
        final EditText etDescription = (EditText)view.findViewById(R.id.etDescription);
        final TextView tvDate = (TextView)view.findViewById(R.id.tvDate);

        buttonLoadPhoto.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                CameraManager.accessPhotoLibrary(getActivity(), MY_PHOTOLIBRARY_REQUEST_CODE);
            }});

        buttonTakePhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.dispatchTakePictureIntent(getActivity(), ADDPHOTO_CAMERA_REQUEST_CODE);
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

        buttonUploadPhoto.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                ImageView iv = (ImageView)getView().findViewById(R.id.targetimage);

                DatabaseManager.addPhotoToBD(etFileName.getText().toString(), tvDate.getText().toString(),
                        etDescription.getText().toString(),((BitmapDrawable)iv.getDrawable()).getBitmap(),
                        getContext(), getView(), MapFragment.mCurrentLocation);
            }
        });

        return view;
    }
}