package com.example.blanche.projetfinal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ProfileFragment extends Fragment {

    private final int HOME_CAMERA_REQUEST_CODE = 102;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
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
                final View dialogView = inflater.inflate(R.layout.change_password, null);
                dialog.setView(dialogView);

                dialog.setPositiveButton("SAVE", null)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog d = dialog.create();
                d.show();

                d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        EditText etOldPwd = (EditText)dialogView.findViewById(R.id.etOldPassword);
                        EditText etNewPwd = (EditText)dialogView.findViewById(R.id.etNewPassword);
                        EditText etNewPwd2 = (EditText)dialogView.findViewById(R.id.etConfirmPassword);

                        if (etOldPwd.getText().toString().trim().length() <=0)
                            etOldPwd.setError("You must enter your current password");

                        if (etNewPwd.getText().toString().trim().length() <= 0)
                            etNewPwd.setError("You must enter a new password");

                        if (etNewPwd2.getText().toString().trim().length() <= 0)
                            etNewPwd2.setError("You must enter something");

                        if (etOldPwd.getText().toString().trim().length() > 0 &&
                                etNewPwd.getText().toString().trim().length() > 0 &&
                                etNewPwd2.getText().toString().trim().length() > 0) {
                            DatabaseManager.changePassword(getActivity(), etOldPwd.getText().toString(),
                                    etNewPwd.getText().toString(), etNewPwd2.getText().toString(), dialogView, d);
                        }
                    }
                });

            }
        });

        return view;
    }



}
