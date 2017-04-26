package com.example.blanche.projetfinal;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        TextView tvNbFollowers = (TextView)view.findViewById(R.id.tvNbFollowers);
        tvNbFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.getFollowers(getActivity());
            }
        });

        TextView tvNbFollowing = (TextView)view.findViewById(R.id.tvNbFollowing);
        tvNbFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.getFollowing(getActivity());
            }
        });

        TextView tvFollowers = (TextView)view.findViewById(R.id.tvFollowers);
        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.getFollowers(getActivity());
            }
        });

        TextView tvFollowing = (TextView)view.findViewById(R.id.tvFollowing);
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.getFollowing(getActivity());
            }
        });

        GridView gridView = (GridView) view.findViewById(R.id.gvPhotoLibrary);
        GridViewAdapter gridAdapter = new GridViewAdapter(this.getContext(), R.layout.photo_library_item_layout, DatabaseManager.getMyImageItems());
        gridView.setAdapter(gridAdapter);

        return view;
    }



}