package com.example.blanche.projetfinal;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.SENSOR_SERVICE;


public class HomeFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor sensor;
    public static int index = 0;
    private SensorEventListener sensorEventListener;
    private long lastTime;


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
                long now = System.currentTimeMillis();
                if (NetworkManager.hasValidConnectivity(getContext())) {
                    if (event.values[0] > 5 && now - lastTime > 100) {

                        ++index;
                        loadPhotos();


                    }
                    lastTime = now;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);


        if(NetworkManager.hasValidConnectivity(getContext())) {
            DatabaseManager.loadHomePhoto(view, this.getContext());
        }

        else{
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
