package com.example.blanche.orgevents;

import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Console;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private final int REQUEST_PERMISSION_PHONE_STATE = 1; // constant for the permission callack
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final float MIN_ZOOM_LEVEL = 14f;
    private final float MAX_ZOOM_LEVEL = 14f;

    private boolean updateUIFirstTime = true;

    private Location currentLocation;
    private Location previousLocation;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Toast.makeText(MapsActivity.this, "API CLIENT BUILT", Toast.LENGTH_SHORT).show();
        }

        // Create the LocationRequest object
        //TODO: manage the case where the battery is low
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) // high accuracy requests require more time and power
                .setInterval(30 * 1000)        // 30 seconds, in milliseconds; frequency that we want location updates - faster updates = more power!
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds; if a location is available sooner we can get it without extra power (i.e. another app is using the location services)
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Toast.makeText(MapsActivity.this, "API CLIENT CONNECTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            Toast.makeText(MapsActivity.this, "API CLIENT DISCONNECTED", Toast.LENGTH_SHORT).show();
        }
    }

    // google play service onConnected method (google play service provides location tracking)
    @Override
    public void onConnected(Bundle connectionHint) {

        Toast.makeText(MapsActivity.this, "onConnected() CALLED", Toast.LENGTH_SHORT).show();
        showPhoneStatePermission(); // this will call zoomToThisLocation() if the permission is granted
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

        /*
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {

                Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                Toast.makeText(MapsActivity.this, "New Marker at Lat: " + marker.getPosition().latitude
                        + ", Long: " + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    // code to grant location tracking permission :
    private void showPhoneStatePermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
            }
        }
        else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation == null) {

                Toast.makeText(MapsActivity.this, "mLastLocation is null", Toast.LENGTH_SHORT).show();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else {
                zoomToThisLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(MapsActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });

        builder.create().show();
    }

    // code to start and track locations updates:
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, createLocationRequest(), this);
        }
        catch (SecurityException e) {
            Log.e("location", "permission denied");
        }
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    private void zoomToThisLocation(double latitude, double longitude) {
        // update map here
        if (mMap != null) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showExplanation("Permission Needed", "Rationale", android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                }
                else {
                    requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_PHONE_STATE);
                }
            }
            else {
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), MAX_ZOOM_LEVEL));
                //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker of my current position"));
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*
    This method gets called every time a new location is detected by Google Play Services.
    So as the user is moving around with their phone or tablet, the location APIs are updating
    the location silently in the background. When the current location is updated, this method
    is called, and we can now handle it in our Activity.
     */
    @Override
    public void onLocationChanged(Location location) {
        previousLocation = currentLocation;
        currentLocation = location;
        try {
            Toast.makeText(MapsActivity.this, "OnLocationChanged() CALLED", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(true);
        } catch(SecurityException e) {

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Toast.makeText(MapsActivity.this,
                "New Marker Added:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

        //Add marker on LongClick position
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(latLng.toString());
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
