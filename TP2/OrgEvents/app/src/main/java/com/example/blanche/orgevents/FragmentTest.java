package com.example.blanche.orgevents;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alain.trandang on 2017-03-23.
 */

public class FragmentTest extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflating the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test_layout, null);
        return v;
    }

}
