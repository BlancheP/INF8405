package com.example.blanche.projetfinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private final int HOME_CAMERA_REQUEST_CODE = 102;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseManager.loadProfile(getActivity(), view);

        TextView tv = (TextView)view.findViewById(R.id.tvChangePhoto);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.dispatchTakePictureIntent(getActivity(), HOME_CAMERA_REQUEST_CODE);
            }
        });
        return view;
    }



}
