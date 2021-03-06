package com.example.blanche.projetfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;

public class PowerLevelReceiver extends BroadcastReceiver {
    static int initialPowerLevel = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "Careful! Your battery is low!! Please minimize your usage of the application!", Toast.LENGTH_LONG);
        }
        else if (intent.getAction().equals(ACTION_BATTERY_OKAY))
            Toast.makeText(context, "Your battery is now okay, you can continue to use the application as normal :)", Toast.LENGTH_LONG);
    }
}
