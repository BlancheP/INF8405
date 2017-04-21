package com.example.blanche.projetfinal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;


public class DashboardFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor sensor;
    public static int index = 0;
    public static boolean justChanged = false;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);




       /* final TextView username = (TextView)view.findViewById(R.id.tvDashUsername);
        final TextView date = (TextView)view.findViewById(R.id.tvDashDate);
        final TextView filename = (TextView)view.findViewById(R.id.tvDashFilename);
        final TextView descr = (TextView)view.findViewById(R.id.tvDashDescr);
        final ImageView photo = (ImageView) view.findViewById(R.id.ivDashPhoto);


        final Context dashContext = this.getContext();*/

        //TODO: Faire en sorte que le mouvement change la photo en temps reel
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] > 2 && !justChanged) {
                    ++index;
                    justChanged = true;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_NORMAL);

        DatabaseManager.loadDashboardPhoto(this.getContext());

        return view;
    }
}
