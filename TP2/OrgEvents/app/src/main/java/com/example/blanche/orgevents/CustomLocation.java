package com.example.blanche.orgevents;

/**
 * Created by alain.trandang on 2017-03-16.
 */

class CustomLocation {

    String mlocationName;
    double mlatitude;
    double mlongitude;

    CustomLocation(String locationName, double latitude, double longitude){
        this.mlocationName = locationName;
        this.mlatitude = latitude;
        this.mlongitude = longitude;
    }
}
