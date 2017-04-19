package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static java.security.AccessController.getContext;

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

        View v = mInflater.inflate(R.layout.markerinfowindow, null);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
