package com.example.blanche.projetfinal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private final int HOME_CAMERA_REQUEST_CODE = 102;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if(NetworkManager.hasValidConnectivity(getContext())){
            DatabaseManager.loadProfile(getActivity(), view);
        } else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Cannot Download Feed")
                    .setMessage("To download feed, please make sure that your connectivity settings match your phone's current connectivity")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        Button bChangePhoto = (Button)view.findViewById(R.id.bChangePhoto);
        bChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.hasValidConnectivity(getContext())) {
                    CameraManager.dispatchTakePictureIntent(getActivity(), HOME_CAMERA_REQUEST_CODE);
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cannot Change Profile Picture")
                            .setMessage("In order to change profile picture, please make sure that your connectivity settings match your phone's current connectivity")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        Button bChangePassword = (Button)view.findViewById(R.id.bChangePassword);
        bChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkManager.hasValidConnectivity(getContext())) {
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

                } else {

                    new AlertDialog.Builder(getContext())
                            .setTitle("Cannot Change Password")
                            .setMessage("In order to change password, please make sure that your connectivity settings match your phone's current connectivity")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        TextView tvNbFollowers = (TextView)view.findViewById(R.id.tvNbFollowers);
        tvNbFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.hasValidConnectivity(getContext())) {
                    DatabaseManager.getFollowers(getActivity());
                }
            }
        });

        TextView tvNbFollowing = (TextView)view.findViewById(R.id.tvNbFollowing);
        tvNbFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.hasValidConnectivity(getContext())) {
                    DatabaseManager.getFollowing(getActivity());
                }
            }
        });

        TextView tvFollowers = (TextView)view.findViewById(R.id.tvFollowers);
        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.hasValidConnectivity(getContext())) {
                    DatabaseManager.getFollowers(getActivity());
                }
            }
        });

        TextView tvFollowing = (TextView)view.findViewById(R.id.tvFollowing);
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkManager.hasValidConnectivity(getContext())) {
                    DatabaseManager.getFollowing(getActivity());
                }
            }
        });

        if(NetworkManager.hasValidConnectivity(getContext())) {
            final GridView gridView = (GridView) view.findViewById(R.id.gvPhotoLibrary);
            final GridViewAdapter gridAdapter = new GridViewAdapter(this.getContext(), R.layout.photo_library_item_layout, DatabaseManager.getMyImageItems());
            gridView.setAdapter(gridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, int position, long id) {
                    final ImageItem item = (ImageItem) adapterView.getItemAtPosition(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.photo_details_layout, null);

                    dialog.setView(dialogView);

                    dialog.setPositiveButton("DELETE", null)
                            .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    final AlertDialog d = dialog.create();

                    d.show();

                    ImageView image = (ImageView) dialogView.findViewById(R.id.ivPhotoDetails);
                    TextView filename = (TextView) dialogView.findViewById(R.id.tvPhotoDetailsTitle);
                    TextView date = (TextView) dialogView.findViewById(R.id.tvPhotoDetailsDate);
                    TextView description = (TextView) dialogView.findViewById(R.id.tvPhotoDetailsDesc);


                    image.setImageBitmap(item.getImage());
                    filename.setText(item.getTitle());
                    date.setText(item.getDate());
                    description.setText(item.getDescription());

                    d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseManager.deletePhoto(item.getTitle());
                            d.dismiss();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new ProfileFragment());
                            fragmentTransaction.commit();
                        }
                    });
                }
            });

        }

        return view;
    }



}
