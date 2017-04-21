package com.example.blanche.projetfinal;

import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.Marker;

import com.squareup.picasso.Callback;

/**
 * Created by alain.trandang on 2017-04-20.
 */

public class MarkerInfoWindowRefresher implements Callback {

    private Marker markerToRefresh;

    public MarkerInfoWindowRefresher(Marker markerToRefresh) {
        this.markerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {
        markerToRefresh.hideInfoWindow();
        markerToRefresh.showInfoWindow();
    }

    @Override
    public void onError() {
        Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
    }

}


