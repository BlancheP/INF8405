package com.example.blanche.projetfinal;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.SENSOR_SERVICE;


public class HomeFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor sensor;
    public static int index = 0;
    public static boolean justChanged = false;
    private SensorEventListener sensorEventListener;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 2 && !justChanged) {
                    ++index;
                    justChanged = true;
                    loadPhotos();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

        DatabaseManager.loadHomePhoto(view, this.getContext());


        return view;
    }

    private void loadPhotos(){
        DatabaseManager.loadHomePhoto(getView(), this.getContext());

    }
    @Override
    public void onStop(){
        sensorManager.unregisterListener(sensorEventListener);
        super.onStop();
    }
}
