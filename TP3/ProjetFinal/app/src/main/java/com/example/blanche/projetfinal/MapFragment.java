package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        LocationListener {

    static GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    GoogleApiClient mGoogleApiClient;

    public static Location mCurrentLocation;
    private static LocationRequest mLocationRequest;

    private static final int CURRENT_LOCATION_UPDATE_INTERVAL = 3000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        initMarkerImages();

        /*
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        final LatLng sydney = new LatLng(-33.852, 151.211);
        final ImageView v = new ImageView(getContext());
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/projetfinal-481cd.appspot.com/o/rob%2Fcirque?alt=media&token=01323dbb-bead-4c24-a793-776727cb1f0e");
        Picasso.with(getContext()).load(uri).into(v, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                //do smth when picture is loaded successfully

                Bitmap bitmap = ((BitmapDrawable)v.getDrawable()).getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                MarkerOptions marker = new MarkerOptions().position(sydney).title("My marker").snippet("Thinking of finding some thing...").icon(icon);
                googleMap.addMarker(marker);
            }

            @Override
            public void onError() {
            }

        });

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }


        /*
        MarkerInfoWindow markerInfoWindow = new MarkerInfoWindow(getActivity().getLayoutInflater());
        mGoogleMap.setInfoWindowAdapter(markerInfoWindow);
        */
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(CURRENT_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*
    This method gets called every time a new location is detected by Google Play Services.
    So as the user is moving around with their phone or tablet, the location APIs are updating
    the location silently in the background. When the current location is updated, this method
    is called, and we can now handle it in our Activity.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        try {
            //Toast.makeText(getActivity(), "OnLocationChanged() CALLED", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), "Current Long: " + mCurrentLocation.getLongitude() + " Current Lat: " + mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            Log.d("MapsActivity", "onLocationChanged CALLED");
            //mGoogleMap.setMyLocationEnabled(true);
        } catch(SecurityException e) {

        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    //fonction qui mets sur la map la photo la plus recente de tous les users de l'application
    public void initMarkerImages(){

        Map<String, ImageItem> markerImages = DatabaseManager.getMarkerImages();

        for (Map.Entry<String, ImageItem> image : markerImages.entrySet()) {

            if(image.getValue().getLatitude() != null && image.getValue().getLongitude() != null ) {

                Bitmap imageToDisplay = Bitmap.createScaledBitmap(image.getValue().getImage(), 150, 150, false);
                BitmapDescriptor iconWithImage = BitmapDescriptorFactory.fromBitmap(imageToDisplay);

                MarkerOptions markerOptions = new MarkerOptions().position(
                        new LatLng(
                                image.getValue().getLatitude(),
                                image.getValue().getLongitude()))
                        .title(image.getKey())
                        .icon(iconWithImage);

                mGoogleMap.addMarker(markerOptions);

            }
        }


    }

}