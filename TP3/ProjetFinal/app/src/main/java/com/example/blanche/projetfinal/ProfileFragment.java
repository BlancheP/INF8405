package com.example.blanche.projetfinal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private final int HOME_CAMERA_REQUEST_CODE = 102;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseManager.loadProfile(getActivity(), view);

        Button bChangePhoto = (Button)view.findViewById(R.id.bChangePhoto);
        bChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.dispatchTakePictureIntent(getActivity(), HOME_CAMERA_REQUEST_CODE);
            }
        });

        Button bChangePassword = (Button)view.findViewById(R.id.bChangePassword);
        bChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Change password");
                LayoutInflater inflater = getActivity().getLayoutInflater();
                dialog.setView(inflater.inflate(R.layout.change_password, null));

                dialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });

        return view;
    }



}
