package com.example.blanche.projetfinal;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by alain.trandang on 2017-04-19.
 */

public class MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mInflater;

    public MarkerInfoWindow(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        View v = mInflater.inflate(R.layout.fragment_markerinfowindow, null);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
